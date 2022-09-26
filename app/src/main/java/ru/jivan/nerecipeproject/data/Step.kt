package ru.jivan.nerecipeproject.data

data class Step(
    var id: Long,
    val recipeId: Long,
    var step: Long=1,
    var title: String?,
    var text: String?,
    var image: String? = null
)