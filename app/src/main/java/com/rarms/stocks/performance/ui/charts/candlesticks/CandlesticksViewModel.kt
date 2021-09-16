package com.rarms.stocks.performance.ui.charts.candlesticks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rarms.stocks.performance.data.stocks.StocksRepository
import com.rarms.stocks.performance.model.QuoteSymbol
import com.rarms.stocks.performance.ui.charts.ChartViewModel
import com.rarms.stocks.performance.ui.charts.ChartViewState
import kotlinx.coroutines.flow.MutableStateFlow

class CandlesticksViewModel(stocksRepository: StocksRepository) : ChartViewModel(stocksRepository) {
    val selectedStockIndex: MutableStateFlow<Int> = MutableStateFlow(0)
    val stockNames: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())

    override fun onViewStateUpdate(state: MutableStateFlow<ChartViewState>) {
        findStockNames(state.value.stocks)
    }

    private fun findStockNames(stocks: List<QuoteSymbol>) {
        stockNames.value = stocks.map {
            it.symbol
        }
    }
    fun onStockSelected(stockIndex: Int) {
        selectedStockIndex.value = stockIndex
    }

    class Factory(private val stocksRepository: StocksRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return CandlesticksViewModel(stocksRepository) as T
        }
    }
}
