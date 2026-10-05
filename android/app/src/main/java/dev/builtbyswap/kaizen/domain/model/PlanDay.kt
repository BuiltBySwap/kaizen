package dev.builtbyswap.kaizen.domain.model

import java.time.LocalDate

/** One study day from the roadmap. */
data class PlanDay(
    val date: LocalDate,
    val id: String,
    val track: String,
    val priority: String,
    val load: String,
    val hours: Double,
    val topic: String,
    val why: String = "",
    val steps: List<String> = emptyList(),
    val tool: String = "",
    val leetCode: String = "",
    val mobileQuestion: String = "",
    val english: String = "",
)
