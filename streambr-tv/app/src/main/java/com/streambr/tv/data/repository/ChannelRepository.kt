package com.streambr.tv.data.repository

import com.streambr.tv.data.api.Channel
import com.streambr.tv.data.api.StreamBrApi
import com.streambr.tv.data.local.ChannelDao
import com.streambr.tv.data.local.ChannelEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repositório orquestrador de canais.
 * Tenta carregar da API, se conseguir atualiza o cache local.
 * Sempre retorna do cache local para garantir funcionamento offline/instantâneo.
 */
class ChannelRepository(
    private val api: StreamBrApi,
    private val dao: ChannelDao
) {

    /**
     * Retorna lista de canais.
     * @param forceRefresh se true, ignora o cache e tenta API.
     */
    suspend fun getChannels(token: String, forceRefresh: Boolean = false): List<Channel> = withContext(Dispatchers.IO) {
        if (forceRefresh) {
            try {
                val response = api.getChannels(token, limit = 500)
                if (response.isSuccessful) {
                    val channels = response.body()?.channels ?: emptyList()
                    // Atualiza o banco com a nova lista (deleta antigos e insere novos)
                    dao.refreshChannels(channels.map { ChannelEntity.fromDomain(it) })
                }
            } catch (e: Exception) {
                // Em caso de erro na API, apenas logamos; o app continuará usando o cache
                e.printStackTrace()
            }
        }

        // Retorna tudo o que está no banco (convertido para o modelo de domínio)
        dao.getAll().map { it.toDomain() }
    }

    /** Busca canais no banco sem tentar API (uso rápido) */
    suspend fun getCachedChannels(): List<Channel> = withContext(Dispatchers.IO) {
        dao.getAll().map { it.toDomain() }
    }
}
