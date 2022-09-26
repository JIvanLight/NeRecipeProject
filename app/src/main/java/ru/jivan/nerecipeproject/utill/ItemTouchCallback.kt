package ru.jivan.nerecipeproject.utill

import ru.jivan.nerecipeproject.adapter.EditInteractionListener
import androidx.recyclerview.widget.RecyclerView
import ru.jivan.nerecipeproject.adapter.RecipeStepsEditAdapter
import androidx.recyclerview.widget.ItemTouchHelper

class ItemTouchCallback(private val listener: EditInteractionListener) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags( dragFlags, swipeFlags )
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val from = viewHolder.adapterPosition
        val to = target.adapterPosition

        val adapter = recyclerView.adapter as RecipeStepsEditAdapter
        val list = adapter.currentList.toMutableList()

        list[from] = list[to].copy(step = list[from].step )
            .also { list[to] = list[from].copy(step = list[to].step) }

        listener.onDrag(list)

        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        listener.onSwipe(viewHolder.adapterPosition.toLong())
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        viewHolder?.itemView?.alpha = 0.4f
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        viewHolder.itemView.alpha = 1.0f
    }
}