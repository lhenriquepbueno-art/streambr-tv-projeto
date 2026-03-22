# StreamBR — Regras ProGuard
# Mantém nomes de classes usadas pelo Retrofit (serialização JSON)
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.streambr.tv.data.api.** { *; }

# ExoPlayer / Media3
-keep class androidx.media3.** { *; }
-dontwarn androidx.media3.**

# Leanback
-keep class androidx.leanback.** { *; }

# Gson
-keep class com.google.gson.** { *; }
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
