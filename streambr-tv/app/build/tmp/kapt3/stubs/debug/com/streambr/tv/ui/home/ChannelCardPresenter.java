package com.streambr.tv.ui.home;

/**
 * Presenter de canal ao vivo para o BrowseFragment.
 *
 * Um Presenter no Leanback é o equivalente a um ViewHolder + Adapter
 * do RecyclerView — ele infla o layout do card, preenche os dados
 * e gerencia o ciclo de vida (bind/unbind).
 *
 * O sistema de foco do D-pad é gerenciado automaticamente pelo
 * BrowseFragment — o Presenter só precisa definir como o card
 * responde visualmente ao ganhar/perder foco (escala + elevação).
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001:\u0001\rB\u0005\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0010\u0010\t\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u000bH\u0016J\u0010\u0010\f\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016\u00a8\u0006\u000e"}, d2 = {"Lcom/streambr/tv/ui/home/ChannelCardPresenter;", "Landroidx/leanback/widget/Presenter;", "()V", "onBindViewHolder", "", "viewHolder", "Landroidx/leanback/widget/Presenter$ViewHolder;", "item", "", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "onUnbindViewHolder", "ViewHolder", "app_debug"})
public final class ChannelCardPresenter extends androidx.leanback.widget.Presenter {
    
    public ChannelCardPresenter() {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public androidx.leanback.widget.Presenter.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    androidx.leanback.widget.Presenter.ViewHolder viewHolder, @org.jetbrains.annotations.NotNull()
    java.lang.Object item) {
    }
    
    @java.lang.Override()
    public void onUnbindViewHolder(@org.jetbrains.annotations.NotNull()
    androidx.leanback.widget.Presenter.ViewHolder viewHolder) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\r\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\fR\u0011\u0010\u000f\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0013\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\f\u00a8\u0006\u0015"}, d2 = {"Lcom/streambr/tv/ui/home/ChannelCardPresenter$ViewHolder;", "Landroidx/leanback/widget/Presenter$ViewHolder;", "view", "Landroid/view/View;", "(Landroid/view/View;)V", "badgeLive", "Landroid/widget/LinearLayout;", "getBadgeLive", "()Landroid/widget/LinearLayout;", "badgeQuality", "Landroid/widget/TextView;", "getBadgeQuality", "()Landroid/widget/TextView;", "group", "getGroup", "logo", "Landroid/widget/ImageView;", "getLogo", "()Landroid/widget/ImageView;", "name", "getName", "app_debug"})
    public static final class ViewHolder extends androidx.leanback.widget.Presenter.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final android.widget.ImageView logo = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.TextView name = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.TextView group = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.TextView badgeQuality = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.LinearLayout badgeLive = null;
        
        public ViewHolder(@org.jetbrains.annotations.NotNull()
        android.view.View view) {
            super(null);
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.ImageView getLogo() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getName() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getGroup() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getBadgeQuality() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.LinearLayout getBadgeLive() {
            return null;
        }
    }
}