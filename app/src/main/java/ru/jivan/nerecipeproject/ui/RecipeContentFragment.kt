package ru.jivan.nerecipeproject.ui

import ru.jivan.nerecipeproject.R
import ru.jivan.nerecipeproject.adapter.RecipeStepsEditAdapter
import ru.jivan.nerecipeproject.utill.ItemTouchCallback
import ru.jivan.nerecipeproject.viewModel.EditViewModel
import ru.jivan.nerecipeproject.data.Recipe
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.net.Uri
import android.content.Intent
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.launch
import androidx.navigation.fragment.findNavController
import androidx.core.content.edit
import androidx.fragment.app.viewModels
import kotlin.properties.Delegates
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import ru.jivan.nerecipeproject.databinding.RecipeContentFragmentBinding

class RecipeContentFragment : Fragment() {

    private val viewModel: EditViewModel by viewModels()
    private val args by navArgs<RecipeContentFragmentArgs>()
    private val recipeId by lazy { args.recipeId }

    private val prefs by lazy {
        requireContext().getSharedPreferences(
            "recipeIds", Context.MODE_PRIVATE
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = RecipeContentFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        val items = Recipe.Companion.RecipeCategory.values().map { it.localizedName }
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.items, items)
        binding.categoryItem.setAdapter(arrayAdapter)

        val recycleViewAdapter = RecipeStepsEditAdapter(viewModel)
        binding.recycleRecipeEdit.adapter = recycleViewAdapter

        val itemTouchHelper = ItemTouchHelper(ItemTouchCallback(viewModel))
        itemTouchHelper.attachToRecyclerView(binding.recycleRecipeEdit)

        viewModel.steps.observe(viewLifecycleOwner) { steps ->
            recycleViewAdapter.submitList(steps)
        }

        var newRecipeId: Long by Delegates.observable(
            prefs.getLong(NEXT_ID_RECIPE_KEY, 1L)
        ) { _, _, newValue ->
            prefs.edit { putLong(NEXT_ID_RECIPE_KEY, newValue) }
        }

        viewModel.data.observe(viewLifecycleOwner) {
            val recipe = viewModel.getRecipe(recipeId)

            if (recipe != null) {
                with(binding) {
                    recipeTitleEdit.setText(recipe.title)
                    recipeAuthorEdit.setText(recipe.author)
                    categoryItem.setText(recipe.category.localizedName, false)
                    viewModel.getSteps(recipeId)
                }
            } else {
                binding.newStepButton.callOnClick()
            }
        }

        binding.newStepButton.setOnClickListener {
            viewModel.onAddNewStepClicked(
                newStepId++,
                if (recipeId != NEW_RECIPE_ID) recipeId else newRecipeId,
                recycleViewAdapter.itemCount.toLong()
            )
        }

        binding.saveButton.setOnClickListener {
            val category = Recipe.Companion.RecipeCategory.values()
                .find { it.localizedName == binding.categoryItem.text.toString() }
            val recipe = viewModel.getRecipe(recipeId)
            if (
                viewModel.onSaveButtonClicked(
                    binding.recipeTitleEdit.text.toString(),
                    binding.recipeAuthorEdit.text.toString(),
                    category,
                    if (recipeId == NEW_RECIPE_ID) newRecipeId else recipe?.position!!
                )
            ) {
                if (recipeId == NEW_RECIPE_ID) newRecipeId++
                findNavController().popBackStack()
            }
        }

        val selectImageActivityLauncher = registerForActivityResult(
            ResultContract
        ) { uri ->
            uri ?: return@registerForActivityResult
            requireContext().contentResolver.takePersistableUriPermission(uri
                ,  Intent.FLAG_GRANT_READ_URI_PERMISSION
                        + Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
            viewModel.setImage(uri)
        }
        viewModel.selectImageEvent.observe(viewLifecycleOwner) {
            binding.recycleRecipeEdit.clearFocus()
            selectImageActivityLauncher.launch()
        }
    }.root

    object ResultContract : ActivityResultContract<Unit, Uri?>() {
        override fun createIntent(context: Context, input: Unit) =
            Intent(
                Intent.ACTION_OPEN_DOCUMENT,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )

        override fun parseResult(resultCode: Int, intent: Intent?) =
            if (resultCode == Activity.RESULT_OK) {
                intent?.data
            } else null
    }

    companion object {
        var newStepId: Long = 0
        const val NEXT_ID_RECIPE_KEY = "nextId"
        const val NEW_RECIPE_ID = 0L
    }
}