package com.example.cityfinder

import android.app.Application
import com.example.cityfinder.repo.TrieAlgorithm
import com.example.cityfinder.ui.citymap.CityMapViewModel
import com.example.cityfinder.ui.search.SearchViewModel
import com.google.android.gms.maps.MapsInitializer
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class BaseApplication : Application() {

    private val viewModelModule = module {
        viewModel { SearchViewModel() }
        viewModel { CityMapViewModel() }
    }

    val trieModule = module {
        single {
            TrieAlgorithm()
        }
    }

    override fun onCreate() {
        super.onCreate()
        MapsInitializer.initialize(this)
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@BaseApplication)
            modules(listOf(viewModelModule, trieModule))
        }
    }
}