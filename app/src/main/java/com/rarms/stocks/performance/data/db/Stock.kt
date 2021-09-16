package com.rarms.stocks.performance.data.db

import androidx.room.Entity
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.rarms.stocks.performance.model.QuoteSymbol
import com.rarms.stocks.performance.model.Timeframe

@Entity(tableName = "stocks", primaryKeys = ["symbolId", "timeframe"])
data class StockDBO(
    val symbolId: String, //AAPL
    @TypeConverters(Converters::class) val symbol: QuoteSymbol, //store as json for simplification
    val timeframe: Int //Timeframe.order
)

fun convertQuoteSymbolToStockDBO(quoteSymbol: QuoteSymbol, timeframe: Timeframe) : StockDBO {
    val symbolId = quoteSymbol.symbol
    val time = timeframe.ordinal
    return StockDBO(symbolId, quoteSymbol, time)
}

class Converters {
    var gson = Gson()

    @TypeConverter
    fun stringToQuoteSymbols(data: String?): QuoteSymbol {
        if (data == null) {
            throw RuntimeException("data is empty")
        }
        return gson.fromJson(data, QuoteSymbol::class.java)
    }

    @TypeConverter
    fun quoteSymbolToString(someObjects: QuoteSymbol): String {
        return gson.toJson(someObjects)
    }
}
