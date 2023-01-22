package com.example.myfitness.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.FragmentTransaction
import com.example.myfitness.CalculatorActivity
import com.example.myfitness.DataAccessObjects.UsersDAO
import com.example.myfitness.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {
    private lateinit var etWeight: EditText
    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnEditProfile: Button
    private lateinit var btnOpenBazalCalculator : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_profile, container, false)

        etWeight = v.findViewById(R.id.userWeight)
        etUsername = v.findViewById(R.id.username)
        etEmail = v.findViewById(R.id.email)
        btnEditProfile = v.findViewById(R.id.button_edit_profile)
        btnOpenBazalCalculator = v.findViewById(R.id.btn_open_basal_calculator)

        loadUserData()

        btnEditProfile.setOnClickListener {
            val frgmntEditProfile = EditProfileFragment()
            frgmntEditProfile.setOnCloseCallback {
                btnEditProfile.visibility = View.VISIBLE
            }
            val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.mainlayoutProfile, frgmntEditProfile)
            transaction.commit()
            btnEditProfile.visibility = View.GONE
        }

        btnOpenBazalCalculator.setOnClickListener {
            val intent = Intent (requireContext(), CalculatorActivity::class.java)
            startActivity(intent)
        }

        return v
    }

     fun loadUserData(){
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val currentUser = UsersDAO.getUserInfo(requireContext())
            if (currentUser.isNotEmpty()) {
                etWeight.setText(currentUser[0].weight.toString())
                etUsername.setText(currentUser[0].username)
                etEmail.setText(currentUser[0].email)
            }
        }
    }


    override fun onResume() {
        super.onResume()
        loadUserData()
    }
}
