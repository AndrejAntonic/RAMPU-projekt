package com.example.myfitness.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.example.myfitness.DataAccessObjects.UsersDAO
import com.example.myfitness.R
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {
    private lateinit var etWeight: EditText
    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnEditProfile: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_profile, container, false)

        etWeight = v.findViewById(R.id.userWeight)
        etUsername = v.findViewById(R.id.username)
        etEmail = v.findViewById(R.id.email)
        btnEditProfile = v.findViewById(R.id.button_edit_profile)

        viewLifecycleOwner.lifecycleScope.launch {
            val currentUser = UsersDAO.getUserInfo(requireContext())
            if(currentUser.isNotEmpty()){
                etWeight.setText(currentUser[0].weight.toString())
                etUsername.setText(currentUser[0].username)
                etEmail.setText(currentUser[0].email)
            }
        }
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
        return v
    }
}
