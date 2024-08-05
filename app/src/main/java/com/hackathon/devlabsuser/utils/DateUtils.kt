package com.hackathon.devlabsuser.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    fun formatTimestamp(timestamp: String): String {
        val apiDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        apiDateFormat.timeZone = TimeZone.getTimeZone("UTC")

        val date = apiDateFormat.parse(timestamp)
        val currentDate = Date()

        val today = Calendar.getInstance()
        today.time = currentDate

        val messageDate = Calendar.getInstance()
        messageDate.time = date

        return when {
            isSameDay(today, messageDate) -> SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)
            isYesterday(today, messageDate) -> "Kemarin"
            else -> SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(date)
        }
    }

    private fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    private fun isYesterday(today: Calendar, messageDay: Calendar): Boolean {
        today.add(Calendar.DAY_OF_YEAR, -1)
        return isSameDay(today, messageDay)
    }
}
