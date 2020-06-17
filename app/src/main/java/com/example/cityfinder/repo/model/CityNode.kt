package com.example.cityfinder.repo.model

import java.util.*

data class CityNode(
    val char: Char,
    val nodes: SortedMap<Char, CityNode>,
    var cities: MutableSet<City>? = null
)