package com.rarms.stocks.performance

import android.app.Application
import com.rarms.stocks.performance.data.AppContainer
import com.rarms.stocks.performance.data.AppContainerImpl

class StocksApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainerImpl(this)
    }
}