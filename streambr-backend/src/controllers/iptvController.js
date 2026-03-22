const { setupDatabase } = require('../models/db');
const M3UParser = require('../utils/M3UParser');
const axios = require('axios');

// Fontes locais premium (Lista de TV por assinataura)
const DEFAULT_SOURCES = [
    { name: 'Premium TV', url: 'C:\\Users\\luizb\\Downloads\\gilmariacotias-full.m3u' }
];

async function getChannels(req, res) {
    const db = await setupDatabase();
    try {
        const host = req.get('host');
        const protocol = req.protocol === 'https' || req.get('x-forwarded-proto') === 'https' ? 'https' : 'http';
        const baseUrl = `${protocol}://${host}`;

        const channels = await db.all('SELECT * FROM channels ORDER BY group_name, name');
        const groups = [...new Set(channels.map(c => c.group_name))];

        res.json({
            total: channels.length,
            page: 1,
            channels: channels.map(c => ({
                id: c.id,
                name: c.name,
                logo: c.logo,
                group: c.group_name,
                quality: c.quality,
                tvgId: c.tvgId || '',
                // Redireciona para o Proxy do Backend para contornar bloqueios
                streamUrl: `${baseUrl}/api/stream/${c.id}`,
                hlsUrl: `${baseUrl}/api/stream/${c.id}`,
                isAdult: c.is_adult === 1
            })),
            groups: groups
        });
    } catch (error) {
        res.status(500).json({ message: 'Erro ao buscar canais' });
    }
}

async function proxyStream(req, res) {
    const { id } = req.params;
    const db = await setupDatabase();
    
    try {
        const channel = await db.get('SELECT stream_url FROM channels WHERE id = ?', [id]);
        if (!channel) return res.status(404).send('Canal não encontrado');

        console.log(`🎬 Proxying stream: ${channel.stream_url}`);

        // Cabeçalhos para simular um player comum e evitar bloqueios
        const headers = {
            'User-Agent': 'VLC/3.0.18 (LibVLC 3.0.18)',
            'Accept': '*/*',
            'Connection': 'keep-alive'
        };

        const response = await axios({
            method: 'get',
            url: channel.stream_url,
            responseType: 'stream',
            headers: headers,
            timeout: 20000
        });

        // Repassa os cabeçalhos de conteúdo do provedor original
        res.setHeader('Content-Type', response.headers['content-type'] || 'video/mp2t');
        if (response.headers['content-length']) {
            res.setHeader('Content-Length', response.headers['content-length']);
        }

        response.data.pipe(res);

        response.data.on('error', (err) => {
            console.error('❌ Erro no stream pipe:', err.message);
            res.end();
        });

        req.on('close', () => {
            // Aborta a requisição ao provedor se o cliente fechar a conexão
            response.data.destroy();
        });

    } catch (error) {
        console.error('❌ Erro no Proxy:', error.message);
        res.status(500).send('Erro ao carregar stream');
    }
}

async function getVod(req, res) {
    // Mock VOD for now
    res.json({
        total: 1,
        items: [
            {
                id: '1',
                title: 'Exemplo de Filme',
                year: 2024,
                genre: ['Ação'],
                duration: 120,
                rating: 8.5,
                plot: 'Um filme de exemplo para testar o app.',
                posterUrl: 'https://via.placeholder.com/300x450.png?text=Filme+Exemplo',
                quality: '4K',
                streamUrl: 'https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8'
            }
        ]
    });
}

async function syncChannels(req, res) {
    const { m3uUrl } = req.body;
    const sources = m3uUrl ? [{ name: 'Custom', url: m3uUrl }] : DEFAULT_SOURCES;
    
    try {
        const db = await setupDatabase();
        await db.run('DELETE FROM channels'); 
        console.log('🧹 Banco limpo. Iniciando importação inteligente...');

        const allChannels = [];
        for (const source of sources) {
            console.log(`📡 Coletando: ${source.name}`);
            try {
                await M3UParser.parseStream(
                  source.url, 
                  async (c) => {
                    // Normalização básica para agrupamento
                    const cleanGroup = c.category.replace(/^Canais\s*\|\s*/i, '');
                    
                    // Extração de metadados para decisão
                    const name = c.name.toUpperCase();
                    const is4K = /4K|ULTRA\s*HD/i.test(name);
                    const isHD = /FHD|HD/i.test(name);
                    const isSP = /SP|SAO\s*PAULO|SÃO\s*PAULO/i.test(name) || /GLOBO\s+[A-Z]+\s+SP/i.test(name);
                    
                    // Nome base (sem qualidade e tags de região redundantes para Globo/SBT/etc)
                    let baseName = c.name
                        .replace(/\s+(4K|FHD|FHD\s*\+\s*H265|HD|SD|ULTRA\s*HD|LOW|HEVC|H265|H\.265)\b/gi, '')
                        .replace(/\s*\[.*?\]/g, '')
                        .trim();

                    // Se for Globo, SBT, Band ou Record, removemos a cidade do nome base para deduplicar
                    // Mas mantemos se for um canal numerado (ex: Premiere 1, Sportv 2)
                    if (/(GLOBO|SBT|BAND|RECORD|BANDSPORTS|SPORTV|PREMIERE)\s/i.test(baseName)) {
                        // Se não tiver número, removemos prefixos regionais como - SP, (SP), RJ, etc.
                        if (!/\s\d+$/i.test(baseName)) {
                           baseName = baseName.replace(/\s+([A-Z]{2}|SAO\s*PAULO|SÃO\s*PAULO|RJ|MG|RS|PR|BA|PE|CE|SC|DF|GO|ES|MS|MT|AL|AM|AP|PA|PB|PI|RN|RO|RR|SE|TO)\b/gi, '').trim();
                        }
                    }

                    allChannels.push({
                        ...c,
                        group: cleanGroup,
                        baseName,
                        is4K,
                        isHD,
                        isSP,
                        qualityTier: is4K ? 1 : (isHD ? 2 : 3)
                    });
                  },
                  { onlyCanais: true }
                );
            } catch (err) {
                console.error(`❌ Erro na fonte ${source.name}:`, err.message);
            }
        }

        console.log(`🔍 Processando ${allChannels.length} canais para remover duplicados...`);

        // Agrupamento por Canal + Categoria
        const groups = {};
        for (const c of allChannels) {
            const key = `${c.group}|${c.baseName.toUpperCase()}`;
            if (!groups[key]) groups[key] = { tier1: [], tier2: [], tier3: [] };
            
            if (c.qualityTier === 1) groups[key].tier1.push(c);
            else if (c.qualityTier === 2) groups[key].tier2.push(c);
            else groups[key].tier3.push(c);
        }

        const finalists = [];
        for (const key in groups) {
            const g = groups[key];
            
            // Função para escolher o melhor de um tier (prioridade SP)
            const pickBest = (list) => {
                if (list.length === 0) return null;
                const sp = list.find(x => x.isSP);
                return sp || list[0];
            };

            const best4K = pickBest(g.tier1);
            const bestHD = pickBest(g.tier2);
            
            if (best4K) finalists.push(best4K);
            if (bestHD) finalists.push(bestHD);
            
            // Se não tiver nenhum de alta qualidade, pega o SD (tier 3) para não perder o canal
            if (!best4K && !bestHD) {
                const bestSD = pickBest(g.tier3);
                if (bestSD) finalists.push(bestSD);
            }
        }

        console.log(`💾 Salvando ${finalists.length} canais otimizados no banco...`);
        
        let totalSincronizado = 0;
        const stmt = await db.prepare(
            'INSERT INTO channels (id, name, logo, group_name, quality, stream_url, hls_url, is_adult) VALUES (?, ?, ?, ?, ?, ?, ?, ?)'
        );

        for (const c of finalists) {
            totalSincronizado++;
            const qualityLabel = c.qualityTier === 1 ? '4K' : (c.qualityTier === 2 ? 'HD' : 'SD');
            await stmt.run(
                String(totalSincronizado),
                c.name,
                c.logo,
                c.group,
                qualityLabel,
                c.url,
                c.url,
                0
            );
        }
        await stmt.finalize();

        res.json({ 
            success: true, 
            message: `Sincronização otimizada concluída: ${totalSincronizado} canais (de ${allChannels.length} originais)`,
            total: totalSincronizado 
        });

    } catch (error) {
        console.error('❌ Erro na sincronização:', error.message);
        res.status(500).json({ message: 'Falha na sincronização', error: error.message });
    }
}

module.exports = { getChannels, getVod, syncChannels, proxyStream };
