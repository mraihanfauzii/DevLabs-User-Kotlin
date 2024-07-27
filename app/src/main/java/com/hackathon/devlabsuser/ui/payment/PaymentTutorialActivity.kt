package com.hackathon.devlabsuser.ui.payment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hackathon.devlabsuser.databinding.ActivityPaymentTutorialBinding

class PaymentTutorialActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentTutorialBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentTutorialBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}