package com.example.myfitness.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.myfitness.DataAccessObjects.UsersDAO
import com.example.myfitness.R
import kotlinx.coroutines.launch


class EditProfileFragment : Fragment() {

    private var onCloseCallback: (() -> Unit)? = null
    fun setOnCloseCallback(callback: () -> Unit) {
        onCloseCallback = callback
    }

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var etWeight: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnEditUser: Button
    private lateinit var btnCancel: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_edit_profile, container, false)

        etWeight = v.findViewById(R.id.userWeight)
        etUsername = v.findViewById(R.id.usernameEdit)
        etPassword = v.findViewById(R.id.passwordEdit)
        etEmail = v.findViewById(R.id.emailEdit)
        btnEditUser = v.findViewById(R.id.saveChangesBtn)
        btnCancel = v.findViewById(R.id.cancelBtn)

        btnEditUser.setOnClickListener {
            val weight = etWeight.text.toString().toDouble()
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()
            val email = etEmail.text.toString()

            viewLifecycleOwner.lifecycleScope.launch {
                UsersDAO.EditUser(username, email, password, weight)
            }
            onCloseCallback?.invoke()
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.remove(this)
            fragmentTransaction.commit()
        }

        btnCancel.setOnClickListener{
            onCloseCallback?.invoke()
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.remove(this)
            fragmentTransaction.commit()
        }

        return v
    }
}
