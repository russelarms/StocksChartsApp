package com.rarms.stocks.performance.ui.charts

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rarms.stocks.performance.model.QuoteSymbol
import com.rarms.stocks.performance.model.Timeframe
import com.rarms.stocks.performance.ui.components.TimeframeTabs
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ChartBody(
    stocks: List<QuoteSymbol>,
    timeframe: Timeframe,
    refreshing: Boolean,
    onTimeframeSelected: (Timeframe) -> Unit,
    settingsComposable: @Composable () -> Unit,
    onRefreshClick: () -> Unit,
    error: String,
    chartBody: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        val state = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
        val scope = rememberCoroutineScope()
        ModalBottomSheetLayout(
            sheetState = state,
            sheetContent = { settingsComposable() }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                TabsAndButtons(timeframe, onTimeframeSelected, onRefreshClick,
                    onSettingsClicked = {
                        scope.launch {
                            state.show()
                        }
                    }
                )
                ScreenBody(error, stocks, chartBody)
            }
        }
        if (refreshing) {
            CircularProgressIndicator(color = MaterialTheme.colors.onSurface)
        }
    }

}

@Composable
private fun ScreenBody(
    error: String,
    stocks: List<QuoteSymbol>,
    chartBody: @Composable () -> Unit
) {
    when {
        error.isNotBlank() -> CenterOfScreen {
            Text(text = error)
        }
        stocks.isNotEmpty() -> chartBody()
        else -> CenterOfScreen {
            Text(text = "No stocks")
        }
    }
}

@Composable
private fun TabsAndButtons(
    timeframe: Timeframe,
    onTimeframeSelected: (Timeframe) -> Unit,
    onRefreshClick: () -> Unit,
    onSettingsClicked: () -> Unit
) {
    TimeframeTabs(
        timeframes = Timeframe.values().toList(),
        selectedTimeframe = timeframe,
        onTimeframeSelected = onTimeframeSelected
    )
    Row(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Button(modifier = Modifier
            .height(48.dp)
            .padding(8.dp), onClick = { onRefreshClick() }) {
            Text(text = "Refresh")
        }
        Button(modifier = Modifier
            .height(48.dp)
            .padding(8.dp), onClick = { onSettingsClicked() }) {
            Icon(
                imageVector = Icons.Outlined.Tune,
                contentDescription = "Settings"
            )
        }
    }
}

@Composable
fun CenterOfScreen(composable: @Composable () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()

    ) {
        composable()
    }
}
