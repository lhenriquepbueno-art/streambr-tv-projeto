package com.streambr.tv.ui.home;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\nH\u0002J\b\u0010\f\u001a\u00020\rH\u0002J\u000e\u0010\u000e\u001a\u00020\rH\u0082@\u00a2\u0006\u0002\u0010\u000fJ\u000e\u0010\u0010\u001a\u00020\rH\u0082@\u00a2\u0006\u0002\u0010\u000fJ\b\u0010\u0011\u001a\u00020\rH\u0002J\u0010\u0010\u0012\u001a\u00020\r2\u0006\u0010\u0013\u001a\u00020\u0014H\u0002J\u0012\u0010\u0015\u001a\u00020\r2\b\u0010\u0016\u001a\u0004\u0018\u00010\u0017H\u0016J\u001a\u0010\u0018\u001a\u00020\r2\u0006\u0010\u0019\u001a\u00020\u001a2\b\u0010\u0016\u001a\u0004\u0018\u00010\u0017H\u0016J\u0010\u0010\u001b\u001a\u00020\r2\u0006\u0010\u001c\u001a\u00020\u001dH\u0002J \u0010\u001e\u001a\u00020\r2\u0006\u0010\u000b\u001a\u00020\n2\u0006\u0010\u001f\u001a\u00020\n2\u0006\u0010 \u001a\u00020!H\u0002J\b\u0010\"\u001a\u00020\rH\u0002J\u0014\u0010#\u001a\u00020\r2\n\u0010$\u001a\u00060%j\u0002`&H\u0002J\b\u0010\'\u001a\u00020\rH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006("}, d2 = {"Lcom/streambr/tv/ui/home/MainBrowseFragment;", "Landroidx/leanback/app/BrowseSupportFragment;", "()V", "repository", "Lcom/streambr/tv/data/repository/ChannelRepository;", "rowsAdapter", "Landroidx/leanback/widget/ArrayObjectAdapter;", "tokenManager", "Lcom/streambr/tv/util/TokenManager;", "injectToken", "", "url", "loadContent", "", "loadLiveChannels", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "loadVod", "logout", "onChannelClicked", "ch", "Lcom/streambr/tv/data/api/Channel;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onViewCreated", "view", "Landroid/view/View;", "onVodClicked", "vod", "Lcom/streambr/tv/data/api/VodItem;", "openPlayer", "title", "isLive", "", "setupUI", "showError", "e", "Ljava/lang/Exception;", "Lkotlin/Exception;", "syncChannelsSilently", "app_debug"})
public final class MainBrowseFragment extends androidx.leanback.app.BrowseSupportFragment {
    private com.streambr.tv.util.TokenManager tokenManager;
    private com.streambr.tv.data.repository.ChannelRepository repository;
    @org.jetbrains.annotations.NotNull()
    private final androidx.leanback.widget.ArrayObjectAdapter rowsAdapter = null;
    
    public MainBrowseFragment() {
        super();
    }
    
    @java.lang.Override()
    public void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupUI() {
    }
    
    private final void loadContent() {
    }
    
    private final java.lang.Object loadLiveChannels(kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final void syncChannelsSilently() {
    }
    
    private final java.lang.Object loadVod(kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final void onChannelClicked(com.streambr.tv.data.api.Channel ch) {
    }
    
    private final void onVodClicked(com.streambr.tv.data.api.VodItem vod) {
    }
    
    private final void openPlayer(java.lang.String url, java.lang.String title, boolean isLive) {
    }
    
    private final java.lang.String injectToken(java.lang.String url) {
        return null;
    }
    
    private final void showError(java.lang.Exception e) {
    }
    
    private final void logout() {
    }
}