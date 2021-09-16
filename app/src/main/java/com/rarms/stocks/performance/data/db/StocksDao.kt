package com.rarms.stocks.performance.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StocksDao {
    @Query("SELECT * FROM stocks WHERE timeframe = :timeframe ORDER BY symbolId")
    fun getStocks(timeframe: Int): Flow<List<StockDBO>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(stocks: List<StockDBO>)
}
