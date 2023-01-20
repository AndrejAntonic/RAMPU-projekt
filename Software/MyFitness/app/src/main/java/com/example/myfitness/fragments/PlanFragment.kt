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
import kotlinx.coroutines.flow.combine
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
                determinePlan(newPlanPreferences)
                recyclerView.layoutManager = LinearLayoutManager(view?.context)
                //loadPlan(dialogHelper.determinePlan(newPlanPreferences))
            }.show()

        dialogHelper.populateSpinnerPreference()
        dialogHelper.populateSpinnerExperience()
        dialogHelper.populateSpinnerDays()
    }

    private fun determinePlan(newPlanPreferences: PlanPreferences) {
        when(newPlanPreferences.days) {
            1 -> generateFullBody(newPlanPreferences)
            2 -> generateUpperLower(newPlanPreferences)
            3 -> generatePPL(newPlanPreferences)
            4 -> generatePPSL(newPlanPreferences)
            5 -> generateBroSplit(newPlanPreferences)
            6 -> generate2xPPL(newPlanPreferences)
            7 -> generateBroUpperLower(newPlanPreferences)
        }
    }

    private fun generateBroUpperLower(newPlanPreferences: PlanPreferences) {
        lifecycleScope.launch {
            val finalList: MutableList<Plan> = arrayListOf()
            val selectedExercisesChest = combineList(getList("Prsa", 5))
            val selectedExercisesBack = combineList(getList("Leđa", 5))
            val selectedExcersisesShoulder = combineList(getList("Ramena", 5))
            val selectedExercisesLegs = combineList(getList("Noge", 5))
            val selectedExercisesArms = combineList(getList("Bicepsi", 3), getList("Tricepsi", 3))
            val selectedExercisesUpper = combineList(getList("Prsa", 2), getList("Leđa", 2), getList("Ramena", 1), getList("Bicepsi", 1), getList("Tricepsi", 1))
            val selectedExercisesLower = combineList(getList("Noge", 5))
            finalList.add(Plan("Ponedjeljak", selectedExercisesChest))
            finalList.add(Plan("Utorak", selectedExercisesBack))
            finalList.add(Plan("Srijeda", selectedExcersisesShoulder))
            finalList.add(Plan("Četvrtak", selectedExercisesLegs))
            finalList.add(Plan("Petak", selectedExercisesArms))
            finalList.add(Plan("Subota", selectedExercisesUpper))
            finalList.add(Plan("Nedjelja", selectedExercisesLower))
            val planAdapter = PlanAdapter(finalList)

            recyclerView.adapter = planAdapter
            temp.text = "Bro Split x Upper Lower"
        }
    }

    private fun generate2xPPL(newPlanPreferences: PlanPreferences) {
        lifecycleScope.launch {
            val finalList: MutableList<Plan> = arrayListOf()
            val selectedExercisesPush1 = combineList(getList("Prsa", 3), getList("Ramena", 1), getList("Tricepsi", 1))
            val selectedExercisesPull1 = combineList(getList("Leđa", 4), getList("Bicepsi", 1))
            val selectedExercisesLegs1 = combineList(getList("Noge", 5))
            val selectedExercisesPush2 = combineList(getList("Prsa", 2), getList("Ramena", 2), getList("Tricepsi", 2))
            val selectedExercisesPull2 = combineList(getList("Leđa", 3), getList("Bicepsi", 4))
            val selectedExercisesLegs2 = combineList(getList("Noge", 5))
            val listRest: MutableList<Exercises> = arrayListOf()
            listRest.add(Exercises("Odmor"))
            finalList.add(Plan("Ponedjeljak", selectedExercisesPush1))
            finalList.add(Plan("Utorak", selectedExercisesPull1))
            finalList.add(Plan("Srijeda", selectedExercisesLegs1))
            finalList.add(Plan("Četvrtak", listRest))
            finalList.add(Plan("Petak", selectedExercisesPush2))
            finalList.add(Plan("Subota", selectedExercisesPull2))
            finalList.add(Plan("Nedjelja", selectedExercisesLegs2))
            val planAdapter = PlanAdapter(finalList)

            recyclerView.adapter = planAdapter
            temp.text = "2xPush Pull Legs"
        }
    }

    private fun generateBroSplit(newPlanPreferences: PlanPreferences) {
        lifecycleScope.launch {
            val finalList: MutableList<Plan> = arrayListOf()
            val selectedExercisesChest = combineList(getList("Prsa", 5))
            val selectedExercisesBack = combineList(getList("Leđa", 5))
            val selectedExcersisesShoulder = combineList(getList("Ramena", 5))
            val selectedExercisesLegs = combineList(getList("Noge", 5))
            val selectedExercisesArms = combineList(getList("Bicepsi", 3), getList("Tricepsi", 3))
            val listRest: MutableList<Exercises> = arrayListOf()
            listRest.add(Exercises("Odmor"))
            finalList.add(Plan("Ponedjeljak", selectedExercisesChest))
            finalList.add(Plan("Utorak", selectedExercisesBack))
            finalList.add(Plan("Srijeda", selectedExcersisesShoulder))
            finalList.add(Plan("Četvrtak", listRest))
            finalList.add(Plan("Petak", selectedExercisesLegs))
            finalList.add(Plan("Subota", selectedExercisesArms))
            finalList.add(Plan("Nedjelja", listRest))
            val planAdapter = PlanAdapter(finalList)

            recyclerView.adapter = planAdapter
            temp.text = "Bro Split"
        }
    }

    private fun generatePPSL(newPlanPreferences: PlanPreferences) {
        lifecycleScope.launch {
            val finalList: MutableList<Plan> = arrayListOf()
            val selectedExercisesPush = combineList(getList("Prsa", 4), getList("Tricepsi", 2))
            val selectedExercisesPull = combineList(getList("Leđa", 4), getList("Bicepsi", 2))
            val selectedExcersisesShoulder = combineList(getList("Ramena", 4), getList("Bicepsi", 1), getList("Tricepsi", 1))
            val selectedExercisesLegs = combineList(getList("Noge", 5))
            val listRest: MutableList<Exercises> = arrayListOf()
            listRest.add(Exercises("Odmor"))
            finalList.add(Plan("Ponedjeljak", selectedExercisesPush))
            finalList.add(Plan("Utorak", selectedExercisesPull))
            finalList.add(Plan("Srijeda", listRest))
            finalList.add(Plan("Četvrtak", selectedExercisesLegs))
            finalList.add(Plan("Petak", selectedExcersisesShoulder))
            finalList.add(Plan("Subota", listRest))
            finalList.add(Plan("Nedjelja", listRest))
            val planAdapter = PlanAdapter(finalList)

            recyclerView.adapter = planAdapter
            temp.text = "Push Pull Shoulders Legs"
        }
    }

    private fun generatePPL(newPlanPreferences: PlanPreferences) {
        lifecycleScope.launch {
            val finalList: MutableList<Plan> = arrayListOf()
            val selectedExercisesPush = combineList(getList("Prsa", 2), getList("Ramena", 2), getList("Tricepsi", 2))
            val selectedExercisesPull = combineList(getList("Leđa", 4), getList("Bicepsi", 2))
            val selectedExercisesLegs: List<Exercises> = combineList(getList("Noge", 5))
            val listRest: MutableList<Exercises> = arrayListOf()
            listRest.add(Exercises("Odmor"))
            finalList.add(Plan("Ponedjeljak", selectedExercisesPush))
            finalList.add(Plan("Utorak", listRest))
            finalList.add(Plan("Srijeda", selectedExercisesPull))
            finalList.add(Plan("Četvrtak", listRest))
            finalList.add(Plan("Petak", selectedExercisesLegs))
            finalList.add(Plan("Subota", listRest))
            finalList.add(Plan("Nedjelja", listRest))
            val planAdapter = PlanAdapter(finalList)

            recyclerView.adapter = planAdapter
            temp.text = "Push Pull Legs"
        }
    }

    private fun generateUpperLower(newPlanPreferences: PlanPreferences) {
        lifecycleScope.launch {
            val finalList: MutableList<Plan> = arrayListOf()
            val selectedExercisesUpper = combineList(getList("Prsa", 2), getList("Leđa", 2), getList("Ramena", 1), getList("Bicepsi", 1), getList("Tricepsi", 1))
            val selectedExercisesLower = combineList(getList("Noge", 6))
            val listRest: MutableList<Exercises> = arrayListOf()
            listRest.add(Exercises("Odmor"))
            finalList.add(Plan("Ponedjeljak", selectedExercisesUpper))
            finalList.add(Plan("Utorak", listRest))
            finalList.add(Plan("Srijeda", listRest))
            finalList.add(Plan("Četvrtak", selectedExercisesLower))
            finalList.add(Plan("Petak", listRest))
            finalList.add(Plan("Subota", listRest))
            finalList.add(Plan("Nedjelja", listRest))
            val planAdapter = PlanAdapter(finalList)

            recyclerView.adapter = planAdapter
            temp.text = "Upper Lower"
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

            recyclerView.adapter = planAdapter
            temp.text = "Full body"
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