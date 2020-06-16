package com.example.citiessearch.ui.search.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.citiessearch.R
import com.example.citiessearch.repo.model.City
import kotlinx.android.synthetic.main.item_city.view.*
import org.koin.core.KoinComponent

class CityAdapter(
    val itemClick: (City) -> Unit
) : RecyclerView.Adapter<CityAdapter.CityViewHolder>(), KoinComponent {

    private lateinit var cities: List<City>

    fun updateCities(cities: List<City>) {
        this.cities = cities
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val cityView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_city, parent, false)

        return CityViewHolder(cityView)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        holder.bind(cities.get(position))

    }

    override fun getItemCount(): Int {
        return cities.size
    }

    inner class CityViewHolder(cityLayout: View) : RecyclerView.ViewHolder(cityLayout) {
        fun bind(city: City) {
            itemView.city_coords.text = city.coord.toString()
            itemView.city_name.text = city.let { "${it.name},${it.country}" }
            itemView.setOnClickListener { itemClick(city) }
        }

    }
}