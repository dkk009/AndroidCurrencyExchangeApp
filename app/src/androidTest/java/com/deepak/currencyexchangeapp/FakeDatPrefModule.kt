package com.deepak.currencyexchangeapp

import com.deepak.currencyexchangeapp.di.DataStoreModule
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataStoreModule::class]
)
class FakeDatPrefModule {
}