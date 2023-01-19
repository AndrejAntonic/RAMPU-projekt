package com.example.myfitness.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfitness.AddExerciseFragment
import com.example.myfitness.DataAccessObjects.ExerciseDAO
import com.example.myfitness.R
import com.example.myfitness.adapters.ExerciseRecyclerViewAdapter
import com.google.firebase.firestore.FirebaseFirestore
import model.Exercise


class ExercisesFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var db: FirebaseFirestore
    private var exercises = mutableListOf<Exercise>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_exercises, container, false)

        db = FirebaseFirestore.getInstance()

        val button = view.findViewById<Button>(R.id.addExerciseButton)
        button.setOnClickListener {
            val frgmntAddExercise = AddExerciseFragment()
            val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.mainlayout, frgmntAddExercise)
            button.visibility = View.GONE
            transaction.commit()
        }

        recyclerView = view.findViewById(R.id.exerciseRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        loadExercises()
        return view
    }

    private fun loadExercises() {
        ExerciseDAO.getAllExercises(db) { exercises ->
            val adapter = ExerciseRecyclerViewAdapter(exercises)
            recyclerView.adapter = adapter
            println(exercises)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val exerciseRecyclerView = view.findViewById<RecyclerView>(R.id.exerciseRecyclerView)
        exerciseRecyclerView.layoutManager = LinearLayoutManager(context)

        val adapter = ExerciseRecyclerViewAdapter(exercises)
        exerciseRecyclerView.adapter = adapter
    }




}
