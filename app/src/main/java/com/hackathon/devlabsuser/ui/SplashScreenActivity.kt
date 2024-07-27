package com.hackathon.devlabsuser.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.hackathon.devlabsuser.R
import com.hackathon.devlabsuser.ui.authentication.AuthenticationManager
import com.hackathon.devlabsuser.ui.authentication.LoginActivity
import com.hackathon.devlabsuser.ui.main.MainActivity
import com.hackathon.devlabsuser.utils.Setting
import com.hackathon.devlabsuser.viewmodel.ProfileViewModel
import com.hackathon.devlabsuser.viewmodel.SettingViewModel

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var authenticationManager: AuthenticationManager
    private lateinit var settingViewModel: SettingViewModel
    private lateinit var profileViewModel: ProfileViewModel
    private val SPLASH_TIME_OUT: Long = 1000 //1 detik
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        authenticationManager = AuthenticationManager(this)

        settingViewModel = ViewModelProvider(this, SettingViewModel.Factory(Setting(this)))[SettingViewModel::class.java]
        settingViewModel.getSetting().observe(this@SplashScreenActivity) {
            if (it) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        Handler().postDelayed({
            if (authenticationManager.checkSession(AuthenticationManager.SESSION) == true) {
                val loginSuccess = Intent(this@SplashScreenActivity, MainActivity::class.java)
                val getToken = authenticationManager.getAccess(AuthenticationManager.TOKEN).toString()
                val token = "Bearer $getToken"
                profileViewModel.getProfile(token)
                profileViewModel.getProfileResponse.observe(this@SplashScreenActivity) { getProfile ->
                    if (getProfile != null) {
                        authenticationManager.apply {
                            Log.e("Get Profile : ", getProfile.toString())
                            login(AuthenticationManager.NAME, getProfile.profileName ?: "")
                            login(AuthenticationManager.PHONE_NUMBER, getProfile.phoneNumber ?: "")
                            login(AuthenticationManager.PROFILE_PICTURE, getProfile.profilePicture ?: "")
                            login(AuthenticationManager.PROFILE_DESCRIPTION, getProfile.profileDescription ?: "Pengguna baru")
                        }
                    } else {
                        authenticationManager.setSession(AuthenticationManager.SESSION, false)
                        authenticationManager.logOut()
                        startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
                        finishAffinity()
                    }
                }
                Log.e("token", "token : " + authenticationManager.getAccess(AuthenticationManager.TOKEN).toString())
                startActivity(loginSuccess)
                finishAffinity()
            } else {
                startActivity(Intent(this@SplashScreenActivity, OnBoardingActivity::class.java))
                finishAffinity()
            }
        }, SPLASH_TIME_OUT)
    }
}