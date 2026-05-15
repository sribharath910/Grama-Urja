package com.gramaUrja.data.model

data class Zone(
    val id: String,
    val name: String,
    val type: ZoneType,
    val powerStatus: PowerStatus,
    val lastUpdated: Long = System.currentTimeMillis()
)

enum class ZoneType {
    VILLAGE, TRANSFORMER
}

enum class PowerStatus {
    ON, OFF
}
