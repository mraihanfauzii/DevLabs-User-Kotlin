package com.hackathon.devlabsuser.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hackathon.devlabsuser.model.Architect
import com.hackathon.devlabsuser.model.Attachments
import com.hackathon.devlabsuser.model.PortfolioTheme

class Converters {

    @TypeConverter
    fun fromArchitect(architect: Architect?): String? {
        return Gson().toJson(architect)
    }

    @TypeConverter
    fun toArchitect(architectString: String?): Architect? {
        return Gson().fromJson(architectString, Architect::class.java)
    }

    @TypeConverter
    fun fromPortfolioTheme(theme: PortfolioTheme?): String? {
        return Gson().toJson(theme)
    }

    @TypeConverter
    fun toPortfolioTheme(themeString: String?): PortfolioTheme? {
        return Gson().fromJson(themeString, PortfolioTheme::class.java)
    }

    @TypeConverter
    fun fromAttachmentsList(attachments: List<Attachments>?): String? {
        return Gson().toJson(attachments)
    }

    @TypeConverter
    fun toAttachmentsList(attachmentsString: String?): List<Attachments>? {
        val listType = object : TypeToken<List<Attachments>>() {}.type
        return Gson().fromJson(attachmentsString, listType)
    }
}
