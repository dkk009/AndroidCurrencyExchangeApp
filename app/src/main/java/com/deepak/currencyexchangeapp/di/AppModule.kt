package com.deepak.currencyexchangeapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.deepak.currencyexchangeapp.databse.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val PREF_NAME = "app_pref"

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideDatabaseInstance(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = AppDatabase::class.java,
            name = "app_db"
        ).build()
    }


}

@InstallIn(SingletonComponent::class)
@Module
object DataStoreModule{
    @Provides
    @Singleton
    fun provideSkrapPref(@ApplicationContext context: Context): DataStore<Preferences> {
        val dataStore = PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile(PREF_NAME)
        }
        return dataStore
    }
}