package ru.jivan.nerecipeproject.repository

import androidx.lifecycle.LiveData
import ru.jivan.nerecipeproject.data.Recipe

interface RecipeRepository {

    val data: LiveData<List<Recipe>>

    fun save(recipe: Recipe)

    fun delete(recipeId: Long)

}