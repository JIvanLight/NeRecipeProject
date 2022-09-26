package ru.jivan.nerecipeproject.repository

import androidx.lifecycle.LiveData
import ru.jivan.nerecipeproject.data.Recipe
import ru.jivan.nerecipeproject.data.Step

interface RecipeRepository {

    val data: LiveData<List<Recipe>>

    fun save(recipe: Recipe)
    fun save(recipeId: Long, recipeStep: Step)
    fun getSteps(recipeId: Long): List<Step>
    fun delete(recipeId: Long)
    fun deleteStep(stepId: Long)
    fun search(searchQuery: String?, categoriesList: List<String>?, favorite: Boolean): List<Recipe>
    fun favorite(recipeId: Long)
    fun swapPositions(firstRecipe: Recipe, secondRecipe: Recipe)
}