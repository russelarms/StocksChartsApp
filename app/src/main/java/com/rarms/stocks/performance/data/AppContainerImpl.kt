package com.rarms.stocks.performance.data

import android.content.Context
import com.rarms.stocks.performance.data.db.AppDatabase
import com.rarms.stocks.performance.data.network.getNetworkService
import com.rarms.stocks.performance.data.stocks.StocksRepository
import com.rarms.stocks.performance.data.stocks.impl.StocksRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Dependency Injection container at the application level.
 */
interface AppContainer {
    val stocksRepository: StocksRepository
}

/**
 * Implementation for the Dependency Injection container at the application level.
 *
 * Variables are initialized lazily and the same instance is shared across the whole app.
 */
@ExperimentalCoroutinesApi
class AppContainerImpl(private val applicationContext: Context) : AppContainer {

    override val stocksRepository: StocksRepository by lazy {
        StocksRepositoryImpl(
            getNetworkService(applicationContext),
            AppDatabase.getInstance(applicationContext).stocksDao()
        )
    }
}
