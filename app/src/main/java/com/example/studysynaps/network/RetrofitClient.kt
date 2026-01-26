package com.example.studysynaps.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Gunakan 10.0.2.2 untuk Emulator Android Studio
    // IP Wi-Fi/Hotspot Laptop Kamu (Berdasar ipconfig terakhir)
    //const val BASE_URL = "http://10.78.72.142/rest_api_synaps/index.php/" //ip hotspot hp
    //const val BASE_URL = "http://10.0.2.2/rest_api_synaps/index.php/" //ip emulator
    //const val BASE_URL = "http://192.168.18.26/rest_api_synaps/index.php/" //ip kost
    //const val BASE_URL = "http://192.168.18.26/rest_api_synaps/index.php/" //ip kampus
    //const val BASE_URL = "http://192.168.56.1/rest_api_synaps/index.php/" //ip laptop
    const val BASE_URL = "http://192.168.1.10/rest_api_synaps/index.php/"

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(ApiService::class.java)
    }
}
