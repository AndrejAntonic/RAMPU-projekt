package com.example.myfitness.helpers

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.*
import com.example.myfitness.DataAccessObjects.ExerciseDAO
import com.example.myfitness.R
import com.google.firebase.firestore.FirebaseFirestore
import model.Exercise


class AddExerciseDialog(private val dialog: AlertDialog, private val context: Context) {

    private val db = FirebaseFirestore.getInstance()
    private val bodyParts = arrayOf("Leđa", "Prsa", "Noge", "Ramena", "Bicepsi", "Tricepsi")
    private val difficulties = arrayOf("1", "2", "3", "4", "5")
    private val equipments = arrayOf("Bučice", "Girje", "Bench", "Smith mašina", "Sprava")


    init {
        val addExerciseView = LayoutInflater.from(context).inflate(R.layout.add_exercise_input_dialog,null)
        dialog.setContentView(addExerciseView)

        val spinnerDiff = addExerciseView.findViewById<Spinner>(R.id.difficultySpinner)

        dialog.setContentView(R.layout.add_exercise_input_dialog)

        val difficultyAdapter =
            ArrayAdapter(context, android.R.layout.simple_spinner_item, difficulties)
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDiff.adapter = difficultyAdapter

        val spinnerEquip = dialog.findViewById<Spinner>(R.id.equipmentSpinner)
        val equipmentAdapter =
            ArrayAdapter(context, android.R.layout.simple_spinner_item, equipments)
        equipmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEquip.adapter = equipmentAdapter

        val spinnerBodyPart = dialog.findViewById<Spinner>(R.id.bodyPartSpinner)
        val bodyPartAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, bodyParts)
        bodyPartAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerBodyPart.adapter = bodyPartAdapter

        spinnerBodyPart.setSelection(0)
        spinnerDiff.setSelection(0)
        spinnerEquip.setSelection(0)

        val addExerciseBtn = dialog.findViewById<Button>(R.id.addExerciseBtn)
        addExerciseBtn.setOnClickListener {

            val exerciseName = dialog.findViewById<EditText>(R.id.exerciseNameEditText).text.toString()
            val exerciseDescription =
                dialog.findViewById<EditText>(R.id.exerciseDescriptionEditText).text.toString()
            val exerciseBodyPart = spinnerBodyPart.selectedItem.toString()
            val imageUrl = dialog.findViewById<EditText>(R.id.imageUrlEditText).text.toString()
            val difficulty = spinnerDiff.selectedItem.toString().toInt()
            val equipment = spinnerEquip.selectedItem.toString()

            val exercise = Exercise(
                exerciseName,
                exerciseDescription,
                exerciseBodyPart,
                imageUrl,
                difficulty,
                equipment
            )

            ExerciseDAO.addExercise(exercise, db)
            Toast.makeText(context, "Vježba dodana!", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        val closeButton = dialog.findViewById<Button>(R.id.closeBtn)
        closeButton.setOnClickListener {
            dialog.dismiss()
        }

    }
}
