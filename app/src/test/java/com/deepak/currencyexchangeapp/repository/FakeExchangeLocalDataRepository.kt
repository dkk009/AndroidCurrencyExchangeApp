package com.deepak.currencyexchangeapp.repository

import com.deepak.currencyexchangeapp.databse.RateEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeExchangeLocalDataRepository : ExchangeDataLocalRepository {
    private val rateEntityMap = mutableMapOf<String, Double>()
    private var lastSyncTime = 0L
    override suspend fun insertData(rateData: HashMap<String, Double>) {
        rateEntityMap.putAll(rateData)
    }

    override suspend fun getRateData(): Flow<List<RateEntity>> {
        return flow {
            emit(rateEntityMap.map { RateEntity(it.key, it.value) })
        }
    }

    override suspend fun getLastSyncTime(): Long {
        return lastSyncTime
    }

    override suspend fun storeLastSyncTime(time: Long) {
        lastSyncTime = time
    }
}