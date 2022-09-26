package ru.jivan.nerecipeproject.ui

import android.os.Bundle
import android.view.*
import ru.jivan.nerecipeproject.R
import ru.jivan.nerecipeproject.adapter.FeedAdapter
import ru.jivan.nerecipeproject.data.Recipe
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.chip.Chip
import com.google.android.material.tabs.TabLayout
import ru.jivan.nerecipeproject.databinding.FeedFragmentBinding

import ru.jivan.nerecipeproject.utill.RecipeTouchCallback
import ru.jivan.nerecipeproject.viewModel.ViewModel

class FeedFragment : Fragment(), SearchView.OnQueryTextListener {

    private val viewModel: ViewModel by viewModels(ownerProducer = ::requireParentFragment)
    private lateinit var binding: FeedFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.navigateRecipeContentScreenEvent.observe(this) { recipeId ->
            val direction = FeedFragmentDirections.toRecipeContentFragment(recipeId)
            findNavController().navigate(direction)
        }

        viewModel.navigateRecipeDetailsScreenEvent.observe(this) { recipeId ->
            val direction = FeedFragmentDirections.toRecipePartsFragment(recipeId)
            findNavController().navigate(direction)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.toolbar, menu)
                val search = menu.findItem(R.id.searchItem)
                val searchView = search.actionView as SearchView
                searchView.isSubmitButtonEnabled = true
                searchView.setOnQueryTextListener(this@FeedFragment)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == R.id.filterButton) {

                    if (menuItem.isChecked) binding.filterGroup.visibility = ViewGroup.VISIBLE
                    else binding.filterGroup.visibility = ViewGroup.GONE

                    menuItem.isChecked = !menuItem.isChecked
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FeedFragmentBinding.inflate(inflater, container, false).also { binding ->
            val tabs: TabLayout = binding.tabs
            tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab != null) {
                        viewModel.filterFavorites(tab.position)
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            })
            binding.fab.setOnClickListener {
                viewModel.onAddButtonClicked()
            }

            binding.filterGroup.forEach { child ->
                (child as? Chip)?.setOnCheckedChangeListener { _, _ ->
                    val ids = binding.filterGroup.checkedChipIds

                    val categories = mutableListOf<String>()

                    ids.forEach { id ->
                        categories.add(binding.filterGroup.findViewById<Chip>(id).text as String)
                    }

                    val categoryNames = categories.map { name ->
                        Recipe.Companion.RecipeCategory.values()
                            .find { it.localizedName == name }?.categoryName
                            ?: throw throw RuntimeException("No such category")
                    }
                    viewModel.setCategoryList(categoryNames)
                }
            }

            val adapter = FeedAdapter(viewModel)
            binding.recipeRecycleView.adapter = adapter

            val itemTouchHelper = ItemTouchHelper(RecipeTouchCallback(viewModel))
            itemTouchHelper.attachToRecyclerView(binding.recipeRecycleView)

            viewModel.filteredData.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }

            viewModel.favoriteTab.observe(viewLifecycleOwner) {
                viewModel.filterList(favorite = it)
            }

            viewModel.searchQuery.observe(viewLifecycleOwner) {
                viewModel.filterList(query = it)
            }

            viewModel.categoryList.observe(viewLifecycleOwner) {
                viewModel.filterList(categoriesList = it)
            }
        }
        return binding.root
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query.isNullOrBlank()) viewModel.setDefData()
        else {
            viewModel.setQuery("%$query%")
        }
        return true
    }
}