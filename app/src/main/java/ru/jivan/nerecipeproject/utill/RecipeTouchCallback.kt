package ru.jivan.nerecipeproject.utill

import ru.jivan.nerecipeproject.adapter.FeedAdapter
import ru.jivan.nerecipeproject.adapter.InteractionListener
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class RecipeTouchCallback(private val listener: InteractionListener) :
    ItemTouchHelper.Callback() {

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = 0
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val from = viewHolder.adapterPosition
        val to = target.adapterPosition

        val adapter = recyclerView.adapter as FeedAdapter
        val list = adapter.currentList.toMutableList()

        listener.onDrag(list[from], list[to])

        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        viewHolder?.itemView?.alpha = 0.4f
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        viewHolder.itemView.alpha = 1.0f
    }
}