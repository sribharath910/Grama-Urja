package com.gramaUrja.utils

object TimeUtils {

    fun getRelativeTime(timestamp: Long): String {
        val diffMs = System.currentTimeMillis() - timestamp
        val diffMin = (diffMs / 60000).toInt()
        return when {
            diffMin < 1 -> "Updated just now"
            diffMin < 60 -> "Updated $diffMin min${if (diffMin > 1) "s" else ""} ago"
            diffMin < 1440 -> {
                val diffHr = diffMin / 60
                "Updated $diffHr hour${if (diffHr > 1) "s" else ""} ago"
            }
            else -> {
                val diffDay = diffMin / 1440
                "Updated $diffDay day${if (diffDay > 1) "s" else ""} ago"
            }
        }
    }

    fun formatTime(timestamp: Long): String {
        val calendar = java.util.Calendar.getInstance()
        calendar.timeInMillis = timestamp
        val hour = calendar.get(java.util.Calendar.HOUR_OF_DAY)
        val minute = calendar.get(java.util.Calendar.MINUTE)
        val amPm = if (hour < 12) "AM" else "PM"
        val displayHour = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
        return "$displayHour:${minute.toString().padStart(2, '0')} $amPm"
    }
}
