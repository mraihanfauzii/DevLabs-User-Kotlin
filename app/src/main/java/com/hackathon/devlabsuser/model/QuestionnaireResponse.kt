package com.hackathon.devlabsuser.model

data class QuestionnaireResponse(
    var serviceType: String? = null,
    var projectLocation: Pair<Double, Double>? = null,
    var projectNeedType: String? = null,
    var buildingType: String? = null,
    var buildingArea: String? = null,
    var numOccupants: Int? = null,
    var numRooms: Int? = null,
    var numBathrooms: Int? = null,
    var numFloors: Int? = null,
    var theme: String? = null,
    var budgetRange: String? = null,
    var startDate: String? = null,
    var additionalNotes: String? = null
)
