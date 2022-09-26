package ru.jivan.nerecipeproject.adapter

import ru.jivan.nerecipeproject.data.Recipe
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.jivan.nerecipeproject.R
import ru.jivan.nerecipeproject.databinding.RecipeCardBinding

class FeedAdapter(
    private val interactionListener: InteractionListener
) : ListAdapter<Recipe, FeedAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(
        private val binding: RecipeCardBinding,
        listener: InteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var recipe: Recipe

        private val popupMenu by lazy {
            PopupMenu(itemView.context, binding.options).apply {

                inflate(R.menu.menu_toolbar)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.remove -> {
                            listener.onRemoveClicked(recipe)
                            true
                        }
                        R.id.edit -> {
                            listener.onEditClicked(recipe)
                            true
                        }
                        else -> false
                    }
                }
                setOnDismissListener { binding.options.isChecked = false }
            }
        }

        init {
            with(binding) {
                options.setOnClickListener {
                    popupMenu.show()
                }
                favorite.setOnClickListener {
                    listener.onFavoriteClicked(recipe)
                }
                recipeCard.setOnClickListener {
                    listener.onRecipeClicked(recipe)
                }
            }
        }

        fun bind(recipe: Recipe) {
            this.recipe = recipe
            with(binding) {
                titleRecipe.text = recipe.title
                authorRecipe.text = recipe.author
                categoryRecipe.text = recipe.category.localizedName
                favorite.isChecked = recipe.favorite
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecipeCardBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private object DiffCallback : DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean =
            oldItem == newItem
    }
}