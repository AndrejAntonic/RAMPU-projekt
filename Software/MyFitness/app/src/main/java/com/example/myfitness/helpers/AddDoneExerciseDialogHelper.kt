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
import androidx.core.widget.addTextChangedListener
import com.example.myfitness.R
import java.text.SimpleDateFormat
import java.util.*

class AddDoneExerciseDialogHelper(private val dialog: AlertDialog, private val context: Context) {

    val searchExerciseEditText : EditText = dialog.findViewById(R.id.exercisePicker)
    val searchListView : ListView = dialog.findViewById(R.id.listview_exercises)
    val saveButton : Button = dialog.findViewById(R.id.btn_save_doneexercise_dialog)
    val cancelButton : Button = dialog.findViewById(R.id.btn_cancel_doneexercise_dialog)

    val setsInput : EditText = dialog.findViewById(R.id.setsInput)
    val repsInput : EditText = dialog.findViewById(R.id.repsInput)
    val weightInput : EditText = dialog.findViewById(R.id.weightInput)
    val dateinput : EditText = dialog.findViewById(R.id.dateInput_dialog)
    val selectedDateTime : Calendar = Calendar.getInstance()
    val sdfDate = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH)

    fun load() {

        val arrayAdapter : ArrayAdapter<*>
        val options : Array<String> = arrayOf("Option 1", "Option 2", "Option 3","Option 1", "Option 2", "Option 3","Option 1", "Option 2", "Option 3","Option 1", "Option 2", "Option 3")

        arrayAdapter = ArrayAdapter(context, R.layout.listview_item, options)
        searchListView.adapter = arrayAdapter

        searchExerciseEditText.addTextChangedListener {
            println()
            if (it.toString().isNotEmpty()) {
                println("MAKING IT VISIBLE")
                searchListView.visibility = View.VISIBLE
                arrayAdapter.filter.filter(it.toString())

            } else {
                searchListView.visibility = View.GONE
            }
        }

        searchListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            // This is your listview's selected item
            val item = parent.getItemAtPosition(position) as String
            println(item)
            searchExerciseEditText.setText(item)
            searchListView.visibility = View.GONE
        }


        saveButton.setOnClickListener {

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
            }
        }
    }

}