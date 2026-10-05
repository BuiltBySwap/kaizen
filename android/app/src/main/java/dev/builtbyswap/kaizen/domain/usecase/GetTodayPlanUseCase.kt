package dev.builtbyswap.kaizen.domain.usecase

import dev.builtbyswap.kaizen.domain.model.PlanDay
import dev.builtbyswap.kaizen.domain.repository.PlanRepository
import java.time.Clock
import java.time.LocalDate
import javax.inject.Inject

/** Returns the plan row for today's date, or null if today is outside the plan. */
class GetTodayPlanUseCase @Inject constructor(
    private val repository: PlanRepository,
    private val clock: Clock,
) {
    suspend operator fun invoke(): PlanDay? {
        val today = LocalDate.now(clock)
        return repository.getAll().firstOrNull { it.date == today }
    }
}
