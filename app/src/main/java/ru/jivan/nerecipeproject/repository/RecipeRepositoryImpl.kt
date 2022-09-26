package ru.jivan.nerecipeproject.repository

import androidx.lifecycle.map
import ru.jivan.nerecipeproject.data.Recipe
import ru.jivan.nerecipeproject.data.Step
import ru.jivan.nerecipeproject.db.*
import ru.jivan.nerecipeproject.ui.RecipeContentFragment

class RecipeRepositoryImpl(private val recipeDao: RecipeDao) : RecipeRepository {

    override val data = recipeDao.getAll().map { entities ->
        entities.map { it.toRecipe() }
    }

    override fun save(recipe: Recipe) {
        if (recipe.id == RecipeContentFragment.NEW_RECIPE_ID) recipeDao.insertRecipe(recipe.toEntity())
        else recipeDao.updateContentById(
            recipe.id,
            recipe.title,
            recipe.author,
            recipe.category.categoryName,
            recipe.position
        )
    }

    override fun save(recipeId: Long, recipeStep: Step) {
        recipeDao.insertStep(recipeStep.toEntity())
    }

    override fun getSteps(recipeId: Long) =
        recipeDao.getRecipeWithSteps(recipeId).steps.map { it.toRecipeStep() }.sortedBy { it.step }

    override fun delete(recipeId: Long) = recipeDao.removeById(recipeId)

    override fun deleteStep(stepId: Long) = recipeDao.removeStepById(stepId)

    override fun search(searchQuery: String?, categoriesList: List<String>?, favorite: Boolean): List<Recipe> {
        return (if (favorite) recipeDao.searchFavorites(searchQuery, categoriesList, favorite)
        else recipeDao.search(searchQuery, categoriesList)).map { entities ->
            entities.toRecipe()
        }
    }

    override fun favorite(recipeId: Long) {
        recipeDao.favoritedById(recipeId)
    }

    override fun swapPositions(firstRecipe: Recipe, secondRecipe: Recipe) {
        recipeDao.updateContentById(
            firstRecipe.id,
            firstRecipe.title,
            firstRecipe.author,
            firstRecipe.category.categoryName,
            secondRecipe.position
        )
        recipeDao.updateContentById(
            secondRecipe.id,
            secondRecipe.title,
            secondRecipe.author,
            secondRecipe.category.categoryName,
            firstRecipe.position
        )
    }
}