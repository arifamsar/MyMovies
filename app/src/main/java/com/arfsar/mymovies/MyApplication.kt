package com.arfsar.mymovies

import android.app.Application
import com.arfsar.mymovies.core.di.dataStoreModule
import com.arfsar.mymovies.core.di.databaseModule
import com.arfsar.mymovies.core.di.networkModule
import com.arfsar.mymovies.core.di.repositoryModule
import com.arfsar.mymovies.di.useCaseModule
import com.arfsar.mymovies.di.viewModelModule
import leakcanary.LeakCanary
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            LeakCanary.config = LeakCanary.config.copy(dumpHeap = true)
        } else {
            LeakCanary.config = LeakCanary.config.copy(dumpHeap = false)
        }
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyApplication)
            modules(
                databaseModule,
                networkModule,
                repositoryModule,
                useCaseModule,
                viewModelModule,
                dataStoreModule
            )
        }
    }
}