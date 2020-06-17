package com.example.cityfinder.repo

import com.example.cityfinder.repo.model.City
import com.example.cityfinder.repo.model.CityNode
import com.example.cityfinder.utils.CITIES_LIMIT
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 *
 * @property root CityNode = Start point of the tree
 * @property filteredList ArrayList<City> = List with resulted cities after filtering
 * @property cityNodeStack Stack<CityNode> = Stack with the current dfs translation of the tree
 * @property currentFilteredCities Int = Number of filtered cities
 */

class TrieAlgorithm {

    private val root =
        CityNode('/', HashMap<Char, CityNode>().toSortedMap(compareByDescending { it }))
    private val filteredList: ArrayList<City> = ArrayList()

    private val cityNodeStack: Stack<CityNode> = Stack()
    var currentFilteredCities = 0

    /**
     *
     * @param cityList List<City> = Receives the list of cities
     * after it was read and parsed
     * Adds all the cities into the tree structure
     *
     */

    fun createTreeFromData(cityList: List<City>) {
        cityList.forEach { addCity(it) }
    }

    /**
     *
     * @param city City
     * Adds a city into the tree and creates coresponding nodes into the tree based on
     * the city's name length.
     * If there were no cities with the same char in prefix, it creates another node for each character,
     * otherwise it goes to the next node.
     * Saves the city in the node that contains the last char and is on len(city.name) level in the tree
     */

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
        if (tempNode.cities == null) {
            tempNode.cities = mutableSetOf()
            tempNode.cities?.add(city)
        } else {
            tempNode.cities?.add(city)
            tempNode.cities?.let {
                tempNode.cities = it.sortedWith(
                    compareBy(
                        { it.name },
                        { it.country }
                    )
                ).toMutableSet()
            }
        }
    }

    /**
     *
     * @param userInput String = User input text
     * @return ArrayList<City> = List of filtered cities
     * Resets the previous search and returns the next page of filteres cities
     * If the input is empty, then it starts from the root node
     * If the input is not empty, then it searches for the corresponding node
     * If no node that corresponds to the input is found, then it returns an empty list
     * If a node that corresponds to the input is found, then it returns the first page of cities
     */

    fun filterCities(userInput: String): ArrayList<City> {

        resetFilterValues()
        if (userInput.isNotEmpty()) {
            val node = findStartNode(userInput)
            if (node == null)
                return filteredList
            else
                cityNodeStack.push(node)
        } else {
            cityNodeStack.push(root)
        }
        searchCities()
        return filteredList
    }

    /**
     *
     * @param cityName String = user input text
     * @return CityNode? = Node that contains the user input text, or null if it does not exist
     */

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

    /**
     * Retrieve the next page of cities,
     * Uses an iterative Depth-First search for the retrieve
     * Uses a stack instead of doing it recursively, because after the CITIES_LIMIT
     * has been reached, for the next page the search should continue from the last node
     * If the stack has been cleared, the search stops
     */

    private fun searchCities() {
        lateinit var currentNode: CityNode
        while (!cityNodeStack.isEmpty() && currentFilteredCities < CITIES_LIMIT) {
            currentNode = cityNodeStack.pop()
            addCitiesToList(currentNode)
            currentNode.nodes.forEach {
                cityNodeStack.push(it.value)
            }
        }
    }

    /**
     *
     * @param node CityNode = Current node in dfs
     * Checks if the node has saved cities and adds them to the filtered list
     * Increments the total number of filtered cities to avoid calling
     * size for filteredList
     */

    private fun addCitiesToList(node: CityNode) {
        /* Collects saved cities from each node, the cities are already sorted in each node */
        node.cities?.forEach { city ->
            currentFilteredCities++
            filteredList.add(city)
        }
    }

    /**
     * Resets stack, list and number of filtered cities when another search is started
     *
     */

    private fun resetFilterValues() {
        cityNodeStack.clear()
        filteredList.clear()
        currentFilteredCities = 0
    }

    /**
     * Gets the next CITIES_LIMIT of cities
     * @return List<City> list with the previous pages plus the next ones
     */

    fun getNextPage(): List<City> {
        currentFilteredCities = 0
        searchCities()
        return filteredList
    }

}