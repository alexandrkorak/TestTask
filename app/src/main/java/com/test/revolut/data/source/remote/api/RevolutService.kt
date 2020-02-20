package com.test.revolut.data.source.remote.api

import com.test.revolut.data.source.ExchangeRatesResponse
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

fun getRevolutService(): RevolutService {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://revolut.duckdns.org")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    return retrofit.create(RevolutService::class.java)
}

interface RevolutService {
    @GET("/latest")
    fun getBaseResponse(@Query("base") base: String): Single<ExchangeRatesResponse>
}