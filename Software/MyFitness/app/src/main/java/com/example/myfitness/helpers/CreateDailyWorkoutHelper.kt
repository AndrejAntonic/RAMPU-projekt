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
import com.example.myfitness.DataAccessObjects.DailyPlanDAO
import com.example.myfitness.DataAccessObjects.ExerciseDAO
import com.example.myfitness.DataAccessObjects.UsersDAO
import com.example.myfitness.R
import com.example.myfitness.adapters.ExerciseListRecyclerViewAdapter
import com.example.myfitness.entities.DailyExercises
import com.google.firebase.Timestamp
import com.example.myfitness.entities.DoneExercise
import java.text.SimpleDateFormat
import java.util.*

class CreateDailyWorkoutHelper(private val context: Context) {
    //kreiranje potrebnih varijabli koje ce se kasnije inicijalizirati
    private val dialog : AlertDialog
    private val dialogView : View
    private val searchExerciseEditText : EditText
    private val recycleView : RecyclerView
    private var selectedExercise : String = ""
    private val saveButton : Button
    private val addButton : Button
    private val cancelButton : Button
    private var allExerciseNames : MutableList<String> = mutableListOf()
    private val setsInput : EditText
    private val repsInput : EditText
    private val weightInput : EditText
    private val dateinput : EditText
    private val selectedDateTime : Calendar
    private val sdfDate : SimpleDateFormat

    private val exerciseList = mutableListOf<DailyExercises>()


    //inicijalizacija AlertDialoga, dialog se kreira iz XML layout filea, inflatea se layout te postavlja kao sadržaj AlertDialoga
    init {
        dialogView = LayoutInflater
            .from(context)
            .inflate(R.layout.fragment_create_workout_plan_for_day, null)
        dialog = AlertDialog.Builder(context)
            .setView(dialogView).show()

        //dodijeljivanje vrijednosti prethodno kreiranim varijablama
        searchExerciseEditText = dialog.findViewById(R.id.exercisePicker1)
        recycleView = dialog.findViewById(R.id.rv_exercise_picker1)
        selectedExercise = ""
        saveButton = dialog.findViewById(R.id.btn_save_doneexercise_dialog1)
        addButton = dialog.findViewById(R.id.btn_add_doneexercise_dialog1)
        cancelButton = dialog.findViewById(R.id.btn_cancel_doneexercise_dialog1)
        allExerciseNames = mutableListOf()
        setsInput = dialog.findViewById(R.id.setsInput1)
        repsInput = dialog.findViewById(R.id.repsInput1)
        weightInput = dialog.findViewById(R.id.weightInput1)
        dateinput = dialog.findViewById(R.id.dateInput_dialog1)
        selectedDateTime = Calendar.getInstance()
        sdfDate = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH)
    }

    //ucitava podatke u recycler view
    suspend fun load() {
        //dohvaća se lista svih vježbi i sprema u allExerciseNames
        allExerciseNames = ExerciseDAO.getAllExerciseNames()
        recycleView.layoutManager = LinearLayoutManager(context)
        val adapter = ExerciseListRecyclerViewAdapter(allExerciseNames)
        recycleView.adapter = adapter

        //kada se pritisne na item iz recycler viewa, uzima poziciju itema te taj item postavlja kao odabrani item
        adapter.setOnItemClickListener(object : ExerciseListRecyclerViewAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, item: String) {
                selectedExercise = item
                searchExerciseEditText.setText(selectedExercise)
                recycleView.visibility = View.GONE
                searchExerciseEditText.error = null
            }
        })

        //ako kliknes na njega vidis sve ponudene vjezbe, inace ne vidis (kao combobox)

        searchExerciseEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                recycleView.visibility = View.VISIBLE
            } else {
                recycleView.visibility = View.GONE
            }
        }

        //filtriranje vježbi u recycler viewu ovisno o unesenom tekstu
        searchExerciseEditText.addTextChangedListener {
            if (it.toString().isNotEmpty()) {
                recycleView.visibility = View.VISIBLE
                adapter.filter(it.toString())
            } else {
                recycleView.visibility = View.GONE
            }
        }




        //sprema u listu

        addButton.setOnClickListener {
            val inputValid = validateInput()
            if (!inputValid) {
                return@setOnClickListener
            }

            //val currentUser = UsersDAO.getCurrentUser(context)
            val exercise : DailyExercises = buildExercise()
            //spremanje vježbe u listu
            exerciseList.add(exercise)
            Toast.makeText(context, "Vježba dodana u plan.", Toast.LENGTH_SHORT).show()

            //reset teksta nakon unosa jedne vjezbe i zakljucavanje datuma jer se radi za isti dan taj plan
            searchExerciseEditText.text.clear()
            setsInput.text.clear()
            weightInput.text.clear()
            repsInput.text.clear()
            dateinput.isEnabled = false

        }


        //sprema u bazu iz liste klikom na save button za trenutnog korisnika

        saveButton.setOnClickListener {

            val currentUser = UsersDAO.getCurrentUser(context)
            DailyPlanDAO.add(exerciseList, currentUser)
            dialog.dismiss()
            Toast.makeText(context, "Kreirani plan je spremljen.", Toast.LENGTH_SHORT).show()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        //activateDateTimeListeners()
    }

    //biranje datuma iz EditTexta

    /*fun activateDateTimeListeners() {
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
    }*/

    //provjere unosa
    private fun validateInput() : Boolean {
        val pattern = "^\\d{2}\\.\\d{2}\\.\\d{4}.\$"
        var allValid = true
        if (selectedExercise.length == 0) {
            searchExerciseEditText.error = "Potrebno odabrati vježbu!"
            allValid = false
        }
        if (weightInput.text.length == 0) {
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
        }
        if (!dateinput.text.matches(pattern.toRegex())) {
            dateinput.error = "Unesite ispravan datum u formatu dd.mm.yyyy."
            allValid = false
        }
        return allValid
    }

    //reformatiranje vrijednosti u format koji se može koristiti za kreiranje nove instance DailyExercises
    //uzimanje inputa korisnika
    fun buildExercise() : DailyExercises {
        return DailyExercises(
            selectedExercise,
            weightInput.text.toString().toInt(),
            setsInput.text.toString().toInt(),
            repsInput.text.toString().toInt(),
            dateinput.text.toString()
        )
    }
}