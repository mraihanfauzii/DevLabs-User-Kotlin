package com.hackathon.devlabsuser.ui.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hackathon.devlabsuser.databinding.ActivityRegisterBinding
import com.hackathon.devlabsuser.model.RegisterRequest
import com.hackathon.devlabsuser.viewmodel.AuthenticationViewModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var authenticationViewModel: AuthenticationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authenticationViewModel = ViewModelProvider(this)[AuthenticationViewModel::class.java]
        authenticationViewModel.isLoading.observe(this@RegisterActivity) { loading ->
            showLoading(loading)
        }
        authenticationViewModel.errorMessage.observe(this, Observer { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })

        binding.apply {
            btnRegister.setOnClickListener {
                val fullName = edtNama.text.toString()
                val email = edtEmail.text.toString().trim()
                val nomorTelepon = edtNomorTelepon.text.toString().trim()
                val password = edtPassword.text.toString().trim()
                val passwordConf = edtPassConf.text.toString().trim()
                val registerRequest = RegisterRequest(
                    profileName = fullName,
                    email = email,
                    phoneNumber = nomorTelepon,
                    password = password,
                    role = "client"
                )
                if (passwordConf == password) {
                    if (email.isNotEmpty() && password.isNotEmpty() && fullName.isNotEmpty() && passwordConf.isNotEmpty()) {
                        authenticationViewModel.register(registerRequest)
                        authenticationViewModel.registerResponse.observe(this@RegisterActivity) {
                            if(it.code == 201) {
                                Toast.makeText(this@RegisterActivity, "Successfully register", Toast.LENGTH_SHORT).show()
                                navigateToLoginActivity()
                            } else{
                                Toast.makeText(this@RegisterActivity, "Failed to register", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else if(email.isEmpty() || password.isEmpty() || fullName.isEmpty() || passwordConf.isEmpty()) {
                        Toast.makeText(this@RegisterActivity, "Semua inputan harus diisi!", Toast.LENGTH_SHORT).show()
                    }
                } else if (password.length < 7 || passwordConf.length < 7)
                    Toast.makeText(this@RegisterActivity, "Password tidak boleh kurang dari 6 karakter!", Toast.LENGTH_SHORT).show()
                else {
                    Toast.makeText(this@RegisterActivity, "Kata sandi konfirmasi harus sama dengan kata sandi!", Toast.LENGTH_SHORT).show()
                }
            }

            binding.btnLogin.setOnClickListener {
                val register = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(register)
            }
        }
    }

    private fun navigateToLoginActivity() {
        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }
}