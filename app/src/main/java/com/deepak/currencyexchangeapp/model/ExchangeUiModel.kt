package com.deepak.currencyexchangeapp.model

data class ExchangeUiModel(
    val symbol: String,
    val rate: Double,
    val calculatedRate: Double,
)