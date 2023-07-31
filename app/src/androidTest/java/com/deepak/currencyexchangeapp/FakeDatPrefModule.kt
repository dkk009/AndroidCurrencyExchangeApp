package com.deepak.currencyexchangeapp

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.deepak.currencyexchangeapp.di.DataStoreModule
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataStoreModule::class]
)
class FakeDatPrefModule {

    @Provides
    @Singleton
    fun provideSkrapPref(@ApplicationContext context: Context): DataStore<Preferences> {
        val dataStore = PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile("app_pref")
        }
        return dataStore
    }
}