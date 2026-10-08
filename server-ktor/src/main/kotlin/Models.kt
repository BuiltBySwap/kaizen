package dev.builtbyswap

import kotlinx.serialization.Serializable

@Serializable
data class Health(val status: String)

@Serializable
data class PlanDayDto(
    val date: String,
    val id: String,
    val track: String,
    val topic: String,
    val hours: Double,
)