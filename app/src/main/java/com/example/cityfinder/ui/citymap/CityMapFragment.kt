package com.example.cityfinder.ui.citymap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.cityfinder.R
import com.example.cityfinder.databinding.FragmentCityMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.koin.androidx.viewmodel.ext.android.viewModel

class CityMapFragment : Fragment(), OnMapReadyCallback {

    private val cityMapViewModel by viewModel<CityMapViewModel>()
    private lateinit var binding: FragmentCityMapBinding
    val args: CityMapFragmentArgs by navArgs()
    private var mapView: MapView? = null


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
        if (mapView == null) {
            mapView = binding.map
            mapView?.onCreate(savedInstanceState)
        }
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            }
        )
        mapView?.getMapAsync(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap?.apply {
            cityMapViewModel.city?.let { city ->
                city.coord?.let { coords ->
                    val currentCity = LatLng(coords.lat, coords.lon)
                    val marker = addMarker(
                        MarkerOptions()
                            .position(currentCity)
                            .title("${city.name}, ${city.country}")
                    )
                    googleMap.animateCamera(
                        CameraUpdateFactory.newCameraPosition(
                            CameraPosition.builder()
                                .target(currentCity)
                                .zoom(8.0f)
                                .build()
                        )
                    )
                    marker.showInfoWindow()
                }
            }

        }
    }
}
