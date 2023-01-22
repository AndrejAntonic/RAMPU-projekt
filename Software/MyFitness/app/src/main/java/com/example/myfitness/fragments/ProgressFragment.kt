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
import com.example.myfitness.charts.TestChart
import com.example.myfitness.helpers.DateHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import model.DoneExercise


class ProgressFragment : Fragment() {
    lateinit var exerciseGroups : Map<String, List<DoneExercise>>
    lateinit var exerciseGroupsFiltered : Map<String, List<DoneExercise>>
    lateinit var exerciseSpinner : Spinner
    lateinit var periodSpinner : Spinner
    lateinit var chartContainer : FrameLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_progress, container, false)
        exerciseSpinner = v.findViewById<Spinner>(R.id.spinner_exercise_progress)
        periodSpinner = v.findViewById<Spinner>(R.id.spinner_exercise_period)
        chartContainer = v.findViewById<FrameLayout>(R.id.chart_container)

        fillPeriodSpinner(v)

        exerciseSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Handle the selected item here
                val selectedItem = parent?.getItemAtPosition(position) as String
                // do something with the selected item, for example
                val chart = TestChart(requireContext(), exerciseGroupsFiltered.get(selectedItem.toString())!!)
                chartContainer.addView(chart)

                Toast.makeText(requireContext(), "Selected: $selectedItem", Toast.LENGTH_SHORT).show()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }



        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val currentUser = UsersDAO.getCurrentUser(requireContext())
            println("GETTING DATA FOR USER " + currentUser)
            val allUserExercises = DoneExercisesDAO.getAllDoneExercisesForUser(currentUser)


            exerciseGroups = allUserExercises.groupBy { it.exerciseName }

            exerciseGroupsFiltered = exerciseGroups.mapValues { (_, exercises) ->
                filterByMonthAndYear(exercises, "2023").sortedBy { it.date.toDate() }
            }

            withContext(Dispatchers.Main) {
                val exerciseNames : List<String> = getExerciseNamesWithEnoughData(exerciseGroupsFiltered)
                fillExerciseSpinner(v, exerciseNames)
                val randomExercise : String = getRandomExerciseFrom(exerciseNames)
                setSpinnerSelection(exerciseSpinner, randomExercise)

                val chart = TestChart(requireContext(), exerciseGroupsFiltered.get(randomExercise)!!)
                chartContainer.addView(chart)
            }

            withContext(Dispatchers.Main) {

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

    fun filterByMonthAndYear(doneExercises: List<DoneExercise>, year: String, month: String? = null) : List<DoneExercise> {
        val filteredExercises = doneExercises.filter {
            if (month != null) {
                DateHelper.getMonth(it.date) == month && DateHelper.getYear(it.date) == year
            } else {
                DateHelper.getYear(it.date) == year
            }
        }
        return filteredExercises
    }


    fun fillPeriodSpinner(view : View) {
        val spinner = view.findViewById<Spinner>(R.id.spinner_exercise_period)
        val spinnerItems = arrayOf("Last Month", "Last Year")
        val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item_basic, spinnerItems)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_basic)
        periodSpinner.adapter = adapter
    }

    fun fillExerciseSpinner(view : View, spinnerItems: List<String>) {
        val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item_basic, spinnerItems)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_basic)
        exerciseSpinner.adapter = adapter
    }

    fun getExerciseNamesWithEnoughData(exerciseGroups: Map<String, List<DoneExercise>>): List<String> {
        return exerciseGroups.filter { (_, exercises) -> exercises.size >= 4 }.keys.toList()
    }

    fun getRandomExerciseFrom(exerciseNames : List<String>) : String {
        val randomIndex = (0 until exerciseNames.size).random()
        return exerciseNames[randomIndex]
    }

    fun setSpinnerSelection(spinner : Spinner, toSelect : String) {
        val spinnerAdapter = spinner.adapter as ArrayAdapter<String>
        val valueIndex = spinnerAdapter.getPosition(toSelect)
        spinner.setSelection(valueIndex)
    }


}