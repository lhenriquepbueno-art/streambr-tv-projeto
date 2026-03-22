package com.streambr.tv.ui;

/**
 * Tela de desbloqueio de conteúdo adulto com PIN de 4 dígitos.
 *
 * Fluxo:
 *  1. Usuário navega com D-pad pelos botões numéricos (0-9)
 *  2. Cada número digitado preenche uma bolinha indicadora
 *  3. Ao completar 4 dígitos, o botão CONFIRMAR fica disponível
 *  4. POST /api/auth/pin → recebe adultToken → salva → abre player
 *
 * Após autenticação bem-sucedida, o adultToken fica salvo por 4 horas
 * (conforme TTL do backend), então o usuário não precisa redigitar o PIN
 * durante esse período.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\f\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0006\u0018\u0000 !2\u00020\u0001:\u0001!B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0002J\b\u0010\u0013\u001a\u00020\u0010H\u0002J\b\u0010\u0014\u001a\u00020\u0010H\u0002J\b\u0010\u0015\u001a\u00020\u0010H\u0002J\b\u0010\u0016\u001a\u00020\u0010H\u0002J\u0012\u0010\u0017\u001a\u00020\u00102\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0014J\u0010\u0010\u001a\u001a\u00020\u00102\u0006\u0010\u001b\u001a\u00020\u001cH\u0002J\b\u0010\u001d\u001a\u00020\u0010H\u0002J\u0010\u0010\u001e\u001a\u00020\u00102\u0006\u0010\u001f\u001a\u00020\u000bH\u0002J\b\u0010 \u001a\u00020\u0010H\u0002R\u0016\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082.\u00a2\u0006\u0004\n\u0002\u0010\u0006R\u0012\u0010\u0007\u001a\u00060\bj\u0002`\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\""}, d2 = {"Lcom/streambr/tv/ui/PinActivity;", "Landroidx/fragment/app/FragmentActivity;", "()V", "dots", "", "Landroid/view/View;", "[Landroid/view/View;", "pinDigits", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "streamTitle", "", "streamUrl", "tokenManager", "Lcom/streambr/tv/util/TokenManager;", "addDigit", "", "digit", "", "clearPin", "confirmPin", "deleteDigit", "hideError", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "setLoading", "on", "", "setupNumpad", "showError", "msg", "updateDots", "Companion", "app_debug"})
public final class PinActivity extends androidx.fragment.app.FragmentActivity {
    private com.streambr.tv.util.TokenManager tokenManager;
    private android.view.View[] dots;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.StringBuilder pinDigits = null;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String streamUrl = "";
    @org.jetbrains.annotations.NotNull()
    private java.lang.String streamTitle = "";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_STREAM_URL = "stream_url";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_TITLE = "title";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_CHANNEL_ID = "channel_id";
    @org.jetbrains.annotations.NotNull()
    public static final com.streambr.tv.ui.PinActivity.Companion Companion = null;
    
    public PinActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupNumpad() {
    }
    
    private final void addDigit(char digit) {
    }
    
    private final void deleteDigit() {
    }
    
    private final void clearPin() {
    }
    
    /**
     * Atualiza as bolinhas: preenchida = dígito inserido, vazia = aguardando
     */
    private final void updateDots() {
    }
    
    private final void confirmPin() {
    }
    
    private final void showError(java.lang.String msg) {
    }
    
    private final void hideError() {
    }
    
    private final void setLoading(boolean on) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lcom/streambr/tv/ui/PinActivity$Companion;", "", "()V", "EXTRA_CHANNEL_ID", "", "EXTRA_STREAM_URL", "EXTRA_TITLE", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}