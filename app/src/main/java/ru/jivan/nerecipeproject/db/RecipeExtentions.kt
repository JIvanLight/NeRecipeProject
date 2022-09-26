package ru.jivan.nerecipeproject.db

import ru.jivan.nerecipeproject.data.Recipe
import ru.jivan.nerecipeproject.data.Step

fun RecipeEntity.toRecipe() = Recipe(
    id = id,
    title = title,
    author = author,
    category = Recipe.Companion.RecipeCategory.values().find { it.categoryName == category }
        ?: throw RuntimeException("No such category"),
    position = position,
    favorite = favorite
)

fun Recipe.toEntity() = RecipeEntity(
    id = id,
    title = title,
    author = author,
    category = category.categoryName,
    position = position,
    favorite = favorite
)

fun StepEntity.toRecipeStep() = Step(
    id = id,
    recipeId = recipeId,
    step = step,
    title = title,
    text = text,
    image = image
)

fun Step.toEntity() = StepEntity(
    id = id,
    recipeId = recipeId,
    step = step,
    title = title ?: "",
    text = text ?: "",
    image = image
)