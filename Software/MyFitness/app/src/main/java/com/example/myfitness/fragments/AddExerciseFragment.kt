package com.example.myfitness

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.myfitness.DataAccessObjects.ExerciseDAO
import com.google.firebase.firestore.FirebaseFirestore
import model.Exercise

class AddExerciseFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    private val bodyParts = arrayOf("Leđa", "Prsa", "Noge", "Ramena", "Bicepsi", "Tricepsi")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_exercise, container, false)
        val spinner = view.findViewById<Spinner>(R.id.bodyPartSpinner)
        val bodyPartAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, bodyParts)
        bodyPartAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = bodyPartAdapter

        spinner.setSelection(0)

        val addExerciseBtn = view.findViewById<Button>(R.id.addExerciseBtn)
        addExerciseBtn.setOnClickListener {
            val exerciseName = view.findViewById<EditText>(R.id.exerciseNameEditText).text.toString()
            val exerciseDescription = view.findViewById<EditText>(R.id.exerciseDescriptionEditText).text.toString()
            val exerciseBodyPart = spinner.selectedItem.toString()
            val imageUrl = view.findViewById<EditText>(R.id.imageUrlEditText).text.toString()
            val difficulty = view.findViewById<EditText>(R.id.difficultySpinner).text.toString().toInt()
            val equipment = view.findViewById<EditText>(R.id.equipmentSpinner).toString()

            val exercise = Exercise(
                exerciseName,
                exerciseDescription,
                exerciseBodyPart,
                imageUrl,
                difficulty,
                equipment
            )
            ExerciseDAO.addExercise(exercise, db)
            Toast.makeText(requireContext(), "Vježba dodana!", Toast.LENGTH_SHORT).show()
        }
        return view
    }
}