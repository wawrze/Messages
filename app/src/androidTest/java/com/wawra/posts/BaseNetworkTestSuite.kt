package com.wawra.posts

import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

abstract class BaseNetworkTestSuite : BaseInstrumentedTestSuite() {

    protected fun createRetrofit(url: HttpUrl): Retrofit = Retrofit.Builder()
        .baseUrl(url)
        .client(OkHttpClient.Builder().build())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

}