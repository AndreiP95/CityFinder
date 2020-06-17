# CityFinder

CityFinder is an app that is searching for a favourite tree. It works by typing a name of a city, showing the city list filtered by the typed name
and showing the selected city on a map.

## Documentation

[General](#general)

[UI Details](#ui-details)

[Trie algorithm](#trie-algorithm)

[Spatial and temporal Complexity](#complexity)


## General

 - The application is build based on MVVM architecture pattern and is using [Android Arhitecture Components](https://developer.android.com/topic/libraries/architecture).
 - Using of ViewModel and Databinding for UI implementation.
 - The Application is also using the following libraries: [Koin](https://insert-koin.io/), [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html), [Jackson](https://github.com/FasterXML/jackson)
 - Code cleanup for the app has been made using [Sonar Lint](https://www.sonarlint.org/)
 - Written in [Kotlin]("https://kotlinlang.org/) version 1.3.70
 - IDE used is [Android](https://developer.android.com/studio) version 3.5.1
 - The app min requirement is SDK 21 ( Android 5.0, [Lolipop](https://developer.android.com/about/versions/lollipop) )

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
                Tree structure: 
                                               M ( cities = null, children = A,I)
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


   ### Second step
   - Filter algorithm is based on a [Depth first search](https://en.wikipedia.org/wiki/Depth-first_search) after the characters inside the user input. 
   - It starts from root node, checks if the next character is part of the nodes children and goes to the next children. If one of the characters is not in the tree structure, the search stops and returns an empty list, otherwise it returns the node where to start retrieving the cities.
   
   ### Third step
   - The final step is creating the list of cities by retrieving the first page of the cities, containing 100 entries. If the user wants to see the next page, the retrieving continues from the last node, which is kept in a stack. 
   - When the user changes the input, the stack and the city list are cleared and the search restarts from the root node.

## Spatial and Temporal Complexity 
  
  ### Temporal Complexity
  
  - Searching for the first city with the coresponding prefix is done in a O(k) complexity, with k being the number of characters in the input. Based on the fact that max number of characters in a city is 50, the complexity is close to being O(log(n)) which is far greater than linear.
  - Showing the cities that have the user input prefix, are shown paginated with increments of 100 such as, the only case when the list contains all entries is when the user scrolls all to the bottom of the list and the input field is empty.
  
  
  ### Spatial Complexity
  
  - The spatial complexity of the tree is close to O(n), since we keep the entries only once in the tree. The only increase in spatial complexity is due to the references of the children nodes in the tree. 
  - Since the tree is created by adding nodes for each character in a word, the implementation is scalable if more cities are added due to the fact that if we have more cities that contains the same prefix, less nodes are needed to be created, and the spatial complexity will remain linear with each new entry that will be added. 
  
  Also, this implementation is more scalable if more entries are added / removed. If we want to add 100k more cities, it won't change the existing nodes, it will add more children and entries. Comparing with a list, the list will need to be re-sorted, which is a time consuming task. 
