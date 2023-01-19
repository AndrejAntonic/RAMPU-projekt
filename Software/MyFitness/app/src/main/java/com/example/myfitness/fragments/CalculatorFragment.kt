package com.example.myfitness.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.myfitness.CalculatorActivity
import com.example.myfitness.InputActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton


class CalculatorFragment : Fragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val intent = Intent (this@CalculatorFragment.context, CalculatorActivity::class.java)
        startActivity(intent)
    }

}