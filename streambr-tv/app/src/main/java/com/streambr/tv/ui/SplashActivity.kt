package com.streambr.tv.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.streambr.tv.data.api.ApiClient
import com.streambr.tv.data.api.RefreshRequest
import com.streambr.tv.ui.home.HomeActivity
import com.streambr.tv.ui.login.LoginActivity
import com.streambr.tv.util.TokenManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.streambr.tv.R
import com.streambr.tv.BuildConfig

/**
 * Tela inicial — exibe logo e decide para onde ir:
 *  - Sem token salvo → LoginActivity
 *  - Token salvo + API ok → HomeActivity
 *  - Token salvo + API falhou → tenta refresh, se falhar → LoginActivity
 *
 * Usa FragmentActivity (não AppCompatActivity) porque Leanback exige isso.
 */
@SuppressLint("CustomSplashScreen")
class SplashActivity : FragmentActivity() {

    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        tokenManager = TokenManager.getInstance(this)

        lifecycleScope.launch {
            delay(1200) // Tempo para exibir o logo
            checkAuthAndNavigate()
        }
    }

    private suspend fun checkAuthAndNavigate() {
        if (!tokenManager.isLoggedIn()) {
            goToLogin()
            return
        }

        // Verifica se o servidor está respondendo
        try {
            val api = ApiClient.getApi(
                tokenManager.serverUrl ?: BuildConfig.API_BASE_URL
            )
            val health = api.health()
            if (health.isSuccessful) {
                goToHome()
            } else {
                tryRefreshToken()
            }
        } catch (e: Exception) {
            // Sem rede — vai direto para home (modo offline com cache)
            goToHome()
        }
    }

    private suspend fun tryRefreshToken() {
        val refreshToken = tokenManager.refreshToken ?: run {
            goToLogin()
            return
        }

        try {
            val api = ApiClient.getApi()
            val response = api.refresh(RefreshRequest(refreshToken))

            if (response.isSuccessful) {
                val body = response.body()!!
                tokenManager.saveLogin(
                    body.accessToken,
                    body.refreshToken,
                    body.user.username
                )
                goToHome()
            } else {
                // Refresh falhou (expirado) — força novo login
                tokenManager.clear()
                goToLogin()
            }
        } catch (e: Exception) {
            goToHome() // Offline — tenta com token antigo
        }
    }

    private fun goToHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private fun goToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
