package com.streambr.tv.data.api

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import com.streambr.tv.BuildConfig

// ─── Modelos de dados ─────────────────────────────────────────────────────────

data class LoginRequest(val username: String, val password: String)

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Int,
    val user: UserInfo
)

data class UserInfo(
    val id: String,
    val username: String,
    val email: String,
    val plan: String
)

data class Channel(
    val id: String,
    val name: String,
    val logo: String,
    val group: String,
    val quality: String,
    val tvgId: String,
    val streamUrl: String,   // URL do proxy — nunca a URL real do provedor
    val hlsUrl: String,
    val isAdult: Boolean = false,
    val tvArchive: Boolean = false
)

data class ChannelListResponse(
    val total: Int,
    val page: Int,
    val channels: List<Channel>,
    val groups: List<String>
)

data class VodItem(
    val id: String,
    val title: String,
    val year: Int,
    val genre: List<String>,
    val duration: Int,
    val rating: Float,
    val plot: String,
    val posterUrl: String,
    val quality: String,
    val streamUrl: String
)

data class VodListResponse(
    val total: Int,
    val items: List<VodItem>
)

data class EpgProgram(
    val id: String,
    val channelId: String,
    val title: String,
    val description: String,
    val start: String,
    val end: String,
    val isLive: Boolean
)

data class EpgResponse(
    val current: EpgProgram?,
    val next: EpgProgram?
)

data class PinRequest(val pin: String)

data class PinResponse(
    val adultToken: String,
    val message: String
)

data class RefreshRequest(val refreshToken: String)

data class SyncRequest(
    val m3uUrl: String? = null,
    val xtreamUrl: String? = null,
    val xtreamUser: String? = null,
    val xtreamPass: String? = null
)

// ─── Interface Retrofit ───────────────────────────────────────────────────────

interface StreamBrApi {

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/auth/refresh")
    suspend fun refresh(@Body request: RefreshRequest): Response<LoginResponse>

    @POST("api/auth/pin")
    suspend fun verifyPin(
        @Header("Authorization") token: String,
        @Body request: PinRequest
    ): Response<PinResponse>

    @GET("api/channels")
    suspend fun getChannels(
        @Header("Authorization") token: String,
        @Query("group") group: String? = null,
        @Query("q") query: String? = null,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 100
    ): Response<ChannelListResponse>

    @POST("api/channels/sync")
    suspend fun syncChannels(
        @Header("Authorization") token: String,
        @Body request: SyncRequest
    ): Response<Map<String, Any>>

    @GET("api/vod")
    suspend fun getVod(
        @Header("Authorization") token: String,
        @Query("category") category: String? = null,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 50
    ): Response<VodListResponse>

    @GET("api/epg")
    suspend fun getEpg(
        @Header("Authorization") token: String,
        @Query("channelId") channelId: String
    ): Response<EpgResponse>

    @GET("api/health")
    suspend fun health(): Response<Map<String, Any>>
}

// ─── Factory do cliente HTTP ──────────────────────────────────────────────────

object ApiClient {

    private var retrofit: Retrofit? = null

    fun getApi(baseUrl: String = BuildConfig.API_BASE_URL): StreamBrApi {
        if (retrofit == null || retrofit!!.baseUrl().toString() != baseUrl) {
            retrofit = buildRetrofit(baseUrl)
        }
        return retrofit!!.create(StreamBrApi::class.java)
    }

    private fun buildRetrofit(baseUrl: String): Retrofit {
        val logging = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG)
                HttpLoggingInterceptor.Level.BODY
            else
                HttpLoggingInterceptor.Level.NONE
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            // Timeouts generosos — streams podem demorar para iniciar
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            // Retry automático em falhas de rede
            .retryOnConnectionFailure(true)
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
