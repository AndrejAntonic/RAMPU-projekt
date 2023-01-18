package com.example.myfitness

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.myfitness.DataAccessObjects.ExerciseDAO
import com.google.firebase.firestore.FirebaseFirestore
import model.Exercise

class AddExerciseActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private val bodyParts = arrayOf("Leđa", "Prsa", "Noge", "Ramena", "Bicepsi", "Tricepsi")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_exercise)

        val spinner = findViewById<Spinner>(R.id.bodyPartSpinner)
        val bodyPartAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, bodyParts)
        bodyPartAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = bodyPartAdapter

        spinner.setSelection(0)

        val addExerciseBtn = findViewById<Button>(R.id.addExerciseBtn)
        addExerciseBtn.setOnClickListener {
            val exerciseName = findViewById<EditText>(R.id.exerciseNameEditText).text.toString()
            val exerciseDescription = findViewById<EditText>(R.id.exerciseDescriptionEditText).text.toString()
            val exerciseBodyPart = spinner.selectedItem.toString()
            val imageUrl = findViewById<EditText>(R.id.imageUrlEditText).text.toString()
            val exercise = Exercise(exerciseName, exerciseDescription, exerciseBodyPart, imageUrl)
            ExerciseDAO.addExercise(exercise, db)
            Toast.makeText(this, "Vježba dodana!", Toast.LENGTH_SHORT).show()
        }
    }
}

