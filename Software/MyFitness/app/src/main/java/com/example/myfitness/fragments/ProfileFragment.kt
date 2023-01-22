package com.example.myfitness.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentTransaction
import com.example.myfitness.R


class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_profile, container, false)

        val button = v.findViewById<Button>(R.id.button_edit_profile)
        button.setOnClickListener {
            val frgmntEditProfile = EditProfileFragment()
            frgmntEditProfile.setOnCloseCallback {
                button.visibility = View.VISIBLE
            }
            val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.mainlayoutProfile, frgmntEditProfile)
            transaction.commit()
            button.visibility = View.GONE
        }


        return v
    }

}