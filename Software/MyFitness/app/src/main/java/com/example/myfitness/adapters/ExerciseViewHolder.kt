package com.example.myfitness.adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myfitness.R
import model.Exercise

class ExerciseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val exerciseName: TextView
    private val exerciseDescription: TextView
    init {
        exerciseName = view.findViewById(R.id.naziv_vjezbe_rv)
        exerciseDescription = view.findViewById(R.id.opis_vjezbe_rv)
    }

    fun bind(exercise: Exercise) {
        exerciseName.text = exercise.name
        exerciseDescription.text = exercise.description
    }
}


