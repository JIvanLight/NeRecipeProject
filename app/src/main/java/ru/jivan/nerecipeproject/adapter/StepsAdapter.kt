package ru.jivan.nerecipeproject.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import ru.jivan.nerecipeproject.data.Step
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.jivan.nerecipeproject.R
import ru.jivan.nerecipeproject.databinding.RecipeStepBinding
import ru.jivan.nerecipeproject.utill.App

class StepsAdapter : ListAdapter<Step, StepsAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(
        private val binding: RecipeStepBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(recipeStep: Step) {
            with(binding) {
                stepNumber.text =
                    App.getAppResources().getString(R.string.step_number, recipeStep.step + 1)
                stepTitle.text = recipeStep.title
                stepText.text = recipeStep.text
                if (recipeStep.image != null) stepImage.setImageURI(Uri.parse(recipeStep.image))
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecipeStepBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private object DiffCallback : DiffUtil.ItemCallback<Step>() {
        override fun areItemsTheSame(oldItem: Step, newItem: Step): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Step, newItem: Step): Boolean =
            oldItem == newItem
    }
}