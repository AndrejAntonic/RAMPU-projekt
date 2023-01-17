package com.example.myfitness.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.myfitness.R
import com.example.myfitness.helpers.NewGenerateProgramHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PlanFragment : Fragment() {
    private lateinit var btnGenerate: FloatingActionButton
    private lateinit var daysSeekBar: SeekBar
    private lateinit var seekbarValue : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btnGenerate = view.findViewById(R.id.id_fragment_profile_add_program)
        btnGenerate.setOnClickListener { showDialog() }
    }

    private fun showDialog() {
        val newGenerateProgramView = LayoutInflater.from(context).inflate(R.layout.generate_program, null)
        val dialogHelper = NewGenerateProgramHelper(newGenerateProgramView)


        AlertDialog.Builder(context)
            .setView(newGenerateProgramView)
            .setTitle("Generiranje plana treninga")
            .setPositiveButton("Generiraj plan treninga") {_, _ ->
                var newPlan = dialogHelper.buildPlan()
            }.show()

        dialogHelper.populateSpinnerPreference()
        dialogHelper.populateSpinnerExperience()
        dialogHelper.populateSpinnerDays()
    }
}