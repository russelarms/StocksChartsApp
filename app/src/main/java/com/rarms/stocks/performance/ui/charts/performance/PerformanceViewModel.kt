package com.rarms.stocks.performance.ui.charts.performance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rarms.stocks.performance.data.stocks.StocksRepository
import com.rarms.stocks.performance.model.PerformanceBy
import com.rarms.stocks.performance.ui.charts.ChartViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class PerformanceViewModel(stocksRepository: StocksRepository): ChartViewModel(stocksRepository) {
    val selectedPerformanceBy: MutableStateFlow<PerformanceBy> = MutableStateFlow(
        PerformanceBy.Opens)

    fun onPerformanceSelected(performanceBy: PerformanceBy) {
        selectedPerformanceBy.value = performanceBy
    }

    class Factory(private val stocksRepository: StocksRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return PerformanceViewModel(stocksRepository) as T
        }
    }
}
