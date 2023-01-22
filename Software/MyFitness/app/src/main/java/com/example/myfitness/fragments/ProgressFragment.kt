package com.example.myfitness.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.Spinner
import com.example.myfitness.DataAccessObjects.DoneExercisesDAO
import com.example.myfitness.DataAccessObjects.UsersDAO
import com.example.myfitness.R
import com.example.myfitness.charts.TestChart
import com.example.myfitness.helpers.DateHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import model.DoneExercise


class ProgressFragment : Fragment() {
    lateinit var exerciseGroups : Map<String, List<DoneExercise>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_progress, container, false)

        fillPeriodSpinner(v)

        val chartContainer = v.findViewById<FrameLayout>(R.id.chart_container)

        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val currentUser = UsersDAO.getCurrentUser(requireContext())
            println("GETTING DATA FOR USER " + currentUser)
            val allUserExercises = DoneExercisesDAO.getAllDoneExercisesForUser(currentUser)


            exerciseGroups = allUserExercises.groupBy { it.exerciseName }
            println("BEFORE")
            exerciseGroups.values.forEach{ println(it[0].exerciseName + " " + it.size) }

            val exerciseGroupsFiltered = exerciseGroups.mapValues { (_, exercises) ->
                filterByMonthAndYear(exercises, "01", "2023").sortedBy { it.date.toDate() }
            }

            withContext(Dispatchers.Main) {
                val exerciseNames : List<String> = getExerciseNamesWithEnoughData(exerciseGroupsFiltered)
                fillExerciseSpinner(v, exerciseNames)
            }

            println("AFTER")
            exerciseGroupsFiltered.values.forEach{ println(it[0].exerciseName + " " + it.size) }

            withContext(Dispatchers.Main) {
                val chart = TestChart(requireContext(), exerciseGroupsFiltered.get("Back Squat")!!)
                chartContainer.addView(chart)
            }
        }


        // Napravit da chart prima exercise kao tip dataseta, a kao treci parametar prima po cemu ce prikazivati Y vrijednost


        return v
    }


    fun getExercisesForMonth(exercises : List<DoneExercise>, wantedMonth : String) : List<DoneExercise> {
        return exercises.filter {
            DateHelper.getMonth(it.date) == wantedMonth
        }
    }

    fun filterByExercise(exercises: List<DoneExercise>, wantedExercise: String) : List<DoneExercise> {
        return exercises.filter {
            it.exerciseName == wantedExercise
        }
    }

    fun filterByMonthAndYear(doneExercises: List<DoneExercise>, month: String, year: String) : List<DoneExercise> {
        val filteredExercises = doneExercises.filter {
            DateHelper.getMonth(it.date) == month && DateHelper.getYear(it.date) == year
        }
        return filteredExercises
    }


    fun fillPeriodSpinner(view : View) {
        val spinner = view.findViewById<Spinner>(R.id.spinner_exercise_period)
        val spinnerItems = arrayOf("Last Month", "Last Year")
        val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item_basic, spinnerItems)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_basic)
        spinner.adapter = adapter
    }

    fun fillExerciseSpinner(view : View, spinnerItems: List<String>) {
        val spinner = view.findViewById<Spinner>(R.id.spinner_exercise_progress)
        val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item_basic, spinnerItems)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_basic)
        spinner.adapter = adapter
    }

    fun getExerciseNamesWithEnoughData(exerciseGroups: Map<String, List<DoneExercise>>): List<String> {
        return exerciseGroups.filter { (_, exercises) -> exercises.size >= 4 }.keys.toList()
    }
}