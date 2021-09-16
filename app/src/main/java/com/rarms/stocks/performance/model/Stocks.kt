package com.rarms.stocks.performance.model

enum class Timeframe {
    Week, Month
}

enum class PerformanceBy {
    Opens, Closures
}

data class QuoteSymbol(
    val closures: List<Double>,
    val highs: List<Double>,
    val lows: List<Double>,
    val opens: List<Double>,
    val symbol: String,
    val timestamps: List<Long>,
    val volumes: List<Int>
)