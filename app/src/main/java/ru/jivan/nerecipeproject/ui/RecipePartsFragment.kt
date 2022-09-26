package ru.jivan.nerecipeproject.ui

import ru.jivan.nerecipeproject.viewModel.ViewModel
import android.os.Bundle
import ru.jivan.nerecipeproject.R
import ru.jivan.nerecipeproject.adapter.StepsAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.jivan.nerecipeproject.databinding.RecipePartsFragmentsBinding

class RecipePartsFragment : Fragment() {
    private val recipeViewModel: ViewModel by viewModels(ownerProducer = ::requireParentFragment)
    private val args by navArgs<RecipePartsFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        recipeViewModel.navigateRecipeContentScreenEvent.observe(this) { recipeId ->
            val direction =
                RecipePartsFragmentDirections.recipePartsFragmentToRecipeContentFragment(recipeId)
            findNavController().navigate(direction)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return RecipePartsFragmentsBinding.inflate(layoutInflater, container, false)
            .also { binding ->
                recipeViewModel.data.observe(viewLifecycleOwner) { recipes ->
                    val recipe = recipes.find { it.id == args.recipeId } ?: return@observe

                    val popupMenu by lazy {
                        PopupMenu(requireContext(), binding.recipeParts.options).apply {

                            inflate(R.menu.menu_toolbar)
                            setOnMenuItemClickListener { menuItem ->
                                when (menuItem.itemId) {
                                    R.id.remove -> {
                                        recipeViewModel.onRemoveClicked(recipe)
                                        findNavController().popBackStack()
                                        true
                                    }
                                    R.id.edit -> {
                                        recipeViewModel.onEditClicked(recipe)
                                        true
                                    }
                                    else -> false
                                }
                            }
                            setOnDismissListener { binding.recipeParts.options.isChecked = false }
                        }
                    }

                    with(binding.recipeParts) {
                        titleRecipe.text = recipe.title
                        authorRecipe.text = recipe.author
                        favorite.isChecked = recipe.favorite
                        categoryRecipe.text = recipe.category.localizedName

                        options.setOnClickListener {
                            popupMenu.show()
                        }
                        favorite.setOnClickListener {
                            recipeViewModel.onFavoriteClicked(recipe)
                        }
                        recipeCard.setOnClickListener {
                            recipeViewModel.onRecipeClicked(recipe)
                        }
                    }

                    val stepsAdapter = StepsAdapter()
                    binding.stepsRecycleView.adapter = stepsAdapter
                    stepsAdapter.submitList(recipeViewModel.getSteps(args.recipeId))
                }
            }.root
    }
}