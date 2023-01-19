package com.example.myfitness.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.myfitness.AddExerciseFragment
import com.example.myfitness.R
import com.example.myfitness.helpers.AddDoneExerciseDialogHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ExercisesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_exercises, container, false)

        val button = v.findViewById<Button>(R.id.addExerciseButton)
        button.setOnClickListener{
            val frgmntAddExercise = AddExerciseFragment()
            val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.mainlayout, frgmntAddExercise)
            transaction.commit()
        }



        val btnAddDoneExercise = v.findViewById<Button>(R.id.addDoneExercise)


        btnAddDoneExercise.setOnClickListener {
            val addDoneExerciseDialog = showDialog()
            val addDoneExerciseDialogHelper = AddDoneExerciseDialogHelper(addDoneExerciseDialog, requireContext())

            val scope = CoroutineScope(Dispatchers.Main)
            scope.launch {
                addDoneExerciseDialogHelper.load()
            }

        }


        return v
    }



    private fun showDialog() : AlertDialog {
        val addDoneExerciseDialog = LayoutInflater
            .from(context)
            .inflate(R.layout.done_exercise_input_dialog, null)

        return AlertDialog.Builder(context)
            .setView(addDoneExerciseDialog)
            .show()

    }

}