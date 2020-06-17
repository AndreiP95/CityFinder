package com.example.cityfinder.ui.search

import androidx.lifecycle.ViewModel
import com.example.cityfinder.repo.TrieAlgorithm
import com.example.cityfinder.repo.model.City
import org.koin.core.KoinComponent
import org.koin.core.inject

class SearchViewModel : ViewModel(), KoinComponent {

    private val trieAlgorithm = inject<TrieAlgorithm>()
    private lateinit var entryText: String

    fun searchForCities(city: String): List<City> {
        entryText = city
        return trieAlgorithm.value.filterCities(city)
    }

    fun getNextCities(): List<City> {
        return trieAlgorithm.value.getNextPage()
    }
}
