package com.deepak.currencyexchangeapp.repository

import com.deepak.currencyexchangeapp.model.ExchangeDataResp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeExchangeRemoteDataRepository : ExchangeDataRemoteRepository {
    private lateinit var exchangeDataResp: ExchangeDataResp
    override suspend fun getLatestExchangeData(): Flow<Result<ExchangeDataResp>> {
        return flow {
            if (::exchangeDataResp.isInitialized) {
                emit(Result.success(exchangeDataResp))
            } else {
                emit(Result.failure(Exception("Data is not ready")))
            }
        }
    }

    fun setExpectedExchangeReps(exchangeDataResp: ExchangeDataResp) {
        this.exchangeDataResp = exchangeDataResp
    }
}