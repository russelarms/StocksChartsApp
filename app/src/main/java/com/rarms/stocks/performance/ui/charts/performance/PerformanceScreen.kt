package com.rarms.stocks.performance.ui.charts.performance

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rarms.stocks.performance.model.PerformanceBy
import com.rarms.stocks.performance.ui.charts.ChartBody

@Composable
fun PerformanceScreen(performanceViewModel: PerformanceViewModel) {
    val viewState by performanceViewModel.state.collectAsState()
    val performanceBy = performanceViewModel.selectedPerformanceBy.collectAsState()
    ChartBody(
        stocks = viewState.stocks,
        timeframe = viewState.timeframe,
        refreshing = viewState.refreshing,
        onTimeframeSelected = performanceViewModel::onTimeframeSelected,
        onRefreshClick = performanceViewModel::updateDataForSelectedTimeframe,
        error = viewState.error,
        settingsComposable = {
            SettingsWindow(
                chosenPerformance = performanceBy.value,
                performanceViewModel::onPerformanceSelected
            )
        },
        chartBody = {
            PerformanceChartBody(viewState.stocks, viewState.timeframe, performanceBy.value)
        }
    )
}


@Composable
fun SettingsWindow(
    chosenPerformance: PerformanceBy,
    onPerformanceSelected: (PerformanceBy) -> Unit
) {
    val radioOptions = PerformanceBy.values().map { it.name }.toList()
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(chosenPerformance.name) }
    Column(modifier = Modifier.padding(8.dp)) {
        Row(modifier = Modifier.padding(8.dp)) {
            Text(text = "Calculate performance by")
        }
        radioOptions.forEach { text ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = {
                            onOptionSelected(text)
                            onPerformanceSelected(PerformanceBy.valueOf(text))
                        }
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = {
                        onOptionSelected(text)
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

