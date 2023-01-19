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
            val finalList: MutableList<Plan> = arrayListOf()
            val selectedExercisesFinal = combineList(getList("Prsa", 2), getList("Leđa", 2), getList("Ramena", 2), getList("Noge", 2), getList("Bicepsi", 1), getList("Tricepsi", 1))
            val listRest: MutableList<Exercises> = arrayListOf()
            listRest.add(Exercises("Odmor"))
            finalList.add(Plan("Ponedjeljak", selectedExercisesFinal))
            finalList.add(Plan("Utorak", listRest))
            finalList.add(Plan("Srijeda", listRest))
            finalList.add(Plan("Četvrtak", listRest))
            finalList.add(Plan("Petak", listRest))
            finalList.add(Plan("Subota", listRest))
            finalList.add(Plan("Nedjelja", listRest))
            val planAdapter = PlanAdapter(finalList)

            temp.text = "Full body"
            recyclerView.adapter = planAdapter
        }
    }

    private suspend fun getList(bodyPart: String, n: Int): List<Exercises> {
        when(bodyPart) {
            "Prsa" -> return reduceList(getList(bodyPart), n)
            "Leđa" -> return reduceList(getList(bodyPart), n)
            "Ramena" -> return reduceList(getList(bodyPart), n)
            "Noge" -> return reduceList(getList(bodyPart), n)
            "Bicepsi" -> return reduceList(getList(bodyPart), n)
            "Tricepsi" -> return reduceList(getList(bodyPart), n)
        }
        return ArrayList()
    }

    private suspend fun getList(bodyPart: String): MutableList<Exercises> {
        return ExercisesDAO.getExercise(bodyPart)
    }

    private fun reduceList(list: MutableList<Exercises>, n: Int): List<Exercises> {
        return list.asSequence().shuffled().take(n).toList()
    }

    private fun combineList(vararg lists: List<Exercises>): List<Exercises> {
        var result: List<Exercises> = ArrayList()
        for(list in lists) {
            result += list
        }
        return result
    }
}