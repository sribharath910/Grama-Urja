package com.gramaUrja.data.model

data class ActivityItem(
    val id: String,
    val zoneId: String,
    val zoneName: String,
    val action: PowerStatus,
    val timestamp: Long = System.currentTimeMillis()
)
