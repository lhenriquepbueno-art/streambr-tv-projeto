package com.streambr.tv.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.streambr.tv.R
import com.streambr.tv.data.api.ApiClient
import com.streambr.tv.data.api.LoginRequest
import com.streambr.tv.ui.home.HomeActivity
import com.streambr.tv.util.TokenManager
import kotlinx.coroutines.launch

class LoginActivity : FragmentActivity() {

    private lateinit var tokenManager: TokenManager
    private lateinit var etServerUrl: EditText
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var progressLogin: ProgressBar
    private lateinit var tvError: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        tokenManager = TokenManager.getInstance(this)

        etServerUrl   = findViewById(R.id.et_server_url)
        etUsername    = findViewById(R.id.et_username)
        etPassword    = findViewById(R.id.et_password)
        btnLogin      = findViewById(R.id.btn_login)
        progressLogin = findViewById(R.id.progress_login)
        tvError       = findViewById(R.id.tv_error)

        etServerUrl.setText(tokenManager.serverUrl ?: "http://192.168.1.100:3000")
        etUsername.setText(tokenManager.username ?: "")

        btnLogin.setOnClickListener { doLogin() }
        btnLogin.setOnKeyListener { _, code, ev ->
            if (ev.action == KeyEvent.ACTION_DOWN &&
                (code == KeyEvent.KEYCODE_DPAD_CENTER || code == KeyEvent.KEYCODE_ENTER)) {
                doLogin(); true
            } else false
        }
        etPassword.setOnKeyListener { _, code, ev ->
            if (ev.action == KeyEvent.ACTION_DOWN && code == KeyEvent.KEYCODE_ENTER) {
                doLogin(); true
            } else false
        }

        when {
            etServerUrl.text.isBlank() -> etServerUrl.requestFocus()
            etUsername.text.isBlank()  -> etUsername.requestFocus()
            else                       -> etPassword.requestFocus()
        }
    }

    private fun doLogin() {
        val serverUrl = etServerUrl.text.toString().trim().trimEnd('/')
        val username  = etUsername.text.toString().trim()
        val password  = etPassword.text.toString()

        if (serverUrl.isEmpty() || username.isEmpty() || password.isEmpty()) {
            showError(getString(R.string.error_empty_fields)); return
        }
        if (!serverUrl.startsWith("http")) {
            showError("URL deve começar com http:// ou https://"); return
        }

        setLoading(true); hideError()

        lifecycleScope.launch {
            try {
                tokenManager.serverUrl = "$serverUrl/"
                val response = ApiClient.getApi("$serverUrl/").login(LoginRequest(username, password))

                if (response.isSuccessful) {
                    val body = response.body()!!
                    tokenManager.saveLogin(body.accessToken, body.refreshToken, body.user.username)
                    startActivity(Intent(this@LoginActivity, HomeActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    })
                } else {
                    showError(when (response.code()) {
                        401  -> getString(R.string.error_invalid_credentials)
                        404  -> "Servidor não encontrado. Verifique a URL."
                        else -> "Erro ${response.code()}"
                    })
                }
            } catch (e: Exception) {
                showError(when {
                    e.message?.contains("resolve host") == true -> "Não foi possível conectar.\nVerifique o IP e o Wi-Fi."
                    e.message?.contains("refused") == true      -> "Conexão recusada. Servidor está rodando?"
                    e.message?.contains("timeout") == true      -> "Timeout. Servidor demorou para responder."
                    else -> getString(R.string.error_server_unreachable)
                })
            } finally { setLoading(false) }
        }
    }

    private fun setLoading(on: Boolean) {
        btnLogin.isEnabled       = !on
        btnLogin.text            = if (on) "" else getString(R.string.btn_login)
        progressLogin.visibility = if (on) View.VISIBLE else View.GONE
        listOf(etServerUrl, etUsername, etPassword).forEach { it.isEnabled = !on }
    }

    private fun showError(msg: String) { tvError.text = msg; tvError.visibility = View.VISIBLE }
    private fun hideError()            { tvError.visibility = View.GONE }
}
