package com.rarms.stocks.performance.data.network

import android.content.Context
import com.rarms.stocks.performance.util.SkipNetworkInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

fun getNetworkService(context: Context): Network {
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(SkipNetworkInterceptor(context = context))
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("http://localhost/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    return retrofit.create(Network::class.java)
}

interface Network {
    @GET("responseQuotesWeek.json")
    suspend fun fetchWeekData(): NetworkResponse.Success

    @GET("responseQuotesMonth.json")
    suspend fun fetchMonthData(): NetworkResponse.Success
}


