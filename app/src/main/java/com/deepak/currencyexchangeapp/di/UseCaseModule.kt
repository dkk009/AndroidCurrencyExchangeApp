package com.deepak.currencyexchangeapp.di

import com.deepak.currencyexchangeapp.usecase.ExchangeDataUseCase
import com.deepak.currencyexchangeapp.usecase.ExchangeDataUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class UseCaseModule {
    @Binds
    abstract fun bindExchangeUse(exchangeDataUseCaseImpl: ExchangeDataUseCaseImpl): ExchangeDataUseCase
}