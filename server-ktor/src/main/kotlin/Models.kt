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

@Serializable
data class PlanDay(
    val date: String,
    val id: String,
    val track: String,
    val priority: String,
    val load: String,
    val hours: Double,
    val topic: String,
    val why: String,
    val steps: List<String>,
    val tool: String,
    val leetcode: String,
    val mobileQuestion: String,
    val english: String,
)

@Serializable
data class Question(
    val number: Int,
    val category: String,
    val text: String,
    val date: String,
)

@Serializable
data class ApiError(val error: String)