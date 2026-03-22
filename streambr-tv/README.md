# StreamBR TV — App Android TV / Firestick

App nativo em Kotlin para Android TV e Amazon Firestick, consumindo o backend StreamBR.

---

## Por que Kotlin nativo (e não Flutter ou React Native)?

| Necessidade | Kotlin | Flutter | React Native |
|---|---|---|---|
| D-pad (controle remoto) | Nativo via Leanback | Parcial | Manual / buggy |
| ExoPlayer (HLS) | API direta | Plugin extra | Plugin limitado |
| Android TV Leanback UI | SDK oficial | Customizado | Sem suporte |
| DRM Widevine | Nativo | Plugin | Incompleto |
| Performance em hardware fraco | Máxima | Muito boa | Média |

Fire TV Stick Lite tem apenas 1GB RAM e CPU quad-core 1.3GHz. Kotlin nativo usa a menor quantidade de memória possível.

---

## Estrutura do projeto

```
app/src/main/java/com/streambr/tv/
├── ui/
│   ├── SplashActivity.kt        ← Verifica token, decide onde ir
│   ├── PinActivity.kt           ← PIN para área adulta
│   ├── login/
│   │   └── LoginActivity.kt     ← Tela de login otimizada para TV
│   ├── home/
│   │   └── HomeActivity.kt      ← BrowseFragment com grid de canais/VOD
│   └── player/
│       └── PlayerActivity.kt    ← ExoPlayer full-screen com D-pad
├── data/
│   ├── api/
│   │   └── ApiService.kt        ← Retrofit + modelos de dados
│   └── repository/
│       └── ChannelRepository.kt ← Abstração da camada de dados
└── util/
    └── TokenManager.kt          ← JWT criptografado (EncryptedSharedPreferences)
```

---

## Setup no Android Studio

### Pré-requisitos
- Android Studio Hedgehog (2023.1.1) ou superior
- JDK 17
- Android SDK 34

### 1. Abrir o projeto

```bash
# Clone o repositório
git clone https://github.com/seu-user/streambr-tv
# Abra no Android Studio: File → Open → pasta streambr-tv
```

### 2. Configurar URL do backend

Edite `app/build.gradle`:

```gradle
// Desenvolvimento (IP do servidor na rede local)
buildConfigField "String", "API_BASE_URL", '"http://192.168.1.100:3000/"'

// Produção (HTTPS obrigatório)
buildConfigField "String", "API_BASE_URL", '"https://api.seudominio.com/"'
```

### 3. Gerar o APK

**Via Android Studio:**
`Build → Build Bundle(s) / APK(s) → Build APK(s)`

O APK gerado fica em:
`app/build/outputs/apk/debug/app-debug.apk`

---

## Instalar no Firestick (sideload)

### Método 1: ADB via rede (mais fácil)

```bash
# 1. No Firestick: Configurações → Meu Fire TV → Opções do desenvolvedor
#    → Depuração ADB: ON
#    → Aplicativos de fontes desconhecidas: ON

# 2. Descubra o IP do Firestick:
#    Configurações → Meu Fire TV → Sobre → Rede

# 3. No seu computador:
adb connect 192.168.1.XXX:5555
adb install app-debug.apk

# 4. O app aparece em: Aplicativos → Recentes
```

### Método 2: Downloader (sem computador)

1. Instale o app **Downloader** na Amazon Store
2. Suba o APK para qualquer servidor HTTP (Google Drive, Dropbox, etc.)
3. No Downloader, acesse a URL do APK e instale

### Método 3: Android TV

```bash
# Habilite depuração ADB nas configurações do Android TV
# e use o mesmo procedimento do Firestick
adb connect IP_DA_TV:5555
adb install app-debug.apk
```

---

## Arquitetura de comunicação

```
Firestick / Android TV
        │
        │  HTTP/HTTPS + JWT
        ▼
  Backend StreamBR (Node.js)
        │
        │  Proxy transparente
        ▼
  Provedor IPTV (M3U / Xtream)
        │
        │  HLS / MPEG-TS
        ▼
   ExoPlayer (no app)
```

**Nunca há comunicação direta entre o app e o provedor IPTV.**
O backend é o intermediário — esconde credenciais e controla acesso.

---

## Fluxo de autenticação no app

```
1. SplashActivity verifica token salvo (EncryptedSharedPreferences)
2. Se existe → tenta /api/health para ver se servidor responde
   - OK → HomeActivity
   - Falhou → tenta /api/auth/refresh com o refreshToken
     - OK → salva novo accessToken → HomeActivity
     - Falhou → tokenManager.clear() → LoginActivity
3. Se não existe → LoginActivity
4. Login bem-sucedido → salva accessToken + refreshToken → HomeActivity
```

---

## Controles do player (D-pad)

| Botão | Ação |
|---|---|
| OK / Play-Pause | Play / Pause |
| ← Esquerda | Voltar 10s (só VOD) |
| → Direita | Avançar 10s (só VOD) |
| ↑ Cima / ↓ Baixo | Mostra overlay de informações |
| Voltar | Fecha player, volta para home |
| Stop | Fecha player |

---

## Reconexão automática (canais ao vivo)

O player tenta reconectar automaticamente até 5 vezes com intervalo de 3 segundos quando:
- O stream cai (player.STATE_ENDED em canal ao vivo)
- Ocorre erro de rede (PlaybackException)

Após 5 tentativas sem sucesso, exibe mensagem de erro.

---

## Próximos passos

1. **Layouts XML** — criar `activity_login.xml`, `activity_home.xml`, `activity_player.xml`, `card_channel.xml`, `card_vod.xml`
2. **ChannelRepository** — camada de cache com Room para funcionar offline
3. **EPG overlay** — mostrar programação atual no overlay do player
4. **Múltiplas qualidades** — seletor de qualidade (Auto/HD/SD) no player
5. **Favoritos** — salvar canais favoritos no Room
6. **Busca** — SearchFragment do Leanback para busca por voz
7. **Área adulta** — PinActivity com teclado numérico grande para TV
