package com.example.myfitness.helpers

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfitness.DataAccessObjects.CreatedWorkoutDAO
import com.example.myfitness.DataAccessObjects.DoneExercisesDAO
import com.example.myfitness.DataAccessObjects.ExerciseDAO
import com.example.myfitness.DataAccessObjects.UsersDAO
import com.example.myfitness.R
import com.example.myfitness.adapters.ExerciseListRecyclerViewAdapter
import com.google.firebase.Timestamp
import model.CreatedWorkout
import java.text.SimpleDateFormat
import java.util.*

class CreateWorkoutDialogHelper(private val context: Context) {
    private val dialog : AlertDialog
    private val dialogView : View
    private val searchExerciseEditText1 : EditText
    private val recycleView : RecyclerView
    private var selectedExercise : String = ""
    private val saveButton : Button
    private val cancelButton : Button
    private var allExerciseNames : MutableList<String> = mutableListOf()
    private val dateinput : EditText
    private val selectedDateTime : Calendar
    private val sdfDate : SimpleDateFormat

    init {
        dialogView = LayoutInflater
            .from(context)
            .inflate(R.layout.self_created_workout, null)
        dialog = AlertDialog.Builder(context)
            .setView(dialogView).show()

        searchExerciseEditText1 = dialog.findViewById(R.id.exercisePicker1)

        recycleView = dialog.findViewById(R.id.rv_exercise_picker)
        selectedExercise = ""
        saveButton = dialog.findViewById(R.id.btn_save_doneexercise_dialog)
        cancelButton = dialog.findViewById(R.id.btn_cancel_doneexercise_dialog)
        allExerciseNames = mutableListOf()
        dateinput = dialog.findViewById(R.id.dateInput_dialog)
        selectedDateTime = Calendar.getInstance()
        sdfDate = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH)
    }

    suspend fun load() {

        allExerciseNames = ExerciseDAO.getAllExerciseNames()
        recycleView.layoutManager = LinearLayoutManager(context)
        val adapter = ExerciseListRecyclerViewAdapter(allExerciseNames)
        recycleView.adapter = adapter

        adapter.setOnItemClickListener(object : ExerciseListRecyclerViewAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, item: String) {
                selectedExercise = item
                searchExerciseEditText1.setText(selectedExercise)
                recycleView.visibility = View.GONE
                searchExerciseEditText1.error = null
            }
        })

        searchExerciseEditText1.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                recycleView.visibility = View.VISIBLE
            } else {
                recycleView.visibility = View.GONE
            }
        }

        searchExerciseEditText1.addTextChangedListener {
            if (it.toString().isNotEmpty()) {
                recycleView.visibility = View.VISIBLE
                adapter.filter(it.toString())
            } else {
                recycleView.visibility = View.GONE
            }
        }


        saveButton.setOnClickListener {
            val inputValid = validateInput()
            if (!inputValid) {
                return@setOnClickListener
            }

            val currentUser = UsersDAO.getCurrentUser(context)
            val workout : CreatedWorkout = buildExercise()
            CreatedWorkoutDAO.add(workout, currentUser)
            dialog.dismiss()
            Toast.makeText(context, "Napravljena vježba spremljena", Toast.LENGTH_SHORT).show()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        activateDateTimeListeners()
    }

    fun activateDateTimeListeners() {
        dateinput.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                DatePickerDialog(
                    view.context,
                    { _, year, monthOfYear, dayOfMonth ->
                        selectedDateTime.set(year, monthOfYear, dayOfMonth)
                        dateinput.setText(sdfDate.format(selectedDateTime.time).toString())
                    },
                    selectedDateTime.get(Calendar.YEAR),
                    selectedDateTime.get(Calendar.MONTH),
                    selectedDateTime.get(Calendar.DAY_OF_MONTH)
                ).show()
                view.clearFocus()
                dateinput.error = null
            }
        }
    }

    private fun validateInput() : Boolean {
        var allValid = true
        if (selectedExercise.length == 0) {
            searchExerciseEditText1.error = "Potrebno odabrati vježbu!"
            allValid = false
        }
       /* if (weightInput.text.length == 0) {
            weightInput.error = "Potrebno unijeti kilažu!"
            allValid = false
        }
        if (setsInput.text.length == 0) {
            setsInput.error = "Potrebno unijeti broj serija!"
            allValid = false
        }
        if (repsInput.text.length == 0) {
            repsInput.error = "Potrebno unijeti broj ponavljanja!"
            allValid = false
        }
        if (dateinput.text.length == 0) {
            dateinput.error = "Potrebno odabrati datum!"
            allValid = false
        }*/
        return allValid
    }

    fun buildExercise() : CreatedWorkout {
        return CreatedWorkout(
            selectedExercise,
            Timestamp(selectedDateTime.time)
        )
    }
}