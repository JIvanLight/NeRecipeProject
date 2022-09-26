package ru.jivan.nerecipeproject.data

import ru.jivan.nerecipeproject.R
import ru.jivan.nerecipeproject.utill.App

data class Recipe(
    val id: Long,
    val title: String,
    val author: String,
    val category: RecipeCategory,
    val position: Long,
    val favorite: Boolean = false
) {
    companion object {
        enum class RecipeCategory(val categoryName: String, val localizedName: String) {
            European(
                "European",
                App.getAppResources().getString(R.string.european)
            ),
            Asian(
                "Asian",
                App.getAppResources().getString(R.string.asian)
            ),
            PanAsian(
                "PanAsian",
                App.getAppResources().getString(R.string.pan_asian)
            ),
            Eastern(
                "Eastern",
                App.getAppResources().getString(R.string.eastern)
            ),
            American(
                "American",
                App.getAppResources().getString(R.string.american)
            ),
            Russian(
                "Russian",
                App.getAppResources().getString(R.string.russian)
            ),
            Mediterranean(
                "Mediterranean",
                App.getAppResources().getString(R.string.mediterranean)
            )
        }
    }
}