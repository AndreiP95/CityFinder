package com.example.citiessearch

import com.example.citiessearch.repo.TrieAlgorithm
import org.junit.Before
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.inject

class TrieAlgorithmTest : KoinComponent {

    private val trieAlgorithm by inject<TrieAlgorithm>()

    companion object {
        private var hasContext = false
    }

    @Before
    fun createTree() {
        if (!hasContext) {
            val app = BaseApplication()
            startKoin {
                androidContext(app)
                modules(app.trieModule)
            }
            trieAlgorithm.createTreeFromData(MockCityList().cityList())
            hasContext = true
        }
    }


    @Test
    fun checkNoResults() {
        val userText = "zzz"
        assert(trieAlgorithm.filterCities(userText).isEmpty())
    }

    @Test
    fun checkInvalidInput() {
        val userText = "Bucharest" // All input should be converted to UpperCase
        assert(trieAlgorithm.filterCities(userText).size == 0)
    }

    @Test
    fun checkFindResultWithValidInput() {
        val userText = "Bucharest".toUpperCase()
        assert(trieAlgorithm.filterCities(userText).size == 1)
    }

    @Test
    fun checkFindMoreResults() {
        val userText = "B".toUpperCase()
        assert(trieAlgorithm.filterCities(userText).size == 3)
    }

    @Test
    fun checkIfNumberOfCitiesIsReset() {

        val userText = "B"
        trieAlgorithm.filterCities(userText)
        assert(trieAlgorithm.currentFilteredCities == 3)
        trieAlgorithm.filterCities("zzz")
        assert(trieAlgorithm.currentFilteredCities == 0)

    }

    @Test
    fun checkIfListOfCitiesIsReset() {
        var userText = "A"
        var list = trieAlgorithm.filterCities(userText)
        assert(list.size == 1)

        userText = "zzz"
        list = trieAlgorithm.filterCities(userText)
        assert(list.isEmpty())
    }

    @Test
    fun checkLastNode() {
        val userText = "Amster".toUpperCase()
        trieAlgorithm.findStartNode(userText)?.char?.let {
            assert(it == 'R')
        }
    }

}