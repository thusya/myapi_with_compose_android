package com.app.procom

import android.app.Application
import com.app.procom.di.appModule
import com.app.procom.di.moviesModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext

class ProComApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        koinConfiguration()
    }

    private fun koinConfiguration() {
        GlobalContext.startKoin {
            androidContext(this@ProComApplication)
            modules(appModule, moviesModules)
        }
    }
}