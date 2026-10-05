package dev.builtbyswap.kaizen.presentation.today

import dev.builtbyswap.kaizen.domain.model.PlanDay

/** Everything the screen needs to draw itself — one immutable state. */
data class TodayState(
    val isLoading: Boolean = true,
    val day: PlanDay? = null,
    val error: String? = null,
)

/** Everything the user can do on this screen. */
sealed interface TodayIntent {
    data object Load : TodayIntent
}
