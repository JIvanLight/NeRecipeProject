package ru.jivan.nerecipeproject.adapter

import ru.jivan.nerecipeproject.data.Recipe

interface InteractionListener{
    fun onRecipeClicked(recipe: Recipe)
    fun onRemoveClicked(recipe: Recipe)
    fun onEditClicked(recipe: Recipe)
    fun onFavoriteClicked(recipe: Recipe)
    fun onDrag(firstRecipe:Recipe,secondRecipe:Recipe)
}