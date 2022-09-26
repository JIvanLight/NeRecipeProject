package ru.jivan.nerecipeproject.adapter

import android.net.Uri
import android.view.LayoutInflater
import ru.jivan.nerecipeproject.data.Step
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.jivan.nerecipeproject.R
import ru.jivan.nerecipeproject.databinding.RecipeEditStepBinding
import ru.jivan.nerecipeproject.utill.App

class RecipeStepsEditAdapter(
    private val interactionListener: EditInteractionListener
) : ListAdapter<Step, RecipeStepsEditAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(
        private val binding: RecipeEditStepBinding,
        private val listener: EditInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.selectImageButton.setOnClickListener {
                listener.onImageSelectClicked(adapterPosition.toLong())
            }
        }

        fun bind(recipeStep: Step) {
            with(binding) {
                stepNumber.text =
                    App.getAppResources().getString(R.string.step_number, recipeStep.step + 1)

                stepTitleEdit.setText(recipeStep.title)

                binding.stepTitleEdit.addTextChangedListener {
                    recipeStep.title = it.toString()
                }
                stepTextEdit.setText(recipeStep.text)
                stepTextEdit.addTextChangedListener {
                    recipeStep.text = it.toString()
                }
                if (recipeStep.image != null) stepImage.setImageURI(Uri.parse(recipeStep.image))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecipeEditStepBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, interactionListener)
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