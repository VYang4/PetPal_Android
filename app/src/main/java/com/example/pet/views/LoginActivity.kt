package com.example.pet.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.pet.R
import com.example.pet.SignUpActivity
import com.example.pet.databinding.ActivityLoginBinding
import com.example.pet.viewmodel.MyViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var activityLoginBinding: ActivityLoginBinding
    private lateinit var viewModel: MyViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = ViewModelProvider(this)[MyViewModel::class.java]
        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        activityLoginBinding.vModel = viewModel

        // Observe userLiveData for changes
        viewModel.userLiveData.observe(this) { firebaseUser ->
            if (firebaseUser != null) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        viewModel.authErrorLiveData.observe(this) {
            // React to sign-in error
        }

        activityLoginBinding.createAcctBTN.setOnClickListener(){
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}