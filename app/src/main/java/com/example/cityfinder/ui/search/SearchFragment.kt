package com.example.cityfinder.ui.search

import android.content.res.Configuration
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cityfinder.R
import com.example.cityfinder.databinding.FragmentSearchBinding
import com.example.cityfinder.repo.model.City
import com.example.cityfinder.ui.search.adapter.CityAdapter
import com.example.cityfinder.utils.afterTextChanged
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val searchViewModel by viewModel<SearchViewModel>()
    lateinit var citiesAdapter: CityAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_search, container, false
        )
        setupUI()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
    }

    private fun setupUI() {
        addAdapterAndScrollListener()
        addListenerForSearchBar()
        resizePhoto(false)
        refreshData(
            searchViewModel.searchForCities("")
        )

        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            }
        )
    }

    private fun addListenerForSearchBar() {
        binding.searchBar?.afterTextChanged {
            refreshData(
                searchViewModel.searchForCities(
                    it.toUpperCase(
                        Locale.getDefault()
                    )
                )
            )
        }
        binding.searchBar?.setOnFocusChangeListener { _, hasFocus ->
            resizePhoto(hasFocus)
        }
    }

    private fun resizePhoto(hasFocus: Boolean) {
        var imageHeight = 50.0f
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            imageHeight *= 2
        }
        if (hasFocus)
            binding.backgroundImage.layoutParams.height = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                imageHeight,
                resources.displayMetrics
            ).toInt()
        else
            binding.backgroundImage.layoutParams.height = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                imageHeight * 2,
                resources.displayMetrics
            ).toInt()
    }

    private fun addAdapterAndScrollListener() {
        val linearLayoutManager = LinearLayoutManager(activity)
        binding.citiesRecyclerView.layoutManager = linearLayoutManager

        binding.citiesRecyclerView.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    recyclerView.layoutManager?.let {
                        it as LinearLayoutManager
                        val totalItems = it.itemCount
                        val visibleItems = it.childCount

                        if (visibleItems + it.findFirstVisibleItemPosition() > totalItems - 20) {
                            citiesAdapter.updateCities(searchViewModel.getNextCities())
                            citiesAdapter.notifyDataSetChanged()
                        }
                    }
                }
            })
    }

    private fun refreshData(cities: List<City>) {
        if (!::citiesAdapter.isInitialized) {
            citiesAdapter = CityAdapter { showCityOnMap(it) }
        }
        binding.citiesRecyclerView.adapter = citiesAdapter
        citiesAdapter.updateCities(cities)
        citiesAdapter.notifyDataSetChanged()
    }

    private fun showCityOnMap(city: City) {
        val action = SearchFragmentDirections.actionShowCityOnMap(city)
        findNavController().navigate(action)
    }

}
