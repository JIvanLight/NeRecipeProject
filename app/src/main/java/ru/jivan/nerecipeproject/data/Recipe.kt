package ru.jivan.nerecipeproject.data

data class Recipe(
    val id: Long,
    val title: String,
    val author: String,
    val category: String,
    val position: Long,
    val favorite: Boolean = false
)