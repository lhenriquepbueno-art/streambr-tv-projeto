package com.streambr.tv.data.local

import androidx.room.*

@Dao
interface ChannelDao {

    @Query("SELECT * FROM channels")
    suspend fun getAll(): List<ChannelEntity>

    @Query("SELECT * FROM channels WHERE isAdult = 0")
    suspend fun getRegularChannels(): List<ChannelEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(channels: List<ChannelEntity>)

    @Query("DELETE FROM channels")
    suspend fun deleteAll()

    @Transaction
    suspend fun refreshChannels(channels: List<ChannelEntity>) {
        deleteAll()
        insertAll(channels)
    }
}
