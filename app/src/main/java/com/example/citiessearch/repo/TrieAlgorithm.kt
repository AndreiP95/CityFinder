package com.example.citiessearch.repo

import com.example.citiessearch.repo.model.City
import com.example.citiessearch.repo.model.CityNode
import com.example.citiessearch.utils.CITIES_LIMIT
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class TrieAlgorithm {

    private val root =
        CityNode('/', HashMap<Char, CityNode>().toSortedMap(compareByDescending { it }))
    private val filteredList: ArrayList<City> = ArrayList()
    private val cityNodeStack: Stack<CityNode> = Stack()
    private var currentFilteredCities = 0

    fun createTreeFromData(cityList: List<City>) {
        cityList.forEach { addCity(it) }
    }

    private fun addCity(city: City) {
        /* Adds the city to the tree. The city is added with its corresponding cities already sorted */
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

    fun filterCities(userInput: String): ArrayList<City> {
        /* Resets the previous search, adds the corresponding substring node
            Or root if the userInput is empty and starts retrieving the cities
         */
        resetFilterValues()
        if (userInput.isNotEmpty()) {
            cityNodeStack.push(findStartNode(userInput))
        } else {
            cityNodeStack.push(root)
        }
        searchCities()
        return filteredList
    }

    fun findStartNode(cityName: String): CityNode? {
        /* Start node is the corresponding node where adding the chars
            from root to this node will equal user inserted string */
        if (cityName.isEmpty())
            return root

        var tempNode = root
        cityName.forEach { char ->
            if (tempNode.nodes.containsKey(char))
                tempNode.nodes[char]?.let { tempNode = it }
            else
                return null
        }
        return tempNode
    }

    private fun searchCities() {
        /* Iterative Depth first search to find another page of cities
            It saves the stack for the case when the user is scrolling down and wants to see the next
            Page of cities
         */
        lateinit var currentNode: CityNode
        while (!cityNodeStack.isEmpty() && currentFilteredCities < CITIES_LIMIT) {
            currentNode = cityNodeStack.pop()
            addCitiesToList(currentNode)
            currentNode.nodes.forEach {
                cityNodeStack.push(it.value)
            }
        }
    }

    private fun addCitiesToList(node: CityNode) {
        /* Collects saved cities from each node, the cities are already sorted in each node */
        node.cities.forEach { city ->
            currentFilteredCities++
            filteredList.add(city)
        }
    }

    private fun resetFilterValues() {
        cityNodeStack.clear()
        filteredList.clear()
        currentFilteredCities = 0
    }

    fun getNextPage(): List<City> {
        currentFilteredCities = 0
        searchCities()
        return filteredList
    }

}