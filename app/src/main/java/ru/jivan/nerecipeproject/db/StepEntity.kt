package ru.jivan.nerecipeproject.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "recipeSteps",
    foreignKeys = [ForeignKey(
        entity = RecipeEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("recipeId"),
        onDelete = ForeignKey.CASCADE
    )]
)
class StepEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "recipeId")
    val recipeId: Long,
    @ColumnInfo(name = "step")
    val step: Long = 1,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "text")
    val text: String,
    @ColumnInfo(name = "image")
    val image: String? = null
)