package com.example.myfitness

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.myfitness.DataAccessObjects.ExerciseDAO
import com.example.myfitness.fragments.ExercisesFragment
import com.google.firebase.firestore.FirebaseFirestore
import model.Exercise

class AddExerciseFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    private val bodyParts = arrayOf("Leđa", "Prsa", "Noge", "Ramena", "Bicepsi", "Tricepsi")
    private val difficulties = arrayOf("1", "2", "3", "4", "5")
    private val equipments = arrayOf("Bučice", "Girje", "Bench", "Smith mašina", "Sprava")



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_exercise, container, false)

        val spinnerDiff = view.findViewById<Spinner>(R.id.difficultySpinner)
        val difficultyAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, difficulties)
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDiff.adapter = difficultyAdapter


        val spinnerEquip =view.findViewById<Spinner>(R.id.equipmentSpinner)
        val equipmentAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, equipments)
        equipmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEquip.adapter = equipmentAdapter

        val spinnerBodyPart = view.findViewById<Spinner>(R.id.bodyPartSpinner)
        val bodyPartAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, bodyParts)
        bodyPartAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerBodyPart.adapter = bodyPartAdapter

        spinnerBodyPart.setSelection(0)
        spinnerDiff.setSelection(0)
        spinnerEquip.setSelection(0)

        val addExerciseBtn = view.findViewById<Button>(R.id.addExerciseBtn)
        addExerciseBtn.setOnClickListener {


            val exerciseName = view.findViewById<EditText>(R.id.exerciseNameEditText).text.toString()
            val exerciseDescription = view.findViewById<EditText>(R.id.exerciseDescriptionEditText).text.toString()
            val exerciseBodyPart = spinnerBodyPart.selectedItem.toString()
            val imageUrl = view.findViewById<EditText>(R.id.imageUrlEditText).text.toString()
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
            Toast.makeText(requireContext(), "Vježba dodana!", Toast.LENGTH_SHORT).show()

            val frgmntExercise = ExercisesFragment()
            val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.mainlayout, frgmntExercise)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        val closeButton = view.findViewById<Button>(R.id.closeBtn)
        closeButton.setOnClickListener {
            val frgmntExercise = ExercisesFragment()
            val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.mainlayout, frgmntExercise)
            transaction.addToBackStack(null)
            transaction.commit()
        }


        return view
    }
}