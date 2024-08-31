package com.hackathon.devlabsuser.viewmodel

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class QuestionnaireViewModel : ViewModel() {
    var architectId: String? = null
    var currentQuestion = 0
    val answers = mutableListOf<String>()
    var selectedLocation: LatLng? = null
    var selectedLocationName: String? = null
    var selectedTheme: String? = null
    var projectName: String? = null
    var budget: String? = null
    var buildingTime: String? = null
    var notes: String? = null

    var area: Int? = null
    var city: String? = null
    var theme: String? = null
}