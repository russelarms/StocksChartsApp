package com.rarms.stocks.performance.data.stocks.impl

import com.rarms.stocks.performance.data.db.StocksDao
import com.rarms.stocks.performance.data.db.convertQuoteSymbolToStockDBO
import com.rarms.stocks.performance.data.network.Network
import com.rarms.stocks.performance.data.network.NetworkResponse
import com.rarms.stocks.performance.data.stocks.StocksRepository
import com.rarms.stocks.performance.model.QuoteSymbol
import com.rarms.stocks.performance.model.Timeframe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
class StocksRepositoryImpl(private val network: Network, private val stocksDao: StocksDao) : StocksRepository {

    override val monthData: Flow<List<QuoteSymbol>> = getStocksFlow(Timeframe.Month)
    override val weekData: Flow<List<QuoteSymbol>> = getStocksFlow(Timeframe.Week)

    private fun getStocksFlow(timeframe: Timeframe): Flow<List<QuoteSymbol>> {
        return stocksDao
            .getStocks(timeframe.ordinal)
            .mapLatest { list ->
                val convertedList: List<QuoteSymbol> = list.map { stockDBO -> stockDBO.symbol }
                return@mapLatest convertedList
            }
    }

    override fun fetchMonthData(): Flow<NetworkResponse> {
        return flow<NetworkResponse> {
            val monthData = network.fetchMonthData()
            val stockDBOs = monthData.content.quoteSymbols.map { quoteSymbol ->
                convertQuoteSymbolToStockDBO(quoteSymbol, Timeframe.Month)
            }
            stocksDao.insertAll(stockDBOs)
            emit(monthData)
        }.catch { e ->
            // If an exception was caught while fetching the podcast, wrap it in
            // an Error instance.
            emit(NetworkResponse.Error(e.message ?: "no cause"))
        }
    }

    override fun fetchWeekData(): Flow<NetworkResponse> {
        return flow<NetworkResponse> {
            val weekData = network.fetchWeekData()
            val stockDBOs = weekData.content.quoteSymbols.map { quoteSymbol ->
                convertQuoteSymbolToStockDBO(quoteSymbol, Timeframe.Week)
            }
            stocksDao.insertAll(stockDBOs)
            emit(network.fetchWeekData())
        }.catch { e ->
            // If an exception was caught while fetching the podcast, wrap it in
            // an Error instance.
            emit(NetworkResponse.Error(e.message ?: "no cause"))
        }
    }

    override suspend fun refresh(timeframe: Timeframe) {
        val request: Flow<NetworkResponse> = when (timeframe) {
            Timeframe.Week -> fetchWeekData()
            Timeframe.Month -> fetchMonthData()
        }
        request
            .onEach { if (it is NetworkResponse.Error) throw Exception(it.cause) }
            .collect()
    }
}