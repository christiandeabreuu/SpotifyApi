package com.example.spotifyapi.utils


import java.text.SimpleDateFormat
import java.util.Locale

object DateUtils {
    fun formatDateFromIsoToBr(dateString: String?): String {
        if (dateString.isNullOrBlank()) return ""
        return try {
            val inputFormat = when {
                dateString.length == 10 -> SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                dateString.length == 7 -> SimpleDateFormat("yyyy-MM", Locale.getDefault())
                dateString.length == 4 -> SimpleDateFormat("yyyy", Locale.getDefault())
                else -> return dateString
            }
            val outputFormat = when {
                dateString.length == 10 -> SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                dateString.length == 7 -> SimpleDateFormat("MM/yyyy", Locale.getDefault())
                dateString.length == 4 -> SimpleDateFormat("yyyy", Locale.getDefault())
                else -> SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            }
            val date = inputFormat.parse(dateString)
            if (date != null) outputFormat.format(date) else dateString
        } catch (e: Exception) {
            dateString
        }
    }
}