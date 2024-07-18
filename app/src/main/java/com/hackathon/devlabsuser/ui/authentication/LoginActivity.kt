package com.hackathon.devlabsuser.ui.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hackathon.devlabsuser.databinding.ActivityLoginBinding
import com.hackathon.devlabsuser.model.LoginRequest
import com.hackathon.devlabsuser.ui.main.MainActivity
import com.hackathon.devlabsuser.viewmodel.AuthenticationViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var authenticationViewModel: AuthenticationViewModel
    private lateinit var authenticationManager: AuthenticationManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authenticationManager = AuthenticationManager(this)
        authenticationViewModel = ViewModelProvider(this)[AuthenticationViewModel::class.java]
        authenticationViewModel.isLoading.observe(this@LoginActivity) { loading ->
            showLoading(loading)
        }
        authenticationViewModel.errorMessage.observe(this, Observer { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })

        binding.btnRegister.setOnClickListener {
            val register = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(register)
        }

        binding.apply {
            btnLogin.setOnClickListener {
                val email = edtEmail.text.toString()
                val password = edtPassword.text.toString()
                val loginRequest = LoginRequest(
                    email = email,
                    password = password,
                    role = "client"
                )
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    authenticationViewModel.login(loginRequest)
                    authenticationViewModel.loginResponse.observe(this@LoginActivity) {
                        if(it.code == 200) {
                            authenticationManager.apply {
                                setSession(AuthenticationManager.SESSION, true)
                                login(AuthenticationManager.TOKEN, it.data.accessToken)
                                login(AuthenticationManager.EMAIL, email)
                            }
                            Toast.makeText(this@LoginActivity, "Successfully login", Toast.LENGTH_SHORT).show()
                            navigateToMainActivity()
                        }
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Email dan Password harus diisi!", Toast.LENGTH_SHORT).show()
                }
            }
            btnLoginGoogle.setOnClickListener {
            }
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }
}