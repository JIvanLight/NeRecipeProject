package ru.jivan.nerecipeproject.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ru.jivan.nerecipeproject.repository.RecipeRepository

class RecipeViewModel (application: Application) : AndroidViewModel(application) {

    private val repository: RecipeRepository

}