package com.deepak.currencyexchangeapp.network

import com.deepak.currencyexchangeapp.model.ExchangeDataResp
import retrofit2.http.GET

interface OpenExchangeService {
    @GET("latest.json")
    suspend fun getLatestExchangeRate(): ExchangeDataResp
}