package com.deepak.currencyexchangeapp.model

import com.google.gson.annotations.SerializedName

data class ExchangeDataResp(
    val disclaimer: String,
    @SerializedName("timestamp")
    val timeStamp: Long,
    val base: String,
    val rates: HashMap<String, Double>
)