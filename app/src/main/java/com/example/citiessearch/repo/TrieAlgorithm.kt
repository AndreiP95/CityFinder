package com.example.citiessearch.repo

import com.example.citiessearch.repo.model.City
import com.example.citiessearch.repo.model.CityNode

class TrieAlgorithm {

    private val root =
        CityNode('/', HashMap<Char, CityNode>().toSortedMap(compareByDescending { it }))

    fun createTreeFromData(cityList: List<City>) {
        cityList.forEach { addCity(it) }
    }

    private fun addCity(city: City) {
        var tempNode = root
        city.name?.forEach {
            it.toUpperCase().let { char ->
                tempNode.nodes.let {
                    if (!it.containsKey(char)) {
                        val createdNode = CityNode(
                            char,
                            HashMap<Char, CityNode>().toSortedMap(compareByDescending { it })
                        )
                        tempNode.nodes[char] = createdNode
                    }
                    tempNode.nodes.get(char)?.let { tempNode = it }
                }
            }
        }
        tempNode.cities.add(city)
    }

}