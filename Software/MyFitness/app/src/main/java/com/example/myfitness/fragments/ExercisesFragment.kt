package com.example.myfitness.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.myfitness.AddExerciseFragment
import com.example.myfitness.R


class ExercisesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_exercises, container, false)

        val button = view.findViewById<Button>(R.id.addExerciseButton)
        button.setOnClickListener{
            val frgmntAddExercise = AddExerciseFragment()
            val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.mainlayout, frgmntAddExercise)
            transaction.commit()
        }
        return view
    }
}