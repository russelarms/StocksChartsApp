package com.rarms.stocks.performance.ui.charts.candlesticks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rarms.stocks.performance.ui.charts.ChartBody

@Composable
fun CandlesticksScreen(candlesticksViewModel: CandlesticksViewModel) {
    val viewState by candlesticksViewModel.state.collectAsState()
    val selectedStockIndex = candlesticksViewModel.selectedStockIndex.collectAsState()
    val stockNames = candlesticksViewModel.stockNames.collectAsState()
    ChartBody(
        stocks = viewState.stocks,
        timeframe = viewState.timeframe,
        refreshing = viewState.refreshing,
        onTimeframeSelected = candlesticksViewModel::onTimeframeSelected,
        onRefreshClick = candlesticksViewModel::updateDataForSelectedTimeframe,
        error = viewState.error,
        settingsComposable = {
            CandlesSettingsWindow(
                selectedIndex = selectedStockIndex.value,
                stockNames = stockNames.value,
                onStockSelected = candlesticksViewModel::onStockSelected
            )
        },
        chartBody = {
            CandlesticksChartBody(
                data = viewState.stocks,
                timeframe = viewState.timeframe,
                selectedStockIndex = selectedStockIndex.value)
        }
    )
}

@Composable
fun CandlesSettingsWindow(
    selectedIndex: Int,
    stockNames: List<String>,
    onStockSelected: (Int) -> Unit
) {
    val radioOptions = stockNames
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(selectedIndex) }
    Column(modifier = Modifier.padding(8.dp)) {
        Row(modifier = Modifier.padding(8.dp)) {
            Text(text = "Choose stock")
        }
        radioOptions.forEachIndexed { index, text ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (index == selectedOption),
                        onClick = {
                            onOptionSelected(index)
                            onStockSelected(index)
                        }
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                RadioButton(
                    selected = (index == selectedOption),
                    onClick = {
                        onOptionSelected(index)
                    }
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.body1.merge(),
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}
