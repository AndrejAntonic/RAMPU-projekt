package com.example.myfitness.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfitness.DataAccessObjects.ExercisesDAO
import com.example.myfitness.DataAccessObjects.UsersDAO
import com.example.myfitness.R
import com.example.myfitness.adapters.PlanAdapter
import com.example.myfitness.entities.Exercises
import com.example.myfitness.entities.Plan
import com.example.myfitness.entities.PlanPreferences
import com.example.myfitness.helpers.NewGenerateProgramHelper
import com.example.myfitness.utils.Notification
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class PlanFragment : Fragment() {
    private lateinit var btnGenerate: FloatingActionButton
    private lateinit var temp: TextView
    private lateinit var recyclerView: RecyclerView
    var listAllDays = listOf("Ponedjeljak", "Utorak", "Srijeda", "Četvrtak", "Petak", "Subota", "Nedjelja")

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
        loadPlan()
        btnGenerate.setOnClickListener { showDialog() }
    }

    private fun loadPlan() {
        lifecycleScope.launch {
            val currentUser = UsersDAO.getCurrentUser(requireContext())
            val planList: MutableList<Plan> = ExercisesDAO.getPlan(currentUser)
            if(!planList.isNullOrEmpty()) {
                val planAdapter = PlanAdapter(planList)

                recyclerView.adapter = planAdapter
                recyclerView.layoutManager = LinearLayoutManager(view?.context)
                temp.text = "Prethodno generirani trening"
            }
        }
    }

    private fun showDialog() {
        val newGenerateProgramView = LayoutInflater.from(context).inflate(R.layout.generate_program, null)
        val dialogHelperPlan = NewGenerateProgramHelper(newGenerateProgramView)

        val newSelectDaysView = LayoutInflater.from(context).inflate(R.layout.generate_program_pick_days, null)
        val dialogHelperDays = NewGenerateProgramHelper(newSelectDaysView)

        val service = Notification(requireContext().applicationContext)
        val days = arrayOf("Ponedljeljak", "Utorak", "Srijeda", "Četvrtak", "Petak", "Subota", "Nedjelja")
        val bul = booleanArrayOf(false, false, false, false, false, false, false)
        var listSelectedDays = mutableListOf<String>()
        var increment = 0;

        AlertDialog.Builder(context)
            .setView(newGenerateProgramView)
            .setTitle("Generiranje plana treninga")
            .setPositiveButton("Odaberi dane") {_, _ ->
                var newPlanPreferences = dialogHelperPlan.buildPlan()
                AlertDialog.Builder(context)
                    .setView(newSelectDaysView)
                    .setMultiChoiceItems(days, bul) { _, item: Int, checked: Boolean ->
                        if(checked) {
                            if(increment < newPlanPreferences.days) {
                                increment++
                                if(!listSelectedDays.contains(days[item]))
                                    listSelectedDays.add(days[item])
                            }
                            else {
                                increment++
                                bul[item] = false
                                Toast.makeText(context, "Ne možete odabrati više dana!", Toast.LENGTH_SHORT).show()
                            }
                        }
                        else {
                            increment--
                            if(listSelectedDays.contains(days[item]))
                                listSelectedDays.remove(days[item])
                        }
                    }
                    .setPositiveButton("Generiraj plan treninga") {_, _ ->
                        val sortedListDays = listSelectedDays.sortedWith(compareBy {days.indexOf(it)})
                        determinePlan(newPlanPreferences, sortedListDays as MutableList<String>)
                        recyclerView.layoutManager = LinearLayoutManager(view?.context)
                        service.showNotification(newPlanPreferences.days)
                    }.show()
            }.show()

        dialogHelperPlan.populateSpinnerPreference()
        dialogHelperPlan.populateSpinnerExperience()
        dialogHelperPlan.populateSpinnerDays()
    }

    private fun determinePlan(newPlanPreferences: PlanPreferences, listSelectedDays: MutableList<String>) {
        when(newPlanPreferences.days) {
            1 -> generateFullBody(newPlanPreferences, listSelectedDays)
            2 -> generateUpperLower(newPlanPreferences, listSelectedDays)
            3 -> generatePPL(newPlanPreferences, listSelectedDays)
            4 -> generatePPSL(newPlanPreferences, listSelectedDays)
            5 -> generateBroSplit(newPlanPreferences, listSelectedDays)
            6 -> generate2xPPL(newPlanPreferences, listSelectedDays)
            7 -> generateBroUpperLower(newPlanPreferences, listSelectedDays)
        }
    }

    private fun generateBroUpperLower(
        newPlanPreferences: PlanPreferences,
        listSelectedDays: MutableList<String>
    ) {
        lifecycleScope.launch {
            val finalList: MutableList<Plan> = arrayListOf()
            val difficulty = getExperience(newPlanPreferences.experience)
            val selectedExercisesChest = combineList(getList("Prsa", 5, difficulty), preference = newPlanPreferences.preference)
            val selectedExercisesBack = combineList(getList("Leđa", 5, difficulty), preference = newPlanPreferences.preference)
            val selectedExcersisesShoulder = combineList(getList("Ramena", 5, difficulty), preference = newPlanPreferences.preference)
            val selectedExercisesLegs = combineList(getList("Noge", 5, difficulty), preference = newPlanPreferences.preference)
            val selectedExercisesArms = combineList(getList("Bicepsi", 3, difficulty), getList("Tricepsi", 3, difficulty), preference = newPlanPreferences.preference)
            val selectedExercisesUpper = combineList(getList("Prsa", 2, difficulty), getList("Leđa", 2, difficulty), getList("Ramena", 1, difficulty), getList("Bicepsi", 1, difficulty), getList("Tricepsi", 1, difficulty), preference = newPlanPreferences.preference)
            val selectedExercisesLower = combineList(getList("Noge", 5, difficulty), preference = newPlanPreferences.preference)
            finalList.add(Plan("Ponedjeljak", selectedExercisesChest))
            finalList.add(Plan("Utorak", selectedExercisesBack))
            finalList.add(Plan("Srijeda", selectedExcersisesShoulder))
            finalList.add(Plan("Četvrtak", selectedExercisesLegs))
            finalList.add(Plan("Petak", selectedExercisesArms))
            finalList.add(Plan("Subota", selectedExercisesUpper))
            finalList.add(Plan("Nedjelja", selectedExercisesLower))
            var currentUser = UsersDAO.getCurrentUser(requireContext())
            ExercisesDAO.addPlan(finalList, currentUser)
            val planAdapter = PlanAdapter(finalList)

            recyclerView.adapter = planAdapter
            temp.text = "Bro Split x Upper Lower"
        }
    }

    private fun generate2xPPL(
        newPlanPreferences: PlanPreferences,
        listSelectedDays: MutableList<String>
    ) {
        lifecycleScope.launch {
            val finalList: MutableList<Plan> = arrayListOf()
            val difficulty = getExperience(newPlanPreferences.experience)
            val selectedExercisesPush1 = combineList(getList("Prsa", 3, difficulty), getList("Ramena", 1, difficulty), getList("Tricepsi", 1, difficulty), preference = newPlanPreferences.preference)
            val selectedExercisesPull1 = combineList(getList("Leđa", 4, difficulty), getList("Bicepsi", 1, difficulty), preference = newPlanPreferences.preference)
            val selectedExercisesLegs1 = combineList(getList("Noge", 5, difficulty), preference = newPlanPreferences.preference)
            val selectedExercisesPush2 = combineList(getList("Prsa", 2, difficulty), getList("Ramena", 2, difficulty), getList("Tricepsi", 2, difficulty), preference = newPlanPreferences.preference)
            val selectedExercisesPull2 = combineList(getList("Leđa", 3, difficulty), getList("Bicepsi", 4, difficulty), preference = newPlanPreferences.preference)
            val selectedExercisesLegs2 = combineList(getList("Noge", 5, difficulty), preference = newPlanPreferences.preference)
            val listRest: MutableList<Exercises> = arrayListOf()
            listRest.add(Exercises("Odmor"))
            finalList.add(Plan("Ponedjeljak", selectedExercisesPush1))
            finalList.add(Plan("Utorak", selectedExercisesPull1))
            finalList.add(Plan("Srijeda", selectedExercisesLegs1))
            finalList.add(Plan("Četvrtak", listRest))
            finalList.add(Plan("Petak", selectedExercisesPush2))
            finalList.add(Plan("Subota", selectedExercisesPull2))
            finalList.add(Plan("Nedjelja", selectedExercisesLegs2))
            var currentUser = UsersDAO.getCurrentUser(requireContext())
            ExercisesDAO.addPlan(finalList, currentUser)
            val planAdapter = PlanAdapter(finalList)

            recyclerView.adapter = planAdapter
            temp.text = "2xPush Pull Legs"
        }
    }

    private fun generateBroSplit(
        newPlanPreferences: PlanPreferences,
        listSelectedDays: MutableList<String>
    ) {
        lifecycleScope.launch {
            val finalList: MutableList<Plan> = arrayListOf()
            val difficulty = getExperience(newPlanPreferences.experience)
            val selectedExercisesChest = combineList(getList("Prsa", 5, difficulty), preference = newPlanPreferences.preference)
            val selectedExercisesBack = combineList(getList("Leđa", 5, difficulty), preference = newPlanPreferences.preference)
            val selectedExcersisesShoulder = combineList(getList("Ramena", 5, difficulty), preference = newPlanPreferences.preference)
            val selectedExercisesLegs = combineList(getList("Noge", 5, difficulty), preference = newPlanPreferences.preference)
            val selectedExercisesArms = combineList(getList("Bicepsi", 3, difficulty), getList("Tricepsi", 3, difficulty), preference = newPlanPreferences.preference)
            val listRest: MutableList<Exercises> = arrayListOf()
            listRest.add(Exercises("Odmor"))
            finalList.add(Plan("Ponedjeljak", selectedExercisesChest))
            finalList.add(Plan("Utorak", selectedExercisesBack))
            finalList.add(Plan("Srijeda", selectedExcersisesShoulder))
            finalList.add(Plan("Četvrtak", listRest))
            finalList.add(Plan("Petak", selectedExercisesLegs))
            finalList.add(Plan("Subota", selectedExercisesArms))
            finalList.add(Plan("Nedjelja", listRest))
            var currentUser = UsersDAO.getCurrentUser(requireContext())
            ExercisesDAO.addPlan(finalList, currentUser)
            val planAdapter = PlanAdapter(finalList)

            recyclerView.adapter = planAdapter
            temp.text = "Bro Split"
        }
    }

    private fun generatePPSL(
        newPlanPreferences: PlanPreferences,
        listSelectedDays: MutableList<String>
    ) {
        lifecycleScope.launch {
            val finalList: MutableList<Plan> = arrayListOf()
            val difficulty = getExperience(newPlanPreferences.experience)
            val selectedExercisesPush = combineList(getList("Prsa", 4, difficulty), getList("Tricepsi", 2, difficulty), preference = newPlanPreferences.preference)
            val selectedExercisesPull = combineList(getList("Leđa", 4, difficulty), getList("Bicepsi", 2, difficulty), preference = newPlanPreferences.preference)
            val selectedExcersisesShoulder = combineList(getList("Ramena", 4, difficulty), getList("Bicepsi", 1, difficulty), getList("Tricepsi", 1, difficulty), preference = newPlanPreferences.preference)
            val selectedExercisesLegs = combineList(getList("Noge", 5, difficulty), preference = newPlanPreferences.preference)
            val listRest: MutableList<Exercises> = arrayListOf()
            listRest.add(Exercises("Odmor"))
            finalList.add(Plan("Ponedjeljak", selectedExercisesPush))
            finalList.add(Plan("Utorak", selectedExercisesPull))
            finalList.add(Plan("Srijeda", listRest))
            finalList.add(Plan("Četvrtak", selectedExercisesLegs))
            finalList.add(Plan("Petak", selectedExcersisesShoulder))
            finalList.add(Plan("Subota", listRest))
            finalList.add(Plan("Nedjelja", listRest))
            var currentUser = UsersDAO.getCurrentUser(requireContext())
            ExercisesDAO.addPlan(finalList, currentUser)
            val planAdapter = PlanAdapter(finalList)

            recyclerView.adapter = planAdapter
            temp.text = "Push Pull Shoulders Legs"
        }
    }

    private fun generatePPL(
        newPlanPreferences: PlanPreferences,
        listSelectedDays: MutableList<String>
    ) {
        lifecycleScope.launch {
            val finalList: MutableList<Plan> = arrayListOf()
            val difficulty = getExperience(newPlanPreferences.experience)
            val selectedExercisesPush = combineList(getList("Prsa", 2, difficulty), getList("Ramena", 2, difficulty), getList("Tricepsi", 2, difficulty), preference = newPlanPreferences.preference)
            val selectedExercisesPull = combineList(getList("Leđa", 4, difficulty), getList("Bicepsi", 2, difficulty), preference = newPlanPreferences.preference)
            val selectedExercisesLegs: List<Exercises> = combineList(getList("Noge", 5, difficulty), preference = newPlanPreferences.preference)
            val listRest: MutableList<Exercises> = arrayListOf()
            listRest.add(Exercises("Odmor"))
            finalList.add(Plan("Ponedjeljak", selectedExercisesPush))
            finalList.add(Plan("Utorak", listRest))
            finalList.add(Plan("Srijeda", selectedExercisesPull))
            finalList.add(Plan("Četvrtak", listRest))
            finalList.add(Plan("Petak", selectedExercisesLegs))
            finalList.add(Plan("Subota", listRest))
            finalList.add(Plan("Nedjelja", listRest))
            var currentUser = UsersDAO.getCurrentUser(requireContext())
            ExercisesDAO.addPlan(finalList, currentUser)
            val planAdapter = PlanAdapter(finalList)

            recyclerView.adapter = planAdapter
            temp.text = "Push Pull Legs"
        }
    }

    private fun generateUpperLower(newPlanPreferences: PlanPreferences, listSelectedDays: MutableList<String>) {
        lifecycleScope.launch {
            val finalList: MutableList<Plan> = arrayListOf()
            val tempDays = mutableListOf<String>()
            tempDays.addAll(listAllDays)
            val difficulty = getExperience(newPlanPreferences.experience)
            val selectedExercisesUpper = combineList(getList("Prsa", 2, difficulty), getList("Leđa", 2, difficulty), getList("Ramena", 1, difficulty), getList("Bicepsi", 1, difficulty), getList("Tricepsi", 1, difficulty), preference = newPlanPreferences.preference)
            val selectedExercisesLower = combineList(getList("Noge", 6, difficulty), preference = newPlanPreferences.preference)
            val listRest: MutableList<Exercises> = arrayListOf()

            var increment = 1
            for(day in listSelectedDays) {
                when(increment) {
                    1 -> finalList.add(Plan(day, selectedExercisesUpper))
                    2 -> finalList.add(Plan(day, selectedExercisesLower))
                }
                increment++
                tempDays.remove(day)
            }
            for(day in tempDays) {
                finalList.add(Plan(day, listRest))
            }
            val sortedFinalList = finalList.sortedWith(compareBy {listAllDays.indexOf(it.day)}) as MutableList
            var currentUser = UsersDAO.getCurrentUser(requireContext())
            ExercisesDAO.addPlan(sortedFinalList, currentUser)
            val planAdapter = PlanAdapter(sortedFinalList)

            recyclerView.adapter = planAdapter
            temp.text = "Upper Lower"
        }
    }

    private fun generateFullBody(newPlanPreferences: PlanPreferences, listSelectedDays: MutableList<String>) {
        lifecycleScope.launch {
            val finalList: MutableList<Plan> = arrayListOf()
            val tempDays = mutableListOf<String>()
            tempDays.addAll(listAllDays)
            val difficulty = getExperience(newPlanPreferences.experience)
            val selectedExercisesFinal = combineList(getList("Prsa", 2, difficulty), getList("Leđa", 2, difficulty), getList("Ramena", 2, difficulty), getList("Noge", 2, difficulty), getList("Bicepsi", 1, difficulty), getList("Tricepsi", 1, difficulty), preference = newPlanPreferences.preference)
            val listRest: MutableList<Exercises> = arrayListOf()
            listRest.add(Exercises("Odmor"))

            for(day in listSelectedDays) {
                finalList.add(Plan(day, selectedExercisesFinal))
                tempDays.remove(day)
            }
            for(day in tempDays) {
                finalList.add(Plan(day, listRest))
            }
            val sortedFinalList = finalList.sortedWith(compareBy {listAllDays.indexOf(it.day)}) as MutableList

            var currentUser = UsersDAO.getCurrentUser(requireContext())
            ExercisesDAO.addPlan(sortedFinalList, currentUser)
            val planAdapter = PlanAdapter(sortedFinalList)

            recyclerView.adapter = planAdapter
            temp.text = "Full body"
        }
    }

    private fun getExperience(experience: String): Int {
        when(experience) {
            "Beginner" -> return 1
            "Intermediate" -> return 2
            "Advanced" -> return 3
        }
        return 1
    }

    private suspend fun getList(bodyPart: String, n: Int, difficulty: Int): List<Exercises> {
        when(bodyPart) {
            "Prsa" -> return reduceList(getListDAO(bodyPart, difficulty), n)
            "Leđa" -> return reduceList(getListDAO(bodyPart, difficulty), n)
            "Ramena" -> return reduceList(getListDAO(bodyPart, difficulty), n)
            "Noge" -> return reduceList(getListDAO(bodyPart, difficulty), n)
            "Bicepsi" -> return reduceList(getListDAO(bodyPart, difficulty), n)
            "Tricepsi" -> return reduceList(getListDAO(bodyPart, difficulty), n)
        }
        return ArrayList()
    }

    private suspend fun getListDAO(bodyPart: String, difficulty: Int): MutableList<Exercises> {
        return ExercisesDAO.getExercise(bodyPart, difficulty)
    }

    private fun reduceList(list: MutableList<Exercises>, n: Int): List<Exercises> {
        return list.asSequence().shuffled().take(n).toList()
    }

    private fun combineList(vararg lists: List<Exercises>, preference: String): List<Exercises> {
        var result: List<Exercises> = ArrayList()
        for(list in lists) {
            result += addSetsReps(preference, list as MutableList<Exercises>)
        }
        return result
    }

    private fun addSetsReps(goal: String, list: MutableList<Exercises>): List<Exercises> {
        when(goal) {
            "Izgradnja mišića" -> return(addMuscle(list))
            "Mršavljenje" -> return(loseWeight(list))
            "Općenito zdravlje" -> return(overallHealth(list))
        }
        return ArrayList()
    }

    private fun overallHealth(lists: MutableList<Exercises>): List<Exercises> {
        var result: List<Exercises> = ArrayList()
        for(list in lists) {
            list.exercise += " " + (2..3).random() + "x" + (12..16).random()
            result += list
        }
        return result
    }

    private fun loseWeight(lists: MutableList<Exercises>): List<Exercises> {
        var result: List<Exercises> = ArrayList()
        for(list in lists) {
            list.exercise += " " + (3..4).random() + "x" + (10..12).random()
            result += list
        }
        return result
    }

    private fun addMuscle(lists: MutableList<Exercises>): List<Exercises> {
        var result: List<Exercises> = ArrayList()
        for(list in lists) {
            list.exercise += " " + (2..4).random() + "x" + (6..8).random()
            result += list
        }
        return result
    }
}