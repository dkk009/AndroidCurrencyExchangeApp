package com.deepak.currencyexchangeapp.usecase

import com.deepak.currencyexchangeapp.model.ExchangeDataResp
import com.deepak.currencyexchangeapp.model.ExchangeUiModel
import com.deepak.currencyexchangeapp.repository.ExchangeDataLocalRepository
import com.deepak.currencyexchangeapp.repository.ExchangeDataRemoteRepository
import com.deepak.currencyexchangeapp.repository.FakeExchangeLocalDataRepository
import com.deepak.currencyexchangeapp.repository.FakeExchangeRemoteDataRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class ExchangeUseCaseTest {

    private val exchangeDataLocalRepository: ExchangeDataLocalRepository =
        FakeExchangeLocalDataRepository()

    private var exchangeDataRemoteRepository: ExchangeDataRemoteRepository =
        FakeExchangeRemoteDataRepository()
    private val useCaseTest: ExchangeDataUseCase = ExchangeDataUseCaseImpl(
        remoteRepository = exchangeDataRemoteRepository,
        localRepository = exchangeDataLocalRepository
    )

    @Test
    fun getExchangeDataObserverTest() = runTest {
        val rateDataMap: HashMap<String, Double> = HashMap<String, Double>().apply {
            put("USD", 1.0)
            put("INR", 82.0)
            put("AED", 3.67)
            put("AFN", 85.14)
        }
        Assert.assertEquals(0, useCaseTest.getExchangeDataObserver().first().size)
        exchangeDataLocalRepository.insertData(rateDataMap)
        Assert.assertEquals(rateDataMap.size, useCaseTest.getExchangeDataObserver().first().size)
    }

    @Test
    fun fetchRemoteDataFailTest() = runTest {
        val data = useCaseTest.fetchRemoteData().first()
        Assert.assertTrue(data.isFailure)
    }

    @Test
    fun fetchRemoteDataSuccess() = runTest {
        val rateDataMap: HashMap<String, Double> = HashMap<String, Double>().apply {
            put("USD", 1.0)
            put("INR", 82.0)
            put("AED", 3.67)
            put("AFN", 85.14)
        }
        val dataResp = ExchangeDataResp(
            disclaimer = "Test Disclaimer",
            timeStamp = System.currentTimeMillis(),
            base = "USD",
            rates = rateDataMap
        )
        (exchangeDataRemoteRepository as FakeExchangeRemoteDataRepository).setExpectedExchangeReps(
            dataResp
        )

        val data = useCaseTest.fetchRemoteData().first()
        Assert.assertTrue(data.isSuccess)
        Assert.assertEquals("USD", data.getOrNull()?.base)
        Assert.assertTrue(data.getOrNull()?.rates?.size == rateDataMap.size)
    }

    @Test
    fun fetchRemoteDataWithinSyncTime() = runTest {
        exchangeDataLocalRepository.storeLastSyncTime(System.currentTimeMillis())
        val data = useCaseTest.fetchRemoteData().first()
        Assert.assertTrue(data.isSuccess)
        Assert.assertTrue(data.getOrNull() == null)
    }

    @Test
    fun fetchRemoteDataExpireSyncTime() = runTest {
        val storeSyncTime = System.currentTimeMillis() - 1000 * 60 * 31
        val rateDataMap: HashMap<String, Double> = HashMap<String, Double>().apply {
            put("USD", 1.0)
            put("INR", 82.0)
            put("AED", 3.67)
            put("AFN", 85.14)
        }
        val dataResp = ExchangeDataResp(
            disclaimer = "Test Disclaimer",
            timeStamp = System.currentTimeMillis(),
            base = "USD",
            rates = rateDataMap
        )
        (exchangeDataRemoteRepository as FakeExchangeRemoteDataRepository).setExpectedExchangeReps(
            dataResp
        )
        exchangeDataLocalRepository.storeLastSyncTime(storeSyncTime)
        val data = useCaseTest.fetchRemoteData().first()
        Assert.assertTrue(data.isSuccess)
        Assert.assertEquals("USD", data.getOrNull()?.base)
        Assert.assertTrue(data.getOrNull()?.rates?.size == rateDataMap.size)
    }

    @Test
    fun calculateExchangeRateTestWithUSD() {
        val rateDataMap: HashMap<String, Double> = HashMap<String, Double>().apply {
            put("USD", 1.0)
            put("INR", 82.0)
            put("AED", 3.67)
            put("AFN", 85.14)
        }
        val rateList = rateDataMap.map {
            ExchangeUiModel(
                symbol = it.key,
                rate = it.value,
                calculatedRate = it.value
            )
        }
        val amount = 10.0
        val selectedCurrency = rateList.find { it.symbol == "USD" }!!
        val dataList = useCaseTest.calculateExchangeRate(
            rateList,
            amount = amount,
            selectedCurrencyRate = selectedCurrency.rate
        )
        val firstItem = dataList.first()
        Assert.assertTrue(firstItem.calculatedRate == firstItem.rate * amount)
    }


    @Test
    fun calculateExchangeRateTestWithINR() {
        val rateDataMap: HashMap<String, Double> = HashMap<String, Double>().apply {
            put("USD", 1.0)
            put("INR", 82.0)
            put("AED", 3.67)
            put("AFN", 85.14)
        }
        val rateList = rateDataMap.map {
            ExchangeUiModel(
                symbol = it.key,
                rate = it.value,
                calculatedRate = it.value
            )
        }
        val amount = 100.0
        val selectedCurrency = rateList.find { it.symbol == "INR" }!!
        val dataList = useCaseTest.calculateExchangeRate(
            rateList,
            amount = amount,
            selectedCurrencyRate = selectedCurrency.rate
        )
        val firstItem = dataList.find { it.symbol == "INR" }!!
        Assert.assertEquals(amount, firstItem.calculatedRate, 0.0)
    }
}



