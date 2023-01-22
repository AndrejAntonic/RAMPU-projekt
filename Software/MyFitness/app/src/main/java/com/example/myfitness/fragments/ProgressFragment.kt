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
import com.example.myfitness.helpers.DateHelper
import com.google.firebase.Timestamp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.myfitness.entities.DoneExercise
import java.util.*


class ProgressFragment : Fragment() {
    lateinit var exerciseGroups : Map<String, List<DoneExercise>>
    lateinit var exerciseGroupsFiltered : Map<String, List<DoneExercise>>
    lateinit var exerciseSpinner : Spinner
    lateinit var periodSpinner : Spinner
    lateinit var chartContainer : FrameLayout
    lateinit var chartFilterContainer : LinearLayout
    lateinit var noticeProgressUnavailable : LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_progress, container, false)
        exerciseSpinner = v.findViewById<Spinner>(R.id.spinner_exercise_progress)
        periodSpinner = v.findViewById<Spinner>(R.id.spinner_exercise_period)
        chartContainer = v.findViewById<FrameLayout>(R.id.chart_container)
        chartFilterContainer = v.findViewById<LinearLayout>(R.id.chart_filter_container)
        noticeProgressUnavailable = v.findViewById<LinearLayout>(R.id.notice_progress_unavailable)


        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val currentUser = UsersDAO.getCurrentUser(requireContext())
            val allUserExercises = DoneExercisesDAO.getAllDoneExercisesForUser(currentUser)

            exerciseGroups = allUserExercises.groupBy { it.exerciseName }

            val currentTimestamp = Timestamp.now()
            exerciseGroupsFiltered = exerciseGroups.mapValues { (_, exercises) ->
                filterByMonthAndYear(exercises, DateHelper.getYear(currentTimestamp)).sortedBy { it.date.toDate() }
            }

            withContext(Dispatchers.Main) {
                val exerciseNames : List<String> = getExerciseNamesWithEnoughData(exerciseGroupsFiltered)

                if (exerciseNames.isEmpty()) {
                    noticeProgressUnavailable.visibility = View.VISIBLE
                    val addDoneExerciseButton = v.findViewById<Button>(R.id.btn_add_done_exercise_progress)
                    addDoneExerciseButton.setOnClickListener {
                        val addDoneExerciseDialogHelper =
                            AddDoneExerciseDialogHelper(requireContext())

                        val scope = CoroutineScope(Dispatchers.Main)
                        scope.launch {
                            addDoneExerciseDialogHelper.load()
                        }
                    }
                    return@withContext
                } else {
                    chartFilterContainer.visibility = View.VISIBLE
                    chartContainer.visibility = View.VISIBLE
                }

                fillPeriodSpinner()
                fillExerciseSpinner(exerciseNames)
                val randomExercise : String = getRandomExerciseFrom(exerciseNames)
                setSpinnerSelection(exerciseSpinner, randomExercise)

                activateSpinnerSelectListeners()

                val chart = LineChart(requireContext(), exerciseGroupsFiltered.get(randomExercise)!!, periodSpinner.selectedItem.toString())
                chartContainer.addView(chart)
            }
        }
        return v
    }

    private fun activateSpinnerSelectListeners() {
        exerciseSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedExercise = parent?.getItemAtPosition(position) as String
                val selectedPeriod = periodSpinner.selectedItem.toString()
                val chart : LineChart
                if (selectedPeriod == "Last Month") {
                    chart = LineChart(requireContext(), getExercisesForLastMonth(selectedExercise), selectedPeriod)
                } else {
                    chart = LineChart(requireContext(), getExercisesForLastYear(selectedExercise), selectedPeriod)
                }
                chartContainer.addView(chart)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        periodSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedPeriod = parent?.getItemAtPosition(position) as String
                val selectedExercise = exerciseSpinner.selectedItem.toString()
                val chart : LineChart
                if (selectedPeriod == "Last Month") {
                    chart = LineChart(requireContext(), getExercisesForLastMonth(selectedExercise), selectedPeriod)
                } else {
                    chart = LineChart(requireContext(), getExercisesForLastYear(selectedExercise), selectedPeriod)
                }
                chartContainer.addView(chart)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
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

    fun fillPeriodSpinner() {
        val spinnerItems = arrayOf("Last Month", "Last Year")
        val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item_basic, spinnerItems)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_basic)
        periodSpinner.adapter = adapter
    }

    fun fillExerciseSpinner(spinnerItems: List<String>) {
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
        return resultData
    }

    fun getExercisesForLastYear(exerciseName: String) : List<DoneExercise> {
        val exerciseData = exerciseGroups.get(exerciseName)!!
        val currentYear = Calendar.getInstance().get(Calendar.YEAR).toString()
        val exercisesInCurrentYear = filterByMonthAndYear(exerciseData, currentYear)
        val resultData = getAverageWeightByMonth(exercisesInCurrentYear).sortedBy { it.date.toDate().month }
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