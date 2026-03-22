package com.streambr.tv.util

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/**
 * Gerencia tokens JWT com armazenamento criptografado.
 *
 * Usa EncryptedSharedPreferences (AES-256) — os tokens ficam seguros
 * mesmo se alguém extrair o APK ou fazer backup do dispositivo.
 *
 * Em Android TV / Firestick não há biometria, então a criptografia
 * por chave de hardware é o nível adequado de segurança.
 */
class TokenManager(context: Context) {

    private val prefs: SharedPreferences by lazy {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences.create(
            context,
            "streambr_secure_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    // ─── Access Token ─────────────────────────────────────────────────────

    var accessToken: String?
        get() = prefs.getString(KEY_ACCESS_TOKEN, null)
        set(value) = prefs.edit().putString(KEY_ACCESS_TOKEN, value).apply()

    // ─── Refresh Token ────────────────────────────────────────────────────

    var refreshToken: String?
        get() = prefs.getString(KEY_REFRESH_TOKEN, null)
        set(value) = prefs.edit().putString(KEY_REFRESH_TOKEN, value).apply()

    // ─── Adult Token (PIN) ────────────────────────────────────────────────

    var adultToken: String?
        get() = prefs.getString(KEY_ADULT_TOKEN, null)
        set(value) = prefs.edit().putString(KEY_ADULT_TOKEN, value).apply()

    // ─── Configurações do servidor ─────────────────────────────────────────

    var serverUrl: String?
        get() = prefs.getString(KEY_SERVER_URL, null)
        set(value) = prefs.edit().putString(KEY_SERVER_URL, value).apply()

    var username: String?
        get() = prefs.getString(KEY_USERNAME, null)
        set(value) = prefs.edit().putString(KEY_USERNAME, value).apply()

    // ─── Helpers ──────────────────────────────────────────────────────────

    /** Retorna o header "Bearer TOKEN" pronto para usar no Retrofit */
    fun authHeader(): String = "Bearer ${accessToken ?: ""}"

    /** Retorna true se há um access token salvo (não verifica expiração) */
    fun isLoggedIn(): Boolean = !accessToken.isNullOrBlank()

    /** Limpa tudo — usado no logout */
    fun clear() {
        prefs.edit().clear().apply()
    }

    /** Salva tudo de uma vez após login bem-sucedido */
    fun saveLogin(accessToken: String, refreshToken: String, username: String) {
        prefs.edit()
            .putString(KEY_ACCESS_TOKEN, accessToken)
            .putString(KEY_REFRESH_TOKEN, refreshToken)
            .putString(KEY_USERNAME, username)
            .apply()
    }

    companion object {
        private const val KEY_ACCESS_TOKEN  = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_ADULT_TOKEN   = "adult_token"
        private const val KEY_SERVER_URL    = "server_url"
        private const val KEY_USERNAME      = "username"

        @Volatile
        private var instance: TokenManager? = null

        fun getInstance(context: Context): TokenManager =
            instance ?: synchronized(this) {
                instance ?: TokenManager(context.applicationContext).also { instance = it }
            }
    }
}
