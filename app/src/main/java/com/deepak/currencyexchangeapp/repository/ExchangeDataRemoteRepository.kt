package com.deepak.currencyexchangeapp.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.deepak.currencyexchangeapp.databse.AppDatabase
import com.deepak.currencyexchangeapp.databse.RateEntity
import com.deepak.currencyexchangeapp.databse.RateEntityDao
import com.deepak.currencyexchangeapp.model.ExchangeDataResp
import com.deepak.currencyexchangeapp.network.OpenExchangeService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

interface ExchangeDataRemoteRepository {
    suspend fun getLatestExchangeData(): Flow<Result<ExchangeDataResp>>
}

class ExchangeDataRemoteRemoteRepositoryImpl @Inject constructor(
    private val exchangeService: OpenExchangeService,
    private val database: AppDatabase
) :
    ExchangeDataRemoteRepository {
    override suspend fun getLatestExchangeData(): Flow<Result<ExchangeDataResp>> {
        return flow {
            val data = try {
                val remoteData = exchangeService.getLatestExchangeRate()
                database.rateEntityDao()
                    .insertAll(remoteData.rates.map { RateEntity(it.key, it.value) })
                Result.success(remoteData)
            } catch (exc: Exception) {
                Result.failure(exc)
            }
            emit(data)
        }.flowOn(Dispatchers.IO)
    }
}

interface ExchangeDataLocalRepository {
    suspend fun insertData(rateData: HashMap<String, Double>)
    suspend fun getRateData(): Flow<List<RateEntity>>
    suspend fun getLastSyncTime(): Long
    suspend fun storeLastSyncTime(time: Long)

}

private val SYNC_TIME = longPreferencesKey("exchange_data_sync_time")

class ExchangeDataLocalRepositoryImpl @Inject constructor(
    private val database: AppDatabase,
    private val appPref: DataStore<Preferences>
) :
    ExchangeDataLocalRepository {
    override suspend fun insertData(rateData: HashMap<String, Double>) {
        database.rateEntityDao()
            .insertAll(rateData.map { RateEntity(symbol = it.key, rate = it.value) })
    }

    override suspend fun getRateData() = database.rateEntityDao().getAll()
    override suspend fun getLastSyncTime(): Long {
        return appPref.data.first()[SYNC_TIME] ?: 0
    }

    override suspend fun storeLastSyncTime(time: Long) {
        appPref.edit {
            it[SYNC_TIME] = time
        }
    }
}