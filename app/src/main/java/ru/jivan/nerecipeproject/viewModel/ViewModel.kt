package ru.jivan.nerecipeproject.viewModel

import androidx.lifecycle.AndroidViewModel
import android.app.Application
import ru.jivan.nerecipeproject.data.Recipe
import ru.jivan.nerecipeproject.db.AppDb
import ru.jivan.nerecipeproject.repository.RecipeRepository
import ru.jivan.nerecipeproject.repository.RecipeRepositoryImpl
import ru.jivan.nerecipeproject.utill.SingleLiveEvent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import ru.jivan.nerecipeproject.adapter.InteractionListener
import ru.jivan.nerecipeproject.ui.RecipeContentFragment

class ViewModel(
    application: Application
) : AndroidViewModel(application), InteractionListener {

    private val repository: RecipeRepository = RecipeRepositoryImpl(
        recipeDao = AppDb.getInstance(context = application).recipeDao
    )

    val data by repository::data

    val searchQuery = MutableLiveData("")

    val categoryList =
        MutableLiveData(Recipe.Companion.RecipeCategory.values().map { it.categoryName })

    val navigateRecipeContentScreenEvent = SingleLiveEvent<Long>()

    val navigateRecipeDetailsScreenEvent = SingleLiveEvent<Long>()

    val favoriteTab = MutableLiveData(false)

    var filteredData = Transformations.distinctUntilChanged(data) as MutableLiveData<List<Recipe>>

    fun setDefData() {
        filteredData.value = data.value
    }

    fun setQuery(query: String) {
        searchQuery.value = query
    }

    fun setCategoryList(categories: List<String>) {
        categoryList.value = categories
    }

    fun filterFavorites(index: Int) {
        favoriteTab.value = index == 1
    }

    override fun onRecipeClicked(recipe: Recipe) {
        navigateRecipeDetailsScreenEvent.value = recipe.id
    }

    override fun onRemoveClicked(recipe: Recipe) = repository.delete(recipe.id)

    override fun onEditClicked(recipe: Recipe) {
        navigateRecipeContentScreenEvent.value = recipe.id
    }

    override fun onFavoriteClicked(recipe: Recipe) = repository.favorite(recipe.id)

    fun onAddButtonClicked() {
        navigateRecipeContentScreenEvent.value = RecipeContentFragment.NEW_RECIPE_ID
    }

    fun getSteps(recipeId: Long) = repository.getSteps(recipeId)

    fun filterList(
        query: String? = searchQuery.value,
        categoriesList: List<String>? = categoryList.value,
        favorite: Boolean = favoriteTab.value == true) {
        filteredData.value = repository.search(if (query.isNullOrBlank()) "%%" else query, categoriesList, favorite)
    }

    override fun onDrag(firstRecipe: Recipe, secondRecipe: Recipe) = repository.swapPositions(firstRecipe, secondRecipe)
}