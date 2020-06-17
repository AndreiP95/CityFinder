package com.example.cityfinder.repo.model

import java.util.*

/**
 *
 * @property char Char = Corresponding character of the Trie node
 * @property nodes SortedMap<Char, CityNode> = Children of this node, sorted reverse alphabetically
 * Reverse alphabetically is needed since the stack mechanism is First in Last out
 * @property cities MutableSet<City>? = Cities with matching name
 * @constructor
 */

data class CityNode(
    val char: Char,
    val nodes: SortedMap<Char, CityNode>,
    var cities: MutableSet<City>? = null
)