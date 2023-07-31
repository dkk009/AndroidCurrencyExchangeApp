package com.deepak.currencyexchangeapp.di

import com.deepak.currencyexchangeapp.repository.ExchangeDataLocalRepository
import com.deepak.currencyexchangeapp.repository.ExchangeDataLocalRepositoryImpl
import com.deepak.currencyexchangeapp.repository.ExchangeDataRemoteRepository
import com.deepak.currencyexchangeapp.repository.ExchangeDataRemoteRemoteRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindExchangeDataRepository(exchangeDataRepository: ExchangeDataRemoteRemoteRepositoryImpl): ExchangeDataRemoteRepository

    @Binds
    abstract fun bindExchangeLocalDataRepository(exchangeDataLocalRepository: ExchangeDataLocalRepositoryImpl): ExchangeDataLocalRepository
}