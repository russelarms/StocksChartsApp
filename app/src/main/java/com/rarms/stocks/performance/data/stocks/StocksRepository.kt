package com.rarms.stocks.performance.data.stocks

import com.rarms.stocks.performance.data.network.NetworkResponse
import com.rarms.stocks.performance.model.QuoteSymbol
import com.rarms.stocks.performance.model.Timeframe
import kotlinx.coroutines.flow.Flow

interface StocksRepository {

    val monthData: Flow<List<QuoteSymbol>>
    val weekData: Flow<List<QuoteSymbol>>

    fun fetchMonthData(): Flow<NetworkResponse>
    fun fetchWeekData(): Flow<NetworkResponse>

    suspend fun refresh(timeframe: Timeframe)
}