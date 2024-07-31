package com.hackathon.devlabsuser.viewmodel

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class QuestionnaireViewModel : ViewModel() {
    var currentQuestion = 0
    val answers = mutableListOf<String>()
    var selectedLocation: LatLng? = null
    var selectedLocationName: String? = null
    var selectedTheme: String? = null
}