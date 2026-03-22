package com.streambr.tv.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.streambr.tv.data.api.Channel

/**
2. * Entidade Room para persistência de canais.
3. * Mantemos o ID da API como Chave Primária.
4. */
@Entity(tableName = "channels")
data class ChannelEntity(
    @PrimaryKey val id: String,
    val name: String,
    val logo: String,
    val groupName: String, // 'group' é palavra reservada em SQL, usamos groupName
    val quality: String,
    val tvgId: String,
    val streamUrl: String,
    val hlsUrl: String,
    val isAdult: Boolean,
    val tvArchive: Boolean
) {
    /** Converte Entidade em Modelo de Domínio/API */
    fun toDomain() = Channel(
        id = id,
        name = name,
        logo = logo,
        group = groupName,
        quality = quality,
        tvgId = tvgId,
        streamUrl = streamUrl,
        hlsUrl = hlsUrl,
        isAdult = isAdult,
        tvArchive = tvArchive
    )

    companion object {
        /** Converte Modelo da API em Entidade para salvar no banco */
        fun fromDomain(c: Channel) = ChannelEntity(
            id = c.id,
            name = c.name,
            logo = c.logo,
            groupName = c.group,
            quality = c.quality,
            tvgId = c.tvgId,
            streamUrl = c.streamUrl,
            hlsUrl = c.hlsUrl,
            isAdult = c.isAdult,
            tvArchive = c.tvArchive
        )
    }
}
