package com.deepak.currencyexchangeapp.di

import com.deepak.currencyexchangeapp.network.OpenExchangeService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val BASE_URL = "https://openexchangerates.org/api/"
private const val APP_KEY = "ea9ebd0c0a1f43c0acce6f11ae24d609"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkhttp(authInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient().newBuilder().addInterceptor(authInterceptor).build()
    }

    @Provides
    @Singleton
    fun provideAppKeyInterceptor(): Interceptor {
        return Interceptor { chain ->
            val url = chain.request().url.newBuilder().addQueryParameter("app_id", APP_KEY).build()
            val request = chain.request().newBuilder().url(url).build()
            return@Interceptor chain.proceed(request)
        }
    }

    @Singleton
    @Provides
    fun provideExchangeService(okHttpClient: OkHttpClient): OpenExchangeService {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(OpenExchangeService::class.java)
    }
}