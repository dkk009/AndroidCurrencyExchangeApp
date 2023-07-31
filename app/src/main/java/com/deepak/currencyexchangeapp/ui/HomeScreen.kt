package com.deepak.currencyexchangeapp.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.deepak.currencyexchangeapp.R
import com.deepak.currencyexchangeapp.model.ExchangeUiModel
import java.text.DecimalFormat

private val df = DecimalFormat("#.##")

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(homeViewModel: HomeViewModel = hiltViewModel()) {
    val homeUiState by homeViewModel.homeViewState.collectAsState()
    LaunchedEffect(key1 = LocalLifecycleOwner.current.lifecycle, block = {
        homeViewModel.fetchRateData()
    })

    Scaffold(modifier = Modifier.fillMaxWidth(), topBar = {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(text = "PayPayExchange", style = MaterialTheme.typography.headlineMedium)
            }
        }
    }) {
        val scrollState = rememberLazyGridState()
        if (homeUiState.showSymbolSelection) {
            ShowCurrencySelector(homeViewModel = homeViewModel)
        }
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    value = homeUiState.userAmount,
                    textStyle = MaterialTheme.typography.bodySmall.copy(textAlign = TextAlign.End),
                    onValueChange = {
                        homeViewModel.setUserAmount(it)
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Number
                    ), placeholder = {
                        Text(text = "####.##")
                    }, modifier = Modifier
                        .align(Alignment.End)
                        .testTag("input_field")
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                        .clickable {
                            homeViewModel.showSymbolSelection(true)
                        }
                        .align(Alignment.End).testTag("select_currency_dialog")
                ) {
                    Text(
                        text = homeUiState.selectedCurrency.symbol,
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_down_24),
                        contentDescription = "Drop down"
                    )
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(horizontal = 20.dp), state = scrollState
                ) {
                    items(
                        items = homeUiState.rateDataList,
                        key = {
                            it.symbol.plus(it.rate)
                        },
                        span = {
                            GridItemSpan(1)
                        },
                    ) { data ->
                        CurrencyGridCell(
                            model = data,
                            isSelected = data == homeUiState.selectedCurrency
                        ) {
                            homeViewModel.setSelectedSymbol(it)
                        }
                    }
                }
            }
            if (homeUiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}


@Composable
private fun CurrencyGridCell(
    model: ExchangeUiModel,
    isSelected: Boolean = false,
    onClick: (ExchangeUiModel) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable {
                onClick.invoke(model)
            },
        colors = CardDefaults.cardColors(containerColor = if (isSelected) Color.Blue else Color.LightGray)
    ) {
        val textColor = if (isSelected) Color.White else Color.Black
        Text(
            text = model.symbol,
            style = MaterialTheme.typography.headlineMedium.copy(color = textColor),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            text = df.format(model.calculatedRate),
            style = MaterialTheme.typography.headlineSmall.copy(color = textColor),
            modifier = Modifier.align(Alignment.CenterHorizontally), maxLines = 2,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ShowCurrencySelector(homeViewModel: HomeViewModel) {
    val scrollState = rememberLazyListState()
    val homeUiState by homeViewModel.homeViewState.collectAsState()
    ModalBottomSheet(onDismissRequest = {
        homeViewModel.showSymbolSelection(false)
    }, modifier = Modifier.testTag("currency_selection_dialog")) {
        LazyColumn(
            state = scrollState,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            items(items = homeUiState.symbolList, key = {
                it.symbol
            }) { data ->
                Row(modifier = Modifier
                    .clickable {
                        homeViewModel.setSelectedSymbol(data)
                        homeViewModel.showSymbolSelection(false)
                    }
                    .fillMaxWidth()) {
                    Text(
                        text = data.symbol,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                }
            }
        }
    }
}