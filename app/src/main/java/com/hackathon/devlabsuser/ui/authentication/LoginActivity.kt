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
import com.hackathon.devlabsuser.viewmodel.ProfileViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var authenticationViewModel: AuthenticationViewModel
    private lateinit var authenticationManager: AuthenticationManager
    private lateinit var profileViewModel: ProfileViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

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
                    login(email, authenticationViewModel)
                } else if (email.isEmpty() && password.isEmpty()){
                    Toast.makeText(this@LoginActivity, "Email dan Password harus diisi!", Toast.LENGTH_SHORT).show()
                } else if (email.isEmpty()) {
                    Toast.makeText(this@LoginActivity, "Email harus diisi!", Toast.LENGTH_SHORT).show()
                } else if (password.isEmpty()) {
                    Toast.makeText(this@LoginActivity, "Password harus diisi!", Toast.LENGTH_SHORT).show()
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

    private fun login(email: String, authenticationViewModel: AuthenticationViewModel) {
        authenticationViewModel.loginResponse.observe(this@LoginActivity) { loginResponse ->
            if(loginResponse != null) {
                if(loginResponse.success) {
                    authenticationManager.apply {
                        setSession(AuthenticationManager.SESSION, true)
                        login(AuthenticationManager.TOKEN, loginResponse.data.accessToken)
                        login(AuthenticationManager.EMAIL, email)
                        login(AuthenticationManager.ROLE, "client")
                        val getToken = authenticationManager.getAccess(AuthenticationManager.TOKEN).toString()
                        val token = "Bearer $getToken"
                        profileViewModel.getProfile(token)
                        profileViewModel.getProfileResponse.observe(this@LoginActivity) { getProfileResponse ->
                            if (getProfileResponse != null) {
                                login(AuthenticationManager.NAME, getProfileResponse.profileName ?: "")
                                login(AuthenticationManager.ID, getProfileResponse.id ?: "")
                                login(AuthenticationManager.PHONE_NUMBER, getProfileResponse.phoneNumber ?: "")
                                login(AuthenticationManager.PROFILE_PICTURE, getProfileResponse.profilePicture ?: "")
                                login(AuthenticationManager.PROFILE_DESCRIPTION, getProfileResponse.profileDescription ?: "Pengguna baru")
                            }
                        }
                    }
                    Toast.makeText(this@LoginActivity, "Successfully login", Toast.LENGTH_SHORT).show()
                    navigateToMainActivity()
                }
                else {
                    Toast.makeText(this@LoginActivity, "Email dan password tidak sesuai!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}