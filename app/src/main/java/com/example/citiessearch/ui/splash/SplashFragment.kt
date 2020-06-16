package com.example.citiessearch.ui.splash


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.citiessearch.R
import com.example.citiessearch.databinding.FragmentSplashBinding
import com.example.citiessearch.repo.TrieAlgorithm
import com.example.citiessearch.repo.model.City
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.io.InputStream
import kotlin.coroutines.CoroutineContext

class SplashFragment : Fragment(), CoroutineScope {

    private val trie = inject<TrieAlgorithm>()
    private lateinit var reader: InputStream
    private lateinit var binding: FragmentSplashBinding
    private var job: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_splash, container, false
        )
        return binding.root
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + (job ?: Job())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        binding.progressBar.isIndeterminate = true

        launch {
            createTree()
        }
    }

    private fun createTree() {
        trie.value.createTreeFromData(startReader())
        goToSearchFragment()
    }

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

    private fun goToSearchFragment() {
        val action = SplashFragmentDirections.actionSplashFragmentToSearchFragment()
        findNavController().navigate(action)
    }

}
