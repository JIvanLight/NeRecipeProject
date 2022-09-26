package ru.jivan.nerecipeproject.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [RecipeEntity::class, StepEntity::class],
    version = 2
)

abstract class AppDb : RoomDatabase() {

    abstract val recipeDao: RecipeDao

    companion object {
        @Volatile
        private var instance: AppDb? = null

        fun getInstance(context: Context): AppDb {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDb {
            return Room.databaseBuilder(
                context, AppDb::class.java, "app.db"
            ).allowMainThreadQueries().build()
        }
    }
}