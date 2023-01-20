package com.example.myfitness.helpers

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.myfitness.DataAccessObjects.DoneExercisesDAO
import com.example.myfitness.DataAccessObjects.ExerciseDAO
import com.example.myfitness.R
import model.DoneExercise
import model.Exercise
import java.text.SimpleDateFormat
import java.util.*

class AddDoneExerciseDialogHelper(private val dialog: AlertDialog, private val context: Context) {

    val searchExerciseEditText : EditText = dialog.findViewById(R.id.exercisePicker)
    val searchListView : ListView = dialog.findViewById(R.id.listview_exercises)
    var selectedExercise : String = ""
    val saveButton : Button = dialog.findViewById(R.id.btn_save_doneexercise_dialog)
    val cancelButton : Button = dialog.findViewById(R.id.btn_cancel_doneexercise_dialog)

    var allExerciseNames : MutableList<String> = mutableListOf()

    val setsInput : EditText = dialog.findViewById(R.id.setsInput)
    val repsInput : EditText = dialog.findViewById(R.id.repsInput)
    val weightInput : EditText = dialog.findViewById(R.id.weightInput)
    val dateinput : EditText = dialog.findViewById(R.id.dateInput_dialog)
    val selectedDateTime : Calendar = Calendar.getInstance()
    val sdfDate = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH)

    suspend fun load() {

        var arrayAdapter : ArrayAdapter<*> = ArrayAdapter(context, R.layout.listview_item, allExerciseNames)

        allExerciseNames = ExerciseDAO.getAllExerciseNames()

        arrayAdapter = ArrayAdapter(context, R.layout.listview_item, allExerciseNames)
        searchListView.adapter = arrayAdapter


        searchExerciseEditText.addTextChangedListener {
            println()
            if (it.toString().isNotEmpty()) {
                searchListView.visibility = View.VISIBLE
                arrayAdapter.filter.filter(it.toString())

            } else {
                searchListView.visibility = View.GONE
            }
        }

        searchListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            // This is your listview's selected item
            selectedExercise = parent.getItemAtPosition(position) as String
            println(selectedExercise)
            searchExerciseEditText.setText(selectedExercise)
            searchListView.visibility = View.GONE
        }


        saveButton.setOnClickListener {
            val inputValid = validateInput()
            if (!inputValid) {
                return@setOnClickListener
            }

            val exercise : DoneExercise = buildExercise()
            DoneExercisesDAO.add(exercise)
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
                dateinput.setError(null)
            }
        }
    }

    private fun validateInput() : Boolean {
        var allValid = true
        if (selectedExercise.length == 0) {
            searchExerciseEditText.setError("Potrebno odabrati vježbu!")
            allValid = false
        }
        if (weightInput.text.length == 0) {
            weightInput.setError("Potrebno unijeti kilažu!")
            allValid = false
        }
        if (setsInput.text.length == 0) {
            setsInput.setError("Potrebno unijeti broj serija!")
            allValid = false
        }
        if (repsInput.text.length == 0) {
            repsInput.setError("Potrebno unijeti broj ponavljanja!")
            allValid = false
        }
        if (dateinput.text.length == 0) {
            dateinput.setError("Potrebno odabrati datum!")
            allValid = false
        }
        return allValid
    }

    fun buildExercise() : DoneExercise {
        val prefs = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        val username = prefs.getString("username", "")!!

        return DoneExercise(
            selectedExercise,
            weightInput.text.toString().toInt(),
            setsInput.text.toString().toInt(),
            repsInput.text.toString().toInt(),
            selectedDateTime.time,
            username
        )
    }
}