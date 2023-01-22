package com.example.myfitness.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.myfitness.DataAccessObjects.DoneExercisesDAO
import com.example.myfitness.DataAccessObjects.UsersDAO
import com.example.myfitness.R
import com.example.myfitness.charts.LineChart
import com.example.myfitness.helpers.AddDoneExerciseDialogHelper
import com.example.myfitness.helpers.CreateWorkoutDialogHelper
import com.example.myfitness.helpers.DateHelper
import com.google.firebase.Timestamp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import model.DoneExercise
import java.util.*


class CreateWorkoutFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.self_created_workout, container, false)

        /*val addDoneExerciseDialogHelper =
            CreateWorkoutDialogHelper(requireContext())

        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch {
            addDoneExerciseDialogHelper.load()
        }*/



        return v
    }
}