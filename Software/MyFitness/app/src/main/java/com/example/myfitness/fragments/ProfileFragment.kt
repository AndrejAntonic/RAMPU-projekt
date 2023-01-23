package com.example.myfitness.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.example.myfitness.DataAccessObjects.UsersDAO
import com.example.myfitness.R
import com.example.myfitness.StartActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {
    private lateinit var etWeight: EditText
    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnEditProfile: Button
    private lateinit var btnLogout : ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_profile, container, false)

        etWeight = v.findViewById(R.id.userWeight)
        etUsername = v.findViewById(R.id.username)
        etEmail = v.findViewById(R.id.email)
        btnEditProfile = v.findViewById(R.id.button_edit_profile)
        btnLogout = v.findViewById(R.id.btn_logout)

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

        btnLogout.setOnClickListener {
            val currentuser = UsersDAO.getCurrentUser(requireContext())
            val scope = CoroutineScope(Dispatchers.IO)
            scope.launch {
                UsersDAO.invalidateSession(currentuser)
                UsersDAO.removeFromSharedPreferences(requireContext())
                val intent = Intent(requireContext(), StartActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(requireContext(), getString(R.string.notif_logged_out), Toast.LENGTH_SHORT).show()
                }
            }
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
