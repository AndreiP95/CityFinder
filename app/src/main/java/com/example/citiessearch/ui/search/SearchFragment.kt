package com.example.citiessearch.ui.search

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.citiessearch.R
import com.example.citiessearch.databinding.FragmentSearchBinding
import com.example.citiessearch.repo.model.City
import com.example.citiessearch.ui.search.adapter.CityAdapter
import com.example.citiessearch.utils.afterTextChanged
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
            if (hasFocus)
                binding.backgroundImage.layoutParams.height = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    100.0f,
                    resources.displayMetrics
                ).toInt()
            else
                binding.backgroundImage.layoutParams.height = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    200.0f,
                    resources.displayMetrics
                ).toInt()
        }
    }

    override fun onResume() {
        super.onResume()
        refreshData(searchViewModel.getNextCities())
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
            citiesAdapter = CityAdapter { showCityOnMap() }
        }
        binding.citiesRecyclerView.adapter = citiesAdapter
        citiesAdapter.updateCities(cities)
        citiesAdapter.notifyDataSetChanged()
    }

    private fun showCityOnMap() {
        val action = SearchFragmentDirections.actionShowCityOnMap()
        findNavController().navigate(action)
    }

}
