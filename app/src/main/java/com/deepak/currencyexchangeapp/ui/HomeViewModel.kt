package com.deepak.currencyexchangeapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deepak.currencyexchangeapp.model.ExchangeUiModel
import com.deepak.currencyexchangeapp.usecase.ExchangeDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Data class using maintaining home screen view state
 */
data class HomeViewState(
    val isLoading: Boolean = false,
    val rateDataList: List<ExchangeUiModel> = emptyList(),
    val isError: Boolean = false,
    val errorMessage: String? = null,
    val userAmount: String = "",
    val calculatedAmount: Double = 0.0,
    val symbolList: List<ExchangeUiModel> = emptyList(),
    val showSymbolSelection: Boolean = false,
    val selectedCurrency: ExchangeUiModel = ExchangeUiModel(
        "USD", 1.0, calculatedRate = 1.0
    )
)

private const val TAG = "HomeViewModel"

/**
 * HomeViewModel for maintaining home screen state,
 * @param useCase : For handling necessary currency exchange calculation.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(private val useCase: ExchangeDataUseCase) : ViewModel() {
    var homeViewState = MutableStateFlow(HomeViewState())
        private set

    init {
        //Registering data model observer
        viewModelScope.launch {
            useCase.getExchangeDataObserver().collectLatest {
                homeViewState.value = homeViewState.value.copy(rateDataList = it, symbolList = it)
            }
        }
    }

    /**
     * Fetching data from remote sever
     */
    fun fetchRateData() {
        viewModelScope.launch {
            homeViewState.value = homeViewState.value.copy(isLoading = true, isError = false)
            useCase.fetchRemoteData().collectLatest {
                it.onSuccess {
                    homeViewState.value = homeViewState.value.copy(isLoading = false)
                }
                it.onFailure {
                    homeViewState.value = homeViewState.value.copy(
                        isLoading = false,
                        isError = true,
                        errorMessage = it.message ?: "Something went wrong"
                    )
                }
            }
        }
    }

    /**
     * For maintaining data state of user input amount
     */
    fun setUserAmount(amount: String) {
        val userAmount = amount.toDoubleOrNull() ?: 1.0
        val calculatedList = useCase.calculateExchangeRate(
            list = homeViewState.value.rateDataList,
            amount = userAmount,
            selectedCurrencyRate = homeViewState.value.selectedCurrency.rate
        )
        homeViewState.value =
            homeViewState.value.copy(userAmount = amount, rateDataList = calculatedList)
    }

    /**
     * For maintaining selected symbol of user input
     */
    fun setSelectedSymbol(it: ExchangeUiModel) {
        homeViewState.value = homeViewState.value.copy(selectedCurrency = it)
    }

    /**
     * For maintaining symbol selection dialog state.
     */
    fun showSymbolSelection(show: Boolean) {
        homeViewState.value = homeViewState.value.copy(showSymbolSelection = show)
    }
}