package com.example.studysynaps.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Gunakan 10.0.2.2 untuk Emulator Android Studio
    // IP Wi-Fi/Hotspot Laptop Kamu (Berdasar ipconfig terakhir)
    private const val BASE_URL = "http://10.78.72.142/rest_api_synaps/index.php/"
    // Jika di HP fisik, gunakan IP Laptop: "http://192.168.1.X/rest_api_synaps/index.php/"

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(ApiService::class.java)
    }
}
