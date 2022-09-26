package ru.jivan.nerecipeproject.viewModel

import ru.jivan.nerecipeproject.data.Recipe
import ru.jivan.nerecipeproject.data.Step
import ru.jivan.nerecipeproject.db.AppDb
import ru.jivan.nerecipeproject.ui.RecipeContentFragment
import ru.jivan.nerecipeproject.repository.RecipeRepository
import ru.jivan.nerecipeproject.repository.RecipeRepositoryImpl
import ru.jivan.nerecipeproject.utill.SingleLiveEvent
import android.app.Application
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.jivan.nerecipeproject.R
import ru.jivan.nerecipeproject.adapter.EditInteractionListener

class EditViewModel(
    application: Application
) : AndroidViewModel(application), EditInteractionListener {

    private val repository: RecipeRepository = RecipeRepositoryImpl(
        recipeDao = AppDb.getInstance(context = application).recipeDao
    )

    val app = application

    val data by repository::data

    val steps = MutableLiveData<List<Step>>(null)

    val selectImageEvent = SingleLiveEvent<Int>()

    private var currentRecipe: Recipe? = null

    private var currentPosition: Long? = null

    override fun onImageSelectClicked(position: Long) {
        currentPosition = position
        selectImageEvent.call()
    }

    fun onSaveButtonClicked(
        title: String,
        author: String,
        category: Recipe.Companion.RecipeCategory?,
        position: Long
    ): Boolean {
        if (title.isBlank() || author.isBlank()) {
            Toast.makeText(
                getApplication(),
                app.getString(R.string.empty_fields),
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        if (category == null) {
            Toast.makeText(
                getApplication(),
                app.getString(R.string.choose_category),
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        if (!checkStepsFieldsNotNull()) {
            Toast.makeText(
                getApplication(),
                app.getString(R.string.empty_fields),
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        val recipe = currentRecipe?.copy(
            title = title,
            author = author,
            category = category,
            position = position
        ) ?: Recipe(RecipeContentFragment.NEW_RECIPE_ID, title, author, category, position)

        repository.save(recipe)
        checkNotNull(steps.value?.forEach {
            repository.save(recipe.id, it)
        }) { "The value cannot be null" }

        currentRecipe = null
        steps.value = emptyList()
        currentPosition = null

        return true
    }

    fun onAddNewStepClicked(globalId: Long, recipeId: Long, position: Long) {

        val newStep = listOf(
            Step(
                id = globalId,
                recipeId = recipeId,
                step = position,
                title = null,
                text = null
            )
        )

        steps.value = steps.value?.plus(newStep) ?: newStep
    }

    private fun checkStepsFieldsNotNull(): Boolean {
        var result = true
        if (steps.value.isNullOrEmpty()) result = false
        else {
            checkNotNull(steps.value!!.forEach {
                if (it.title == null || it.text == null) result = false
            }) { "The value cannot be null" }
        }
        return result
    }

    override fun onSwipe(position: Long) {
        if (checkNotNull(steps.value?.count()) { "The value cannot be null" } > 1) {
            steps.value = steps.value?.filter { it.step != position }
                ?.map { if (it.step > position) it.copy(step = it.step - 1) else it }
        } else
            Toast.makeText(getApplication(), "Can't delete last step!", Toast.LENGTH_LONG).show()
    }

    override fun onDrag(steps: List<Step>) {
        this.steps.value = steps
    }

    fun setImage(uri: Uri) {
        steps.value =
            steps.value?.map { if (it.step == currentPosition) it.copy(image = uri.toString()) else it }
    }

    fun getSteps(recipeId: Long) {
        steps.value = repository.getSteps(recipeId)
    }

    fun getRecipe(recipeId: Long): Recipe? {
        currentRecipe = data.value?.find { it.id == recipeId }
        return currentRecipe
    }
}