package com.hackathon.devlabsuser.ui.payment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hackathon.devlabsuser.databinding.ActivityPaymentBinding
import com.hackathon.devlabsuser.databinding.ActivityPaymentDetailBinding

class PaymentDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}