package com.streambr.tv.data.repository;

/**
 * Repositório orquestrador de canais.
 * Tenta carregar da API, se conseguir atualiza o cache local.
 * Sempre retorna do cache local para garantir funcionamento offline/instantâneo.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bH\u0086@\u00a2\u0006\u0002\u0010\nJ&\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\t0\b2\u0006\u0010\f\u001a\u00020\r2\b\b\u0002\u0010\u000e\u001a\u00020\u000fH\u0086@\u00a2\u0006\u0002\u0010\u0010R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"Lcom/streambr/tv/data/repository/ChannelRepository;", "", "api", "Lcom/streambr/tv/data/api/StreamBrApi;", "dao", "Lcom/streambr/tv/data/local/ChannelDao;", "(Lcom/streambr/tv/data/api/StreamBrApi;Lcom/streambr/tv/data/local/ChannelDao;)V", "getCachedChannels", "", "Lcom/streambr/tv/data/api/Channel;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getChannels", "token", "", "forceRefresh", "", "(Ljava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class ChannelRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.streambr.tv.data.api.StreamBrApi api = null;
    @org.jetbrains.annotations.NotNull()
    private final com.streambr.tv.data.local.ChannelDao dao = null;
    
    public ChannelRepository(@org.jetbrains.annotations.NotNull()
    com.streambr.tv.data.api.StreamBrApi api, @org.jetbrains.annotations.NotNull()
    com.streambr.tv.data.local.ChannelDao dao) {
        super();
    }
    
    /**
     * Retorna lista de canais.
     * @param forceRefresh se true, ignora o cache e tenta API.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getChannels(@org.jetbrains.annotations.NotNull()
    java.lang.String token, boolean forceRefresh, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.streambr.tv.data.api.Channel>> $completion) {
        return null;
    }
    
    /**
     * Busca canais no banco sem tentar API (uso rápido)
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getCachedChannels(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.streambr.tv.data.api.Channel>> $completion) {
        return null;
    }
}