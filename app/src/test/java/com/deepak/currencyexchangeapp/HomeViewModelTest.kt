package com.deepak.currencyexchangeapp

import com.deepak.currencyexchangeapp.model.ExchangeDataResp
import com.deepak.currencyexchangeapp.repository.ExchangeDataLocalRepository
import com.deepak.currencyexchangeapp.repository.ExchangeDataRemoteRepository
import com.deepak.currencyexchangeapp.repository.FakeExchangeLocalDataRepository
import com.deepak.currencyexchangeapp.repository.FakeExchangeRemoteDataRepository
import com.deepak.currencyexchangeapp.ui.HomeViewModel
import com.deepak.currencyexchangeapp.usecase.ExchangeDataUseCase
import com.deepak.currencyexchangeapp.usecase.ExchangeDataUseCaseImpl
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {
    @get:Rule
    var mainCoroutineRule: MainCoroutineRule = MainCoroutineRule()
    private val exchangeDataLocalRepository: ExchangeDataLocalRepository =
        FakeExchangeLocalDataRepository()

    private var exchangeDataRemoteRepository: ExchangeDataRemoteRepository =
        FakeExchangeRemoteDataRepository()
    private val useCase: ExchangeDataUseCase = ExchangeDataUseCaseImpl(
        remoteRepository = exchangeDataRemoteRepository,
        localRepository = exchangeDataLocalRepository
    )

    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setUp() {
        homeViewModel = HomeViewModel(useCase)
    }

    @Test
    fun fetchRemoteDataTest() = runTest {
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
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            homeViewModel.homeViewState.collect()
        }
        homeViewModel.fetchRateData()
        Assert.assertTrue(!homeViewModel.homeViewState.value.isLoading)
    }
}