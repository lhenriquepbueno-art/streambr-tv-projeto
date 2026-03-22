package com.streambr.tv.ui

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.streambr.tv.R
import com.streambr.tv.data.api.ApiClient
import com.streambr.tv.data.api.PinRequest
import com.streambr.tv.ui.player.PlayerActivity
import com.streambr.tv.util.TokenManager
import kotlinx.coroutines.launch
import com.streambr.tv.BuildConfig

/**
 * Tela de desbloqueio de conteúdo adulto com PIN de 4 dígitos.
 *
 * Fluxo:
 *   1. Usuário navega com D-pad pelos botões numéricos (0-9)
 *   2. Cada número digitado preenche uma bolinha indicadora
 *   3. Ao completar 4 dígitos, o botão CONFIRMAR fica disponível
 *   4. POST /api/auth/pin → recebe adultToken → salva → abre player
 *
 * Após autenticação bem-sucedida, o adultToken fica salvo por 4 horas
 * (conforme TTL do backend), então o usuário não precisa redigitar o PIN
 * durante esse período.
 */
class PinActivity : FragmentActivity() {

    private lateinit var tokenManager: TokenManager

    // Views dos indicadores de dígito (bolinhas)
    private lateinit var dots: Array<View>

    // PIN digitado
    private val pinDigits = StringBuilder()

    // Dados do canal a abrir após autenticação
    private var streamUrl = ""
    private var streamTitle = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin)

        tokenManager = TokenManager.getInstance(this)

        streamUrl   = intent.getStringExtra(EXTRA_STREAM_URL) ?: ""
        streamTitle = intent.getStringExtra(EXTRA_TITLE) ?: ""

        dots = arrayOf(
            findViewById(R.id.pin_dot_1),
            findViewById(R.id.pin_dot_2),
            findViewById(R.id.pin_dot_3),
            findViewById(R.id.pin_dot_4)
        )

        setupNumpad()

        // Foca no botão "1" ao abrir
        findViewById<Button>(R.id.btn_1).requestFocus()
    }

    // ─── Configura teclado numérico ───────────────────────────────────────────

    private fun setupNumpad() {
        val numButtons = mapOf(
            R.id.btn_0 to '0', R.id.btn_1 to '1', R.id.btn_2 to '2',
            R.id.btn_3 to '3', R.id.btn_4 to '4', R.id.btn_5 to '5',
            R.id.btn_6 to '6', R.id.btn_7 to '7', R.id.btn_8 to '8',
            R.id.btn_9 to '9'
        )

        numButtons.forEach { (id, digit) ->
            val btn = findViewById<Button>(id)
            btn.setOnClickListener { addDigit(digit) }
            btn.setOnKeyListener { _, code, ev ->
                if (ev.action == KeyEvent.ACTION_DOWN &&
                    (code == KeyEvent.KEYCODE_DPAD_CENTER || code == KeyEvent.KEYCODE_ENTER)) {
                    addDigit(digit); true
                } else false
            }
        }

        // Apagar último dígito
        findViewById<Button>(R.id.btn_delete).setOnClickListener { deleteDigit() }

        // Limpar tudo
        findViewById<Button>(R.id.btn_clear).setOnClickListener { clearPin() }

        // Confirmar
        val btnConfirm = findViewById<Button>(R.id.btn_confirm)
        btnConfirm.setOnClickListener { confirmPin() }
        btnConfirm.setOnKeyListener { _, code, ev ->
            if (ev.action == KeyEvent.ACTION_DOWN &&
                (code == KeyEvent.KEYCODE_DPAD_CENTER || code == KeyEvent.KEYCODE_ENTER)) {
                confirmPin(); true
            } else false
        }
    }

    // ─── Lógica do PIN ────────────────────────────────────────────────────────

    private fun addDigit(digit: Char) {
        if (pinDigits.length >= 4) return
        pinDigits.append(digit)
        updateDots()
        hideError()

        // Auto-confirma ao completar 4 dígitos
        if (pinDigits.length == 4) {
            confirmPin()
        }
    }

    private fun deleteDigit() {
        if (pinDigits.isEmpty()) return
        pinDigits.deleteCharAt(pinDigits.length - 1)
        updateDots()
        hideError()
    }

    private fun clearPin() {
        pinDigits.clear()
        updateDots()
        hideError()
    }

    /** Atualiza as bolinhas: preenchida = dígito inserido, vazia = aguardando */
    private fun updateDots() {
        dots.forEachIndexed { i, dot ->
            dot.setBackgroundResource(
                if (i < pinDigits.length) R.drawable.shape_pin_dot_filled
                else R.drawable.shape_pin_dot_empty
            )
        }
    }

    // ─── Verificação via API ──────────────────────────────────────────────────

    private fun confirmPin() {
        val pin = pinDigits.toString()
        if (pin.length < 4) {
            showError("Digite os 4 dígitos do PIN")
            return
        }

        setLoading(true)

        lifecycleScope.launch {
            try {
                val response = ApiClient.getApi(tokenManager.serverUrl ?: BuildConfig.API_BASE_URL).verifyPin(
                    token   = tokenManager.authHeader(),
                    request = PinRequest(pin)
                )

                if (response.isSuccessful) {
                    val adultToken = response.body()?.adultToken ?: ""
                    tokenManager.adultToken = adultToken

                    // Abre o player com a URL do canal adulto
                    if (streamUrl.isNotBlank()) {
                        startActivity(
                            Intent(this@PinActivity, PlayerActivity::class.java).apply {
                                putExtra(PlayerActivity.EXTRA_STREAM_URL,
                                    "$streamUrl&adult_token=$adultToken")
                                putExtra(PlayerActivity.EXTRA_TITLE, streamTitle)
                                putExtra(PlayerActivity.EXTRA_IS_LIVE, true)
                            }
                        )
                    }
                    finish()

                } else {
                    clearPin()
                    showError(getString(R.string.pin_error))
                    // Devolve foco ao btn_1 para redigitar
                    findViewById<Button>(R.id.btn_1).requestFocus()
                }

            } catch (e: Exception) {
                showError("Erro de conexão. Tente novamente.")
            } finally {
                setLoading(false)
            }
        }
    }

    // ─── UI helpers ───────────────────────────────────────────────────────────

    private fun showError(msg: String) {
        val tv = findViewById<TextView>(R.id.tv_pin_error)
        tv.text = msg
        tv.visibility = View.VISIBLE
    }

    private fun hideError() {
        findViewById<TextView>(R.id.tv_pin_error).visibility = View.GONE
    }

    private fun setLoading(on: Boolean) {
        // Desabilita todos os botões durante a chamada de rede
        listOf(R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4,
               R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9,
               R.id.btn_delete, R.id.btn_clear, R.id.btn_confirm)
            .forEach { id -> findViewById<Button>(id).isEnabled = !on }
    }

    companion object {
        const val EXTRA_STREAM_URL = "stream_url"
        const val EXTRA_TITLE      = "title"
        const val EXTRA_CHANNEL_ID = "channel_id"
    }
}
