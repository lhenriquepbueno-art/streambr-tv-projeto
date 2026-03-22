const { setupDatabase } = require('./db');

async function seedData() {
    const db = await setupDatabase();
    
    // Inserir alguns canais mock se a tabela estiver vazia
    const count = await db.get('SELECT COUNT(*) as total FROM channels');
    if (count.total === 0) {
        const mockChannels = [
            {
                id: 'ch1',
                name: 'Globo RJ HD',
                logo: 'https://upload.wikimedia.org/wikipedia/commons/thumb/a/af/TV_Globo_logo.svg/200px-TV_Globo_logo.svg.png',
                group_name: 'TV Aberta',
                quality: 'HD',
                stream_url: 'https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8',
                hls_url: 'https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8',
                is_adult: 0
            },
            {
                id: 'ch2',
                name: 'SporTV HD',
                logo: 'https://upload.wikimedia.org/wikipedia/pt/4/4b/Logotipo_do_SporTV.png',
                group_name: 'Esportes',
                quality: 'HD',
                stream_url: 'https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8',
                hls_url: 'https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8',
                is_adult: 0
            },
            {
                id: 'ch3',
                name: 'Canal Adulto 1',
                logo: '',
                group_name: 'Adulto +18',
                quality: 'FHD',
                stream_url: 'https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8',
                hls_url: 'https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8',
                is_adult: 1
            }
        ];

        for (const ch of mockChannels) {
            await db.run(
                'INSERT INTO channels (id, name, logo, group_name, quality, stream_url, hls_url, is_adult) VALUES (?, ?, ?, ?, ?, ?, ?, ?)',
                [ch.id, ch.name, ch.logo, ch.group_name, ch.quality, ch.stream_url, ch.hls_url, ch.is_adult]
            );
        }
        console.log('✅ Dados de demonstração inseridos com sucesso!');
    }
}

seedData().catch(console.error);
