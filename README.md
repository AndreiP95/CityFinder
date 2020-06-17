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

A dfs-based search tree

## Spatial and Temporal Complexity 

