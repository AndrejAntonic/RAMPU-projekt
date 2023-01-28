package com.example.myfitness.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.myfitness.DataAccessObjects.DailyPlanDAO
import com.example.myfitness.R
import com.example.myfitness.entities.DoneExercise
import com.example.myfitness.helpers.CreateDailyWorkoutHelper
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CreateWorkoutPlanFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_create_daily_workout_withbtn, container, false)



        val btnOpenPlanCreation = v.findViewById<Button>(R.id.createNewPlan1)
        btnOpenPlanCreation.setOnClickListener {

            val CreateDailyWorkoutHelper =
                CreateDailyWorkoutHelper(requireContext())

            val scope = CoroutineScope(Dispatchers.Main)
            scope.launch {
                CreateDailyWorkoutHelper.load()
            }
        }


        return v
    }
}

