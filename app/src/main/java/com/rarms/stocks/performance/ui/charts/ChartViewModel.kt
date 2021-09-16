package com.rarms.stocks.performance.ui.charts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rarms.stocks.performance.data.stocks.StocksRepository
import com.rarms.stocks.performance.model.QuoteSymbol
import com.rarms.stocks.performance.model.Timeframe
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class ChartViewModel(private val stocksRepository: StocksRepository) : ViewModel() {

    private val selectedTimeframe = MutableStateFlow(Timeframe.Week)
    private val refreshing = MutableStateFlow(false)
    private val error = MutableStateFlow("")
    private val _state = MutableStateFlow(ChartViewState())
    val state: StateFlow<ChartViewState>
        get() = _state

    init {
        initialDataLoad()
    }

    open fun onViewStateUpdate(_state: MutableStateFlow<ChartViewState>) {}

    private fun initialDataLoad() {
        viewModelScope.launch {
            loadData()
            val stocksDataFlow = combine(
                selectedTimeframe,
                stocksRepository.monthData,
                stocksRepository.weekData
            ) { timeframe, monthdata, weekdata ->
                resolveStocks(timeframe, monthdata, weekdata)
            }
            combine(
                selectedTimeframe,
                refreshing,
                stocksDataFlow,
                error,
            ) { timeframe, refreshing, stocksData, error ->
                ChartViewState(
                    stocks = stocksData,
                    timeframe = timeframe,
                    refreshing = refreshing,
                    error = error,
                )
            }.catch { throwable ->
                throw throwable
            }.collect {
                _state.value = it
                onViewStateUpdate(_state)
            }
        }
    }

    private suspend fun loadData() {
        try {
            refreshing.value = true
            stocksRepository.refresh(Timeframe.Week)
            stocksRepository.refresh(Timeframe.Month)
            refreshing.value = false
            error.value = ""
        } catch (e: Exception) {
            error.value = e.message ?: ""
        }
    }

    private fun resolveStocks(
        timeframe: Timeframe,
        monthdata: List<QuoteSymbol>,
        weekdata: List<QuoteSymbol>
    ): List<QuoteSymbol> =
        when (timeframe) {
            Timeframe.Month -> if (monthdata.isNotEmpty()) monthdata else emptyList()
            Timeframe.Week -> if (weekdata.isNotEmpty()) weekdata else emptyList()
        }

    fun updateDataForSelectedTimeframe() {
        viewModelScope.launch {
            refreshing.value = true
            try {
                stocksRepository.refresh(selectedTimeframe.value)
                error.value = ""
            } catch (e: Exception) {
                error.value = e.message ?: ""
            }
            refreshing.value = false
        }
    }

    fun onTimeframeSelected(timeframe: Timeframe) {
        selectedTimeframe.value = timeframe
    }
}

data class ChartViewState(
    val stocks: List<QuoteSymbol> = emptyList(),
    val error: String = "",
    val refreshing: Boolean = false,
    val timeframe: Timeframe = Timeframe.Week
)