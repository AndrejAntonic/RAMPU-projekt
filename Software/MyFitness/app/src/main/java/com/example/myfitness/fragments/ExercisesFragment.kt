package com.example.myfitness.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myfitness.AddExerciseActivity
import com.example.myfitness.R


class ExercisesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exercises, container, false)
    }

    fun onAddExerciseButtonClick(view: View?) {
        val intent = Intent(activity, AddExerciseActivity::class.java)
        startActivity(intent)
    }


}