package ru.jivan.nerecipeproject.adapter

import ru.jivan.nerecipeproject.data.Step

interface EditInteractionListener {
    fun onImageSelectClicked(position:Long)
    fun onSwipe(position: Long)
    fun onDrag(steps:List<Step>)
}