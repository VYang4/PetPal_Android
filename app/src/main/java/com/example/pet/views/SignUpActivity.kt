package com.example.pet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.pet.databinding.ActivitySignUpBinding
import com.example.pet.viewmodel.MyViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var authViewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        authViewModel = ViewModelProvider(this)[MyViewModel::class.java]

        binding.lifecycleOwner = this
        binding.viewModel = authViewModel

        // Observe userLiveData for changes
        authViewModel.userLiveData.observe(this) { firebaseUser ->
            if (firebaseUser != null) {
                Toast.makeText(this, "Sign up successfully", Toast.LENGTH_LONG).show()
                // User is successfully signed up, navigate back to MainActivity
                finish() // This closes the SignUpActivity and returns to the previous activity in the stack
            }
        }

        authViewModel.authErrorLiveData.observe(this) { errorMessage ->
            if (errorMessage != null) {
                // Show error message
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
            }
        }

        binding.accSignUpButton.setOnClickListener {
            val email = binding.emailCreate.text.toString()
            val password = binding.passwordCreate.text.toString()
            authViewModel.signUpWithEmail(email, password)
        }
    }
}