package com.android.moneytracker

import android.app.Application
import com.android.moneytracker.infrastructure.AppContainer
import com.android.moneytracker.infrastructure.AppDataContainer

class MoneyTrackerApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}