package com.streambr.tv.ui;

/**
 * Tela inicial — exibe logo e decide para onde ir:
 * - Sem token salvo → LoginActivity
 * - Token salvo + API ok → HomeActivity
 * - Token salvo + API falhou → tenta refresh, se falhar → LoginActivity
 *
 * Usa FragmentActivity (não AppCompatActivity) porque Leanback exige isso.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u0006H\u0082@\u00a2\u0006\u0002\u0010\u0007J\b\u0010\b\u001a\u00020\u0006H\u0002J\b\u0010\t\u001a\u00020\u0006H\u0002J\u0012\u0010\n\u001a\u00020\u00062\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u0014J\u000e\u0010\r\u001a\u00020\u0006H\u0082@\u00a2\u0006\u0002\u0010\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2 = {"Lcom/streambr/tv/ui/SplashActivity;", "Landroidx/fragment/app/FragmentActivity;", "()V", "tokenManager", "Lcom/streambr/tv/util/TokenManager;", "checkAuthAndNavigate", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "goToHome", "goToLogin", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "tryRefreshToken", "app_debug"})
@android.annotation.SuppressLint(value = {"CustomSplashScreen"})
public final class SplashActivity extends androidx.fragment.app.FragmentActivity {
    private com.streambr.tv.util.TokenManager tokenManager;
    
    public SplashActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final java.lang.Object checkAuthAndNavigate(kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.Object tryRefreshToken(kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final void goToHome() {
    }
    
    private final void goToLogin() {
    }
}