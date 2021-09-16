package com.rarms.stocks.performance.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rarms.stocks.performance.model.Timeframe

@Composable
fun TimeframeTabs(
    timeframes: List<Timeframe>,
    selectedTimeframe: Timeframe,
    onTimeframeSelected: (Timeframe) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedIndex = timeframes.indexOfFirst { it == selectedTimeframe }
    val indicator = @Composable { tabPositions: List<TabPosition> ->
        TimeframeTabIndicator(
            Modifier.tabIndicatorOffset(tabPositions[selectedIndex])
        )
    }
    TabRow(
        selectedTabIndex = selectedIndex,
        indicator = indicator,
        modifier = modifier
    ) {
        timeframes.forEachIndexed { index, timeframe ->
            Tab(
                selected = index == selectedIndex,
                onClick = { onTimeframeSelected(timeframe) },
                text = {
                    Text(
                        text = timeframe.name,
                        style = MaterialTheme.typography.body2
                    )
                }
            )
        }
    }
}

@Composable
fun TimeframeTabIndicator(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.secondary
) {
    Spacer(
        modifier
            .padding(horizontal = 24.dp)
            .height(2.dp)
            .background(color, RoundedCornerShape(topStartPercent = 100, topEndPercent = 100))
    )
}
