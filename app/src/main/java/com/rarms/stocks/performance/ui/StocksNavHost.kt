package com.rarms.stocks.performance.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.rarms.stocks.performance.data.AppContainer
import com.rarms.stocks.performance.ui.StocksScreen.Candlesticks
import com.rarms.stocks.performance.ui.StocksScreen.PerformanceComparison
import com.rarms.stocks.performance.ui.charts.candlesticks.CandlesticksScreen
import com.rarms.stocks.performance.ui.charts.candlesticks.CandlesticksViewModel
import com.rarms.stocks.performance.ui.charts.performance.PerformanceViewModel
import com.rarms.stocks.performance.ui.charts.performance.PerformanceScreen

@Composable
fun StocksNavHost(
    appContainer: AppContainer,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    // instantiation within NavHost leads to recreation of view models' at every navigation event
    val performanceViewModel: PerformanceViewModel = viewModel(
        key = PerformanceComparison.name,
        factory = PerformanceViewModel.Factory(appContainer.stocksRepository)
    )
    val candlesticksViewModel: CandlesticksViewModel = viewModel(
        key = Candlesticks.name,
        factory = CandlesticksViewModel.Factory(appContainer.stocksRepository)
    )
    NavHost(
        navController = navController,
        startDestination = PerformanceComparison.name,
        modifier = modifier
    ) {
        composable(PerformanceComparison.name) {
            PerformanceScreen(performanceViewModel)
        }
        composable(Candlesticks.name) {
            CandlesticksScreen(candlesticksViewModel)
        }
    }
}