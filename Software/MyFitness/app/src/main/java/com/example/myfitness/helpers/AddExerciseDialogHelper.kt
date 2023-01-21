package com.example.myfitness.helpers

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.provider.MediaStore
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
    private val PICK_IMAGE = 1

    init {
        val addExerciseView = LayoutInflater.from(context).inflate(R.layout.add_exercise_input_dialog, null)
        dialog.setContentView(addExerciseView)

        val spinnerDiff = addExerciseView.findViewById<Spinner>(R.id.difficultySpinner)

        val difficultyAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, difficulties)
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDiff.adapter = difficultyAdapter

        val spinnerEquip = addExerciseView.findViewById<Spinner>(R.id.equipmentSpinner)
        val equipmentAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, equipments)
        equipmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEquip.adapter = equipmentAdapter

        val spinnerBodyPart = addExerciseView.findViewById<Spinner>(R.id.bodyPartSpinner)
        val bodyPartAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, bodyParts)
        bodyPartAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerBodyPart.adapter = bodyPartAdapter

        spinnerBodyPart.setSelection(0)
        spinnerDiff.setSelection(0)
        spinnerEquip.setSelection(0)

        val addExerciseBtn = addExerciseView.findViewById<Button>(R.id.addExerciseBtn)
        addExerciseBtn.setOnClickListener {

            val exerciseName = addExerciseView.findViewById<EditText>(R.id.exerciseNameEditText).text.toString()
            val exerciseDescription = addExerciseView.findViewById<EditText>(R.id.exerciseDescriptionEditText).text.toString()
            val exerciseBodyPart = spinnerBodyPart.selectedItem.toString()
            val difficulty = spinnerDiff.selectedItem.toString().toInt()
            val equipment = spinnerEquip.selectedItem.toString()

            val image = addExerciseView.findViewById<ImageView>(R.id.addExerciseImageView)
            if(image.drawable != null) {
                val bitmap = (image.drawable as BitmapDrawable).bitmap
                val exercise = Exercise(
                    exerciseName,
                    exerciseDescription,
                    difficulty,
                    equipment,
                    exerciseBodyPart
                )

                ExerciseDAO.addExercise(exercise, db, bitmap)
                Toast.makeText(context, "Vježba dodana!", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                Toast.makeText(context, "Odaberite sliku!", Toast.LENGTH_SHORT).show()
            }


        }

        val closeButton = addExerciseView.findViewById<Button>(R.id.closeBtn)
        closeButton.setOnClickListener {
            dialog.dismiss()
        }
        val selectImageBtn = addExerciseView.findViewById<Button>(R.id.addImageBtn)
        selectImageBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            (context as Activity).startActivityForResult(intent, PICK_IMAGE)
            //TODO napraviti da sprema sliku u imageview da se slika moze spremiti u bazu
        }
    }

}


