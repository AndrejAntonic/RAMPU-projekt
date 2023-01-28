package com.example.myfitness.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfitness.DataAccessObjects.ExerciseDAO
import com.example.myfitness.R
import com.example.myfitness.adapters.ExerciseRecyclerViewAdapter
import com.example.myfitness.entities.Exercise
import com.example.myfitness.helpers.AddDoneExerciseDialogHelper
import com.example.myfitness.helpers.CreateDailyWorkoutHelper
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CreateWorkoutPlanFragment : Fragment() {

    //private lateinit var recyclerView: RecyclerView
    //private val db = FirebaseFirestore.getInstance()
    //private var exercises = mutableListOf<Exercise>()




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_create_daily_workout_withbtn, container, false)


        val btnAddDoneExercise = v.findViewById<Button>(R.id.createNewPlan1)
        btnAddDoneExercise.setOnClickListener {

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

