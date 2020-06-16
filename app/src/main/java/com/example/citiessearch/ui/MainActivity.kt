package com.example.citiessearch.ui

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.example.citiessearch.R
import com.example.citiessearch.repo.TrieAlgorithm
import com.example.citiessearch.repo.model.City
import com.fasterxml.jackson.databind.ObjectMapper
import org.koin.android.ext.android.inject
import java.io.InputStream

class MainActivity : FragmentActivity() {

    private lateinit var reader: InputStream
    private val trie = inject<TrieAlgorithm>()

    private fun startReader(): List<City> {
        reader = resources.openRawResource(R.raw.cities)
        val buffer = ByteArray(reader.available())
        while (reader.read(buffer) != -1) {
        }

        val text = String(buffer)
        return ObjectMapper().readValue<List<City>>(
            text,
            ObjectMapper().typeFactory.constructCollectionType(List::class.java, City::class.java)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        trie.value.createTreeFromData(startReader())
    }

}
