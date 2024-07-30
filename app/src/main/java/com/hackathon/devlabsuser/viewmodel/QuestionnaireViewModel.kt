package com.hackathon.devlabsuser.viewmodel

import androidx.lifecycle.ViewModel

class QuestionnaireViewModel : ViewModel() {
    var currentQuestion = 0
    val answers = mutableListOf<String>()
}