package com.example.citiessearch.ui.citymap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.citiessearch.R
import com.example.citiessearch.databinding.FragmentCityMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.koin.androidx.viewmodel.ext.android.viewModel

class CityMapFragment : Fragment(), OnMapReadyCallback {

    private val cityMapViewModel by viewModel<CityMapViewModel>()
    private lateinit var binding: FragmentCityMapBinding
    val args: CityMapFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_city_map, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        cityMapViewModel.city = args.cityData
        binding.map?.onCreate(savedInstanceState)
        binding.map?.onResume()
        binding.map?.getMapAsync(this)
    }

    override fun onResume() {
        super.onResume()
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()

                }
            }
        )
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap?.apply {
            cityMapViewModel.city?.let { city ->
                city.coord?.let { coords ->
                    val currentCity = LatLng(coords.lat, coords.lon)
                    val marker = addMarker(
                        MarkerOptions()
                            .position(currentCity)
                            .title("${city.name},${city.country}")
                    )
                    googleMap.animateCamera(
                        CameraUpdateFactory.newCameraPosition(
                            CameraPosition.builder()
                                .target(currentCity)
                                .zoom(15.0f)
                                .build()
                        )
                    )
                    marker.showInfoWindow()
                }
            }

        }
    }
}
