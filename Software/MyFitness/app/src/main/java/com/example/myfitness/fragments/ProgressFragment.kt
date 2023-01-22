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
import com.google.firebase.Timestamp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import model.DoneExercise
import java.util.*


class ProgressFragment : Fragment() {
    lateinit var exerciseGroups : Map<String, List<DoneExercise>>
    lateinit var exerciseGroupsFiltered : Map<String, List<DoneExercise>>
    lateinit var displayExerciseData : List<DoneExercise>
    lateinit var exerciseSpinner : Spinner
    lateinit var periodSpinner : Spinner
    lateinit var chartContainer : FrameLayout
//    lateinit var selectedPeriod : String
//    lateinit var selectedExercise : String

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
                val selectedExercise = parent?.getItemAtPosition(position) as String
                val chart = TestChart(requireContext(), getExercisesForLastMonth(selectedExercise), periodSpinner.selectedItem.toString())
                chartContainer.addView(chart)

                Toast.makeText(requireContext(), "Selected: $selectedExercise", Toast.LENGTH_SHORT).show()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }




        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val currentUser = UsersDAO.getCurrentUser(requireContext())
            val allUserExercises = DoneExercisesDAO.getAllDoneExercisesForUser(currentUser)

            exerciseGroups = allUserExercises.groupBy { it.exerciseName }


            exerciseGroupsFiltered = exerciseGroups.mapValues { (_, exercises) ->
                filterByMonthAndYear(exercises, "2023", "01").sortedBy { it.date.toDate() }
            }

            withContext(Dispatchers.Main) {
                val exerciseNames : List<String> = getExerciseNamesWithEnoughData(exerciseGroupsFiltered)
                fillExerciseSpinner(v, exerciseNames)
                val randomExercise : String = getRandomExerciseFrom(exerciseNames)
                setSpinnerSelection(exerciseSpinner, randomExercise)

                periodSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        val selectedPeriod = parent?.getItemAtPosition(position) as String
                        val selectedExercise = exerciseSpinner.selectedItem.toString()
                        var chart : TestChart
                        if (selectedPeriod == "Last Month") {
                            chart = TestChart(requireContext(), getExercisesForLastMonth(selectedExercise), selectedPeriod)
                        } else {
                            chart = TestChart(requireContext(), getExercisesForLastYear(selectedExercise), selectedPeriod)
                        }

                        chartContainer.addView(chart)

                        Toast.makeText(requireContext(), "Selected: $selectedPeriod", Toast.LENGTH_SHORT).show()
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }

                val chart = TestChart(requireContext(), exerciseGroupsFiltered.get(randomExercise)!!, periodSpinner.selectedItem.toString())
                chartContainer.addView(chart)
            }


        }
        // Napravit da chart prima exercise kao tip dataseta, a kao treci parametar prima po cemu ce prikazivati Y vrijednost


        return v
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

    fun getExercisesForLastMonth(exerciseName : String) : List<DoneExercise> {
        val exerciseData = exerciseGroups.get(exerciseName)!!

        val currentYear = Calendar.getInstance().get(Calendar.YEAR).toString()
        val currentMonth = DateHelper.getMonth(Timestamp.now())

        val resultData = filterByMonthAndYear(exerciseData, currentYear, currentMonth).sortedBy { it.date.toDate() }
        resultData.forEach{ println("ZAPIS " + it.exerciseName)}
        return resultData
    }

    fun getExercisesForLastYear(exerciseName: String) : List<DoneExercise> {
        val exerciseData = exerciseGroups.get(exerciseName)!!
        val currentYear = Calendar.getInstance().get(Calendar.YEAR).toString()
        val exercisesInCurrentYear = filterByMonthAndYear(exerciseData, currentYear)
        val resultData = getAverageWeightByMonth(exercisesInCurrentYear)
        return resultData
    }

    fun getAverageWeightByMonth(doneExercises: List<DoneExercise>): List<DoneExercise> {
        val groupedExercises = doneExercises.groupBy {
            "${DateHelper.getYear(it.date)}-${DateHelper.getMonth(it.date)}"
        }
        return groupedExercises.map {
            val month = it.key
            val exercises = it.value
            val averageWeight = exercises.map { it.weight }.average()
            val date = DateHelper.getFirstDayOfMonth(month)
            DoneExercise(exerciseName = "", weight = averageWeight.toInt(), sets = 0, reps = 0, date = date)
        }
    }
}