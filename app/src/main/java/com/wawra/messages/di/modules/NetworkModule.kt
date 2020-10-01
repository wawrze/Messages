package com.wawra.messages.di.modules

import com.wawra.messages.di.scopes.AppScoped
import com.wawra.messages.network.ApiInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
class NetworkModule {

    companion object {
        const val BASE_URL = "https://api.url"
    }

    @AppScoped
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    @AppScoped
    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .readTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .addInterceptor {
            val newRequest = it.request().newBuilder()
                .addHeader("header_name", "header_value")
                .method(it.request().method(), it.request().body())
                .build()
            it.proceed(newRequest)
        }
        .build()

    @AppScoped
    @Provides
    fun provideApiInterface(retrofit: Retrofit): ApiInterface = retrofit
        .create(ApiInterface::class.java)

}