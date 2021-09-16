package com.rarms.stocks.performance.util

fun calculatePerformance(priceItems: List<Number>) : List<Double> {
    priceItems.onEach {
        if (it.toInt() <= 0) {
            throw IllegalArgumentException("prices must be greater than zero")
        }
    }
    val mapIndexed = priceItems.mapIndexed { index, number ->
        val element = number.toDouble()
        val firstElement = priceItems[0].toDouble()
        val division = element / firstElement
        val div = (division * 100 - 100)
        div
    }
    return mapIndexed
}