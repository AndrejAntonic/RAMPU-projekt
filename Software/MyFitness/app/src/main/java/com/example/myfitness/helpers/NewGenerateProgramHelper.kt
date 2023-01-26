package com.example.myfitness.helpers

import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.myfitness.DataAccessObjects.ExercisesDAO
import com.example.myfitness.R
import com.example.myfitness.entities.Plan
import com.example.myfitness.entities.PlanPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class NewGenerateProgramHelper(private val view: View) {

    private val preferenceSpinner = view.findViewById<Spinner>(R.id.spn_preference)
    private val experienceSpinner = view.findViewById<Spinner>(R.id.spn_experience)
    private val daysSpinner = view.findViewById<Spinner>(R.id.spn_days)


    fun populateSpinnerPreference() {
        var preference = arrayOf("Izgradnja mišića", "Mršavljenje", "Općenito zdravlje")
        val spinnerAdapter = ArrayAdapter(view.context, android.R.layout.simple_spinner_item, preference)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        preferenceSpinner.adapter = spinnerAdapter
    }

    fun populateSpinnerExperience() {
        var experience = arrayOf("Beginner", "Intermediate", "Advanced")
        val spinnerAdapter = ArrayAdapter(view.context, android.R.layout.simple_spinner_item, experience)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        experienceSpinner.adapter = spinnerAdapter
    }

    fun populateSpinnerDays() {
        var days = arrayOf(1, 2, 3, 4, 5, 6, 7)
        val spinnerAdapter = ArrayAdapter(view.context, android.R.layout.simple_spinner_item, days)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        daysSpinner.adapter = spinnerAdapter
    }

    fun buildPlan(): PlanPreferences{
        val spinnerPreference = view.findViewById<Spinner>(R.id.spn_preference)
        val selectedPreference = spinnerPreference.selectedItem
        val spinnerExperience = view.findViewById<Spinner>(R.id.spn_experience)
        val selectedExperience = spinnerExperience.selectedItem
        val spinnerDays = view.findViewById<Spinner>(R.id.spn_days)
        val selectedDays = spinnerDays.selectedItem

        return PlanPreferences(selectedPreference.toString(), selectedExperience.toString(), selectedDays as Int)
    }
}