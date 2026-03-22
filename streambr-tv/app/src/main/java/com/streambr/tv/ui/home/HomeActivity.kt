package com.streambr.tv.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import androidx.lifecycle.lifecycleScope
import com.streambr.tv.R
import com.streambr.tv.data.api.ApiClient
import com.streambr.tv.data.api.Channel
import com.streambr.tv.data.api.VodItem
import com.streambr.tv.ui.PinActivity
import com.streambr.tv.ui.player.PlayerActivity
import com.streambr.tv.ui.login.LoginActivity
import com.streambr.tv.data.local.AppDatabase
import com.streambr.tv.data.repository.ChannelRepository
import com.streambr.tv.util.TokenManager
import kotlinx.coroutines.launch
import com.streambr.tv.BuildConfig
import android.view.View

class HomeActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MainBrowseFragment())
                .commit()
        }
    }
}

class MainBrowseFragment : BrowseSupportFragment() {

    private lateinit var tokenManager: TokenManager
    private lateinit var repository: ChannelRepository
    private val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tokenManager = TokenManager.getInstance(requireContext())
        
        // Inicializa o repositório — usa a URL salva pelo usuário ou o fallback do BuildConfig
        val db  = AppDatabase.getDatabase(requireContext())
        val api = ApiClient.getApi(tokenManager.serverUrl ?: BuildConfig.API_BASE_URL)
        repository = ChannelRepository(api, db.channelDao())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        loadContent()
    }

    private fun setupUI() {
        title = "StreamBR"
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true
        adapter = rowsAdapter
        brandColor = requireContext().getColor(R.color.background_surface)
        searchAffordanceColor = requireContext().getColor(R.color.accent_red)

        onItemViewClickedListener = OnItemViewClickedListener { _, item, _, _ ->
            when (item) {
                is Channel -> onChannelClicked(item)
                is VodItem -> onVodClicked(item)
            }
        }
        setOnSearchClickedListener {
            Toast.makeText(context, "Busca em breve", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadContent() {
        viewLifecycleOwner.lifecycleScope.launch {
            try { loadLiveChannels(); loadVod() }
            catch (e: Exception) { showError(e) }
        }
    }

    private suspend fun loadLiveChannels() {
        val channels = repository.getChannels(tokenManager.authHeader(), forceRefresh = true)
        if (channels.isEmpty()) {
            syncChannelsSilently()
            return
        }

        val regular = channels.filter { !it.isAdult }
        val adult   = channels.filter {  it.isAdult }

        val grouped = regular.groupBy { it.group }
        val preferred = listOf("TV Aberta","Esportes","Filmes","Séries","Kids","Notícias","Documentários","Música")
        val sorted = preferred.filter { it in grouped } + grouped.keys.filter { it !in preferred }.sorted()

        sorted.forEach { name ->
            val cards = ArrayObjectAdapter(ChannelCardPresenter())
            grouped[name]?.forEach { cards.add(it) }
            rowsAdapter.add(ListRow(HeaderItem(name), cards))
        }

        if (adult.isNotEmpty()) {
            val cards = ArrayObjectAdapter(ChannelCardPresenter())
            adult.forEach { cards.add(it) }
            rowsAdapter.add(ListRow(HeaderItem("\uD83D\uDD12 Adulto +18"), cards))
        }
    }

    private fun syncChannelsSilently() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val api = ApiClient.getApi(tokenManager.serverUrl ?: BuildConfig.API_BASE_URL)
                val response = api.syncChannels(tokenManager.authHeader(), com.streambr.tv.data.api.SyncRequest())
                if (response.isSuccessful) {
                    Toast.makeText(context, "Canais sincronizados com sucesso!", Toast.LENGTH_SHORT).show()
                    loadContent() // Recarrega agora que tem dados
                }
            } catch (e: Exception) {
                showError(e)
            }
        }
    }

    private suspend fun loadVod() {
        val r = ApiClient.getApi().getVod(tokenManager.authHeader(), limit = 50)
        if (!r.isSuccessful) return
        val items = r.body()?.items?.takeIf { it.isNotEmpty() } ?: return
        val cards = ArrayObjectAdapter(VodCardPresenter())
        items.forEach { cards.add(it) }
        rowsAdapter.add(ListRow(HeaderItem("Filmes e Séries"), cards))
    }

    private fun onChannelClicked(ch: Channel) {
        if (ch.isAdult && tokenManager.adultToken.isNullOrBlank()) {
            startActivity(Intent(requireContext(), PinActivity::class.java).apply {
                putExtra(PinActivity.EXTRA_STREAM_URL, injectToken(ch.hlsUrl))
                putExtra(PinActivity.EXTRA_TITLE, ch.name)
            })
            return
        }
        openPlayer(injectToken(ch.hlsUrl), ch.name, isLive = true)
    }

    private fun onVodClicked(vod: VodItem) {
        openPlayer(injectToken(vod.streamUrl), vod.title, isLive = false)
    }

    private fun openPlayer(url: String, title: String, isLive: Boolean) {
        startActivity(Intent(requireContext(), PlayerActivity::class.java).apply {
            putExtra(PlayerActivity.EXTRA_STREAM_URL, url)
            putExtra(PlayerActivity.EXTRA_TITLE, title)
            putExtra(PlayerActivity.EXTRA_IS_LIVE, isLive)
        })
    }

    private fun injectToken(url: String): String {
        val token = tokenManager.accessToken ?: return url
        return if ("?" in url) "$url&token=$token" else "$url?token=$token"
    }

    private fun showError(e: Exception) {
        val msg = if (e.message?.contains("resolve host") == true) "Sem conexão com o servidor"
                  else "Erro ao carregar conteúdo"
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    private fun logout() {
        tokenManager.clear()
        startActivity(Intent(requireContext(), LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
    }
}
