package com.streambr.tv.data.api;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010$\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001JJ\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\n\b\u0003\u0010\u0007\u001a\u0004\u0018\u00010\u00062\n\b\u0003\u0010\b\u001a\u0004\u0018\u00010\u00062\b\b\u0003\u0010\t\u001a\u00020\n2\b\b\u0003\u0010\u000b\u001a\u00020\nH\u00a7@\u00a2\u0006\u0002\u0010\fJ(\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010\u000f\u001a\u00020\u0006H\u00a7@\u00a2\u0006\u0002\u0010\u0010J>\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\n\b\u0003\u0010\u0013\u001a\u0004\u0018\u00010\u00062\b\b\u0003\u0010\t\u001a\u00020\n2\b\b\u0003\u0010\u000b\u001a\u00020\nH\u00a7@\u00a2\u0006\u0002\u0010\u0014J \u0010\u0015\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00010\u00160\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0017J\u001e\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00190\u00032\b\b\u0001\u0010\u001a\u001a\u00020\u001bH\u00a7@\u00a2\u0006\u0002\u0010\u001cJ\u001e\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00190\u00032\b\b\u0001\u0010\u001a\u001a\u00020\u001eH\u00a7@\u00a2\u0006\u0002\u0010\u001fJ4\u0010 \u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00010\u00160\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010\u001a\u001a\u00020!H\u00a7@\u00a2\u0006\u0002\u0010\"J(\u0010#\u001a\b\u0012\u0004\u0012\u00020$0\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010\u001a\u001a\u00020%H\u00a7@\u00a2\u0006\u0002\u0010&\u00a8\u0006\'"}, d2 = {"Lcom/streambr/tv/data/api/StreamBrApi;", "", "getChannels", "Lretrofit2/Response;", "Lcom/streambr/tv/data/api/ChannelListResponse;", "token", "", "group", "query", "page", "", "limit", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;IILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getEpg", "Lcom/streambr/tv/data/api/EpgResponse;", "channelId", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getVod", "Lcom/streambr/tv/data/api/VodListResponse;", "category", "(Ljava/lang/String;Ljava/lang/String;IILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "health", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "login", "Lcom/streambr/tv/data/api/LoginResponse;", "request", "Lcom/streambr/tv/data/api/LoginRequest;", "(Lcom/streambr/tv/data/api/LoginRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "refresh", "Lcom/streambr/tv/data/api/RefreshRequest;", "(Lcom/streambr/tv/data/api/RefreshRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "syncChannels", "Lcom/streambr/tv/data/api/SyncRequest;", "(Ljava/lang/String;Lcom/streambr/tv/data/api/SyncRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "verifyPin", "Lcom/streambr/tv/data/api/PinResponse;", "Lcom/streambr/tv/data/api/PinRequest;", "(Ljava/lang/String;Lcom/streambr/tv/data/api/PinRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface StreamBrApi {
    
    @retrofit2.http.POST(value = "api/auth/login")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object login(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.streambr.tv.data.api.LoginRequest request, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.streambr.tv.data.api.LoginResponse>> $completion);
    
    @retrofit2.http.POST(value = "api/auth/refresh")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object refresh(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.streambr.tv.data.api.RefreshRequest request, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.streambr.tv.data.api.LoginResponse>> $completion);
    
    @retrofit2.http.POST(value = "api/auth/pin")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object verifyPin(@retrofit2.http.Header(value = "Authorization")
    @org.jetbrains.annotations.NotNull()
    java.lang.String token, @retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.streambr.tv.data.api.PinRequest request, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.streambr.tv.data.api.PinResponse>> $completion);
    
    @retrofit2.http.GET(value = "api/channels")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getChannels(@retrofit2.http.Header(value = "Authorization")
    @org.jetbrains.annotations.NotNull()
    java.lang.String token, @retrofit2.http.Query(value = "group")
    @org.jetbrains.annotations.Nullable()
    java.lang.String group, @retrofit2.http.Query(value = "q")
    @org.jetbrains.annotations.Nullable()
    java.lang.String query, @retrofit2.http.Query(value = "page")
    int page, @retrofit2.http.Query(value = "limit")
    int limit, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.streambr.tv.data.api.ChannelListResponse>> $completion);
    
    @retrofit2.http.POST(value = "api/channels/sync")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object syncChannels(@retrofit2.http.Header(value = "Authorization")
    @org.jetbrains.annotations.NotNull()
    java.lang.String token, @retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.streambr.tv.data.api.SyncRequest request, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<java.util.Map<java.lang.String, java.lang.Object>>> $completion);
    
    @retrofit2.http.GET(value = "api/vod")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getVod(@retrofit2.http.Header(value = "Authorization")
    @org.jetbrains.annotations.NotNull()
    java.lang.String token, @retrofit2.http.Query(value = "category")
    @org.jetbrains.annotations.Nullable()
    java.lang.String category, @retrofit2.http.Query(value = "page")
    int page, @retrofit2.http.Query(value = "limit")
    int limit, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.streambr.tv.data.api.VodListResponse>> $completion);
    
    @retrofit2.http.GET(value = "api/epg")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getEpg(@retrofit2.http.Header(value = "Authorization")
    @org.jetbrains.annotations.NotNull()
    java.lang.String token, @retrofit2.http.Query(value = "channelId")
    @org.jetbrains.annotations.NotNull()
    java.lang.String channelId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.streambr.tv.data.api.EpgResponse>> $completion);
    
    @retrofit2.http.GET(value = "api/health")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object health(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<java.util.Map<java.lang.String, java.lang.Object>>> $completion);
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
    }
}