package com.deepak.currencyexchangeapp.usecase

import com.deepak.currencyexchangeapp.model.ExchangeDataResp
import com.deepak.currencyexchangeapp.model.ExchangeUiModel
import com.deepak.currencyexchangeapp.repository.ExchangeDataLocalRepository
import com.deepak.currencyexchangeapp.repository.ExchangeDataRemoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import javax.inject.Inject

private const val SYNC_DURATION = 1000 * 60 * 30

interface ExchangeDataUseCase {
    suspend fun getExchangeDataObserver(): Flow<List<ExchangeUiModel>>
    suspend fun fetchRemoteData(): Flow<Result<ExchangeDataResp?>>
    fun calculateExchangeRate(
        list: List<ExchangeUiModel>,
        amount: Double,
        selectedCurrencyRate: Double
    ): List<ExchangeUiModel>
}

/**
 * ExchangeData use case impl, for handling all currency exchange related operation. Proving three
 * operation, dataObserver, fetching data from remote sever, calculate the currency coversion
 *@param remoteRepository: For handling remote operation
 *@param localRepository: For handling data operation locally like Database
 */
class ExchangeDataUseCaseImpl @Inject constructor(
    private val remoteRepository: ExchangeDataRemoteRepository,
    private val localRepository: ExchangeDataLocalRepository
) : ExchangeDataUseCase {
    /**
     * getExchangeDataObserver fetching data from databse and convert entity model to ui model,
     */
    override suspend fun getExchangeDataObserver() =
        localRepository.getRateData().map {
            it.sortedBy { it.symbol }.map {
                ExchangeUiModel(
                    symbol = it.symbol,
                    rate = it.rate,
                    calculatedRate = it.rate,
                )
            }
        }

    /**
     * Fetch data from remote server based on last sync time
     */
    override suspend fun fetchRemoteData(): Flow<Result<ExchangeDataResp?>> {
        val timeDelta = System.currentTimeMillis() - localRepository.getLastSyncTime()
        return if (timeDelta > SYNC_DURATION) {
            remoteRepository.getLatestExchangeData()
        } else {
            flow {
                emit(Result.success(null))
            }
        }
    }

    /**
     * Calculate currency exchange rate based on user demand. Here user entered amount converting
     * to dollar value and proportionally converting list value to selected currency
     * @param list: Currency list
     * @param amount: User specified amount.
     * @param selectedCurrencyRate: User selected currency rate based on dollar value
     */
    override fun calculateExchangeRate(
        list: List<ExchangeUiModel>,
        amount: Double,
        selectedCurrencyRate: Double
    ): List<ExchangeUiModel> {
        val dollarValue = amount / selectedCurrencyRate
        val dataList = list.map {
            ExchangeUiModel(
                symbol = it.symbol,
                rate = it.rate,
                calculatedRate = it.rate * dollarValue,
            )
        }
        return dataList
    }
}