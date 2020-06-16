package com.example.citiessearch

import android.app.Application
import com.example.citiessearch.ui.search.SearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class BaseApplication : Application() {

    private val viewModelModule = module {
        viewModel { SearchViewModel() }
    }

    override fun onCreate() {
        super.onCreate()
        System.gc()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@BaseApplication)
            modules(listOf(viewModelModule))
        }
    }
}