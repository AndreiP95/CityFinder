# CityFinder

CityFinder is an app that is searching for a favourite tree. It works by typing a name of a city, showing the city list filtered by the typed name
and showing the selected city on a map.

## Documentation

[General](#general)

[UI Details](#ui-details)

[Trie algorithm](#trie-algorithm)

[Spatial and temporal Complexity](#complexity)


## General

## UI Details

The app is created from one Activity with three Fragments. 
  - A splash screen that is shown during the inital data processing.
        It contains the app logo and a loading spinner to show the user that the data is still loading
        
  - A search screen where the user can search and select the city that he/she wants to go to.
        It contains of an EditText where the user can add his/hers input and a RecyclerView to show the selected cities.
        The data are shown paginated ( 100 at a time ) and if the users scrolls to the end of the list, it will show the next page.
        When the user presses on a city, it will be redirected to the map.
        
  - A map screen that shows the location of the city on a Google Map
        It zooms in the location of the city while showing a marker containing the city name and country.
        
  
Navigation between UI is made using [Navigation components](https://developer.android.com/guide/navigation) to ensure the future
  scalability of the project, if other screens / features have to be added in the next development cycles. 
 

## Trie Algorithm

Filtering is based on a search tree based on the [Trie](https://en.wikipedia.org/wiki/Trie) algorithm based on three steps.

### First step
  - First step is reading the data into a buffer and parsing the JSON into a list of cities and creating the tree. 
    The tree is created based on the following structure:
    
      Every node contains three elements: A char, a list of children and a list of cities. For every city in the list, the tree is created by parsing the city name, creating a node for each char in its name and keeping its data into the node corresponding with the last character.
      
       #### Example 
          City names : Madrid
          Tree structure: 
                          M (cities = null, children = A)
                          🠫
                          A (cities = null, children = D)
                          🠫
                          D (cities = null, children = R)
                          🠫
                          R (cities = null, children = I)
                          🠫
                          I (cities = null, children = D)
                          🠫
                          D (cities = Madrid, children = null)
                          
                       City names : Madrid, Miami
                       Tree structure: M ( cities = null, children = A,I)
                                     ⬋  ⬊
    (cities = null, children = D )  A     I  (cities = null, children = I )
                                    🠫     🠫
    (cities = null, children = R)   D     A  (cities = null, children = A )
                                    🠫     🠫
    (cities = null, children = I)   R     M  (cities = null, children = M )
                                    🠫     🠫
    (cities = null, children = D)   I     I  (cities = Miami, children = null)
                                    🠫
(cities = Madrid, children = null)  D 


      

## Spatial and Temporal Complexity 

