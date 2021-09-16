package com.rarms.stocks.performance.data.network

import com.rarms.stocks.performance.model.QuoteSymbol

sealed class NetworkResponse {
    data class Success(
        val content: Content,
        val status: String
    ) : NetworkResponse()

    data class Error(
        val cause: String
    ) : NetworkResponse()

}

data class Content(
    val quoteSymbols: List<QuoteSymbol>
)