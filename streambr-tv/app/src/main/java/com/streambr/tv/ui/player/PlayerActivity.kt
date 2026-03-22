package com.streambr.tv.ui.player

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.PlayerView
import com.streambr.tv.R
import com.streambr.tv.data.api.ApiClient
import com.streambr.tv.util.TokenManager
import kotlinx.coroutines.launch
import com.streambr.tv.BuildConfig
import java.text.SimpleDateFormat
import java.util.*

@UnstableApi
class PlayerActivity : FragmentActivity() {

    private lateinit var player: ExoPlayer
    private lateinit var playerView: PlayerView
    private lateinit var progressBuffering: ProgressBar
    private lateinit var overlayInfo: LinearLayout
    private lateinit var overlayControls: LinearLayout
    private lateinit var overlayError: LinearLayout
    private lateinit var tvTitle: TextView
    private lateinit var tvClock: TextView
    private lateinit var tvPosition: TextView
    private lateinit var tvDuration: TextView
    private lateinit var seekBar: SeekBar
    private lateinit var containerTime: LinearLayout
    private lateinit var containerEpg: LinearLayout
    private lateinit var tvEpgNow: TextView
    private lateinit var tvEpgNext: TextView
    private lateinit var tvErrorMessage: TextView
    private lateinit var btnRetry: TextView
    private lateinit var tvReconnecting: TextView
    private lateinit var badgeLive: LinearLayout

    private var streamUrl   = ""
    private var streamTitle = ""
    private var isLive      = false
    private var reconnectAttempts = 0
    private val maxReconnects     = 5

    private val uiHandler = Handler(Looper.getMainLooper())
    private val hideOverlay = Runnable { hideOverlays() }
    private val clockTick = object : Runnable {
        override fun run() {
            tvClock.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            if (!isLive && ::player.isInitialized && player.duration > 0) {
                seekBar.max      = player.duration.toInt()
                seekBar.progress = player.currentPosition.toInt()
                tvPosition.text  = fmtMs(player.currentPosition)
                tvDuration.text  = fmtMs(player.duration)
            }
            uiHandler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        setContentView(R.layout.activity_player)

        streamUrl   = intent.getStringExtra(EXTRA_STREAM_URL) ?: run { finish(); return }
        streamTitle = intent.getStringExtra(EXTRA_TITLE) ?: ""
        isLive      = intent.getBooleanExtra(EXTRA_IS_LIVE, false)

        bindViews()
        setupPlayer()
        loadEpg()
        uiHandler.post(clockTick)
    }

    private fun bindViews() {
        playerView        = findViewById(R.id.player_view)
        progressBuffering = findViewById(R.id.progress_buffering)
        overlayInfo       = findViewById(R.id.overlay_info)
        overlayControls   = findViewById(R.id.overlay_controls)
        overlayError      = findViewById(R.id.overlay_error)
        tvTitle           = findViewById(R.id.tv_player_title)
        tvClock           = findViewById(R.id.tv_clock)
        tvPosition        = findViewById(R.id.tv_position)
        tvDuration        = findViewById(R.id.tv_duration)
        seekBar           = findViewById(R.id.seek_bar)
        containerTime     = findViewById(R.id.container_time)
        containerEpg      = findViewById(R.id.container_epg)
        tvEpgNow          = findViewById(R.id.tv_epg_now)
        tvEpgNext         = findViewById(R.id.tv_epg_next)
        tvErrorMessage    = findViewById(R.id.tv_error_message)
        btnRetry          = findViewById(R.id.btn_retry)
        tvReconnecting    = findViewById(R.id.tv_reconnecting)
        badgeLive         = findViewById(R.id.badge_live)

        tvTitle.text = streamTitle

        badgeLive.visibility     = if (isLive) View.VISIBLE else View.GONE
        seekBar.visibility       = if (isLive) View.GONE    else View.VISIBLE
        containerTime.visibility = if (isLive) View.GONE    else View.VISIBLE
        containerEpg.visibility  = if (isLive) View.VISIBLE else View.GONE

        btnRetry.setOnClickListener { retryStream() }
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar, p: Int, fromUser: Boolean) {
                if (fromUser) player.seekTo(p.toLong())
            }
            override fun onStartTrackingTouch(sb: SeekBar) {}
            override fun onStopTrackingTouch(sb: SeekBar) {}
        })
    }

    private fun setupPlayer() {
        // User-Agent de IPTV para evitar bloqueios do servidor
        val userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Safari/537.36"
        
        val httpFactory = DefaultHttpDataSource.Factory()
            .setUserAgent(userAgent)
            .setConnectTimeoutMs(15_000)
            .setReadTimeoutMs(30_000)
            .setAllowCrossProtocolRedirects(true)

        // Factory de MediaSource para auto-detecção (.ts, .m3u8, etc)
        val mediaSourceFactory = androidx.media3.exoplayer.source.DefaultMediaSourceFactory(httpFactory)

        // Configuração de Áudio para garantir que o som saia corretamente
        val audioAttributes = androidx.media3.common.AudioAttributes.Builder()
            .setUsage(androidx.media3.common.C.USAGE_MEDIA)
            .setContentType(androidx.media3.common.C.AUDIO_CONTENT_TYPE_MOVIE)
            .build()

        // Factory de Renderers configurada para suportar codecs variados (AC3, etc)
        val renderersFactory = androidx.media3.exoplayer.DefaultRenderersFactory(this)
            .setExtensionRendererMode(androidx.media3.exoplayer.DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER)

        player = ExoPlayer.Builder(this)
            .setRenderersFactory(renderersFactory)
            .setMediaSourceFactory(mediaSourceFactory)
            .setAudioAttributes(audioAttributes, true)
            .setHandleAudioBecomingNoisy(true)
            .build()
        
        // Força a decodificação de áudio principal
        player.trackSelectionParameters = player.trackSelectionParameters.buildUpon()
            .build()

        player.volume = 1.0f
        playerView.player = player

        val mediaItem = MediaItem.fromUri(Uri.parse(streamUrl))
        
        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(s: Int) {
                when (s) {
                    Player.STATE_BUFFERING -> { progressBuffering.visibility = View.VISIBLE; tvReconnecting.visibility = View.GONE }
                    Player.STATE_READY     -> { progressBuffering.visibility = View.GONE; tvReconnecting.visibility = View.GONE; overlayError.visibility = View.GONE; reconnectAttempts = 0 }
                    Player.STATE_ENDED     -> { if (isLive) scheduleReconnect() else finish() }
                    else -> {}
                }
            }
            override fun onPlayerError(e: PlaybackException) {
                progressBuffering.visibility = View.GONE
                if (isLive && reconnectAttempts < maxReconnects) scheduleReconnect()
                else showError(e.message ?: "Erro de reprodução")
            }
        })

        player.setMediaItem(MediaItem.fromUri(Uri.parse(streamUrl)))
        player.prepare()
        player.playWhenReady = true
        if (isLive) player.seekToDefaultPosition()
    }

    private fun loadEpg() {
        if (!isLive) return
        val id = streamUrl.substringAfter("/stream/").substringBefore("?").substringBefore("&")
        if (id.isBlank()) return
        lifecycleScope.launch {
            try {
                val tk = TokenManager.getInstance(this@PlayerActivity)
                val r  = ApiClient.getApi(tk.serverUrl ?: BuildConfig.API_BASE_URL).getEpg(tk.authHeader(), id)
                if (r.isSuccessful) {
                    r.body()?.current?.let { tvEpgNow.text = it.title }
                    r.body()?.next?.let    { tvEpgNext.text = it.title }
                }
            } catch (_: Exception) {}
        }
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.action != KeyEvent.ACTION_DOWN) return super.dispatchKeyEvent(event)
        return when (event.keyCode) {
            KeyEvent.KEYCODE_DPAD_CENTER,
            KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE -> { if (player.isPlaying) player.pause() else player.play(); showOverlays(); true }
            KeyEvent.KEYCODE_MEDIA_PLAY       -> { player.play(); showOverlays(); true }
            KeyEvent.KEYCODE_MEDIA_PAUSE      -> { player.pause(); showOverlays(); true }
            KeyEvent.KEYCODE_MEDIA_STOP,
            KeyEvent.KEYCODE_BACK             -> { finish(); true }
            KeyEvent.KEYCODE_DPAD_LEFT  -> { if (!isLive) player.seekTo(maxOf(0L, player.currentPosition - 10_000L)); showOverlays(); true }
            KeyEvent.KEYCODE_DPAD_RIGHT -> { if (!isLive) player.seekTo(player.currentPosition + 10_000L); showOverlays(); true }
            KeyEvent.KEYCODE_DPAD_UP,
            KeyEvent.KEYCODE_DPAD_DOWN  -> { showOverlays(); true }
            else -> super.dispatchKeyEvent(event)
        }
    }

    private fun showOverlays() {
        overlayInfo.visibility = View.VISIBLE; overlayControls.visibility = View.VISIBLE
        uiHandler.removeCallbacks(hideOverlay); uiHandler.postDelayed(hideOverlay, 4000)
    }
    private fun hideOverlays() { overlayInfo.visibility = View.INVISIBLE; overlayControls.visibility = View.INVISIBLE }
    private fun showError(msg: String) { overlayError.visibility = View.VISIBLE; tvErrorMessage.text = msg; progressBuffering.visibility = View.GONE }
    private fun retryStream() { overlayError.visibility = View.GONE; reconnectAttempts = 0; player.prepare(); player.playWhenReady = true }
    private fun scheduleReconnect() {
        reconnectAttempts++
        tvReconnecting.visibility = View.VISIBLE
        tvReconnecting.text = "Reconectando ($reconnectAttempts/$maxReconnects)…"
        uiHandler.postDelayed({ if (!isDestroyed) { player.prepare(); player.playWhenReady = true } }, 3_000L)
    }
    private fun fmtMs(ms: Long): String {
        val s = ms / 1000; val h = s / 3600; val m = (s % 3600) / 60; val sc = s % 60
        return if (h > 0) "%d:%02d:%02d".format(h, m, sc) else "%02d:%02d".format(m, sc)
    }

    override fun onStop()    { super.onStop(); player.pause() }
    override fun onDestroy() { super.onDestroy(); uiHandler.removeCallbacksAndMessages(null); player.release() }

    companion object {
        const val EXTRA_STREAM_URL = "stream_url"
        const val EXTRA_TITLE      = "title"
        const val EXTRA_IS_LIVE    = "is_live"
    }
}
