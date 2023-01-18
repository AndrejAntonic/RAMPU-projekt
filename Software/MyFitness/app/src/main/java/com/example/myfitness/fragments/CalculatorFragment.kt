package com.example.myfitness.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.myfitness.InputActivity
import com.example.myfitness.LoginActivity
import com.example.myfitness.R


class CalculatorFragment : Fragment() {

    lateinit var openButton : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calculator, container, false)


        openButton.setOnClickListener{
            requireActivity().run{
                startActivity(Intent(this, InputActivity::class.java))
                finish()
            }
        }
    }


}