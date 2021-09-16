package com.rarms.stocks.performance.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.StackedLineChart
import androidx.compose.ui.graphics.vector.ImageVector

enum class StocksScreen(
    val icon: ImageVector,
    val readableName: String,
) {
    PerformanceComparison(
        icon = Icons.Outlined.StackedLineChart,
        readableName = "Performance"
    ),
    Candlesticks(
        icon = Icons.Outlined.BarChart,
        readableName = "Candlesticks"
    );

    companion object {
        fun fromRoute(route: String?): StocksScreen =
            when (route?.substringBefore("/")) {
                Candlesticks.name -> Candlesticks
                PerformanceComparison.name -> PerformanceComparison
                null -> PerformanceComparison
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
    }
}
