package com.example.myfitness.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfitness.DataAccessObjects.ExercisesDAO
import com.example.myfitness.LoginActivity
import com.example.myfitness.R
import com.example.myfitness.adapters.PlanAdapter
import com.example.myfitness.entities.Exercises
import com.example.myfitness.entities.Plan
import com.example.myfitness.entities.PlanPreferences
import com.example.myfitness.helpers.NewGenerateProgramHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import kotlin.random.Random

class PlanFragment : Fragment() {
    private lateinit var btnGenerate: FloatingActionButton
    private lateinit var temp: TextView
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btnGenerate = view.findViewById(R.id.id_fragment_profile_add_program)
        recyclerView = view.findViewById(R.id.rv_plan_main)
        temp = view.findViewById(R.id.id_test)
        btnGenerate.setOnClickListener { showDialog() }
    }

    private fun loadPlan(plan: String) {
        temp.text = plan
    }

    private fun showDialog() {
        val newGenerateProgramView = LayoutInflater.from(context).inflate(R.layout.generate_program, null)
        val dialogHelper = NewGenerateProgramHelper(newGenerateProgramView)

        AlertDialog.Builder(context)
            .setView(newGenerateProgramView)
            .setTitle("Generiranje plana treninga")
            .setPositiveButton("Generiraj plan treninga") {_, _ ->
                var newPlanPreferences = dialogHelper.buildPlan()
                generateFullBody(newPlanPreferences)
                recyclerView.layoutManager = LinearLayoutManager(view?.context)
                //loadPlan(dialogHelper.determinePlan(newPlanPreferences))
            }.show()

        dialogHelper.populateSpinnerPreference()
        dialogHelper.populateSpinnerExperience()
        dialogHelper.populateSpinnerDays()
    }

    private fun determinePlan(newPlanPreferences: PlanPreferences) {
        when(newPlanPreferences.days) {
            1 -> return(generateFullBody(newPlanPreferences))
            //2 -> return(generateUpperLower(newPlanPreferences))
            //3 -> return(generatePPL(newPlanPreferences))
            //4 -> return(generatePPSL(newPlanPreferences))
            //5 -> return(generateBroSplit(newPlanPreferences))
            //6 -> return(generate2xPPL(newPlanPreferences))
            //7 -> return(generatePPSLUpperLower(newPlanPreferences))
        }
    }

    private fun generateFullBody(newPlanPreferences: PlanPreferences) {
        lifecycleScope.launch {
            temp.text = "Full body"
            val finalList: MutableList<Plan> = arrayListOf()
            val exercisesTemp: MutableList<Exercises> = ExercisesDAO.getExercise("Ruke")
            val exercises = exercisesTemp.asSequence().shuffled().take(2).toList()
            val exercises2 = exercisesTemp.asSequence().shuffled().take(3).toList()
            finalList.add(Plan("Ponedjeljak", exercises))
            finalList.add(Plan("Utorak", exercises2))
            val planAdapter = PlanAdapter(finalList)

            recyclerView.adapter = planAdapter
        }
    }
}