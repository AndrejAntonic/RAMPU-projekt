package com.example.myfitness.adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myfitness.R
import model.Exercise

class ExerciseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val name: TextView
    private val description: TextView
    private val difficulty : TextView
    init {
        name = view.findViewById(R.id.naziv_vjezbe_rv)
        description = view.findViewById(R.id.opis_vjezbe_rv)
        difficulty = view.findViewById((R.id.difficulty_rv))
    }

    fun bind(exercise: Exercise) {
        name.text = exercise.name
        description.text = "Opis: " + exercise.description
        difficulty.text = "Težina:" + exercise.difficulty.toString()
    }
}


