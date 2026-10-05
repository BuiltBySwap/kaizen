package dev.builtbyswap.kaizen.domain.usecase

import dev.builtbyswap.kaizen.domain.model.PlanDay
import dev.builtbyswap.kaizen.domain.repository.PlanRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class GetTodayPlanUseCaseTest {

    private val ist = ZoneId.of("Asia/Kolkata")

    private fun clockAt(instant: String) = Clock.fixed(Instant.parse(instant), ist)

    private fun day(date: String, id: String) =
        PlanDay(LocalDate.parse(date), id, "Track", "P0", "Normal", 1.0, "Topic $id")

    private val repository = object : PlanRepository {
        override suspend fun getAll() = listOf(day("2026-10-03", "DD-01"), day("2026-10-04", "PRJ-01"))
    }

    @Test
    fun returnsTheRowForToday() = runBlocking {
        val useCase = GetTodayPlanUseCase(repository, clockAt("2026-10-04T03:00:00Z")) // 08:30 IST, 4 Oct
        assertEquals("PRJ-01", useCase()?.id)
    }

    @Test
    fun usesTheLocalDateNotUtc() = runBlocking {
        // 20:00 UTC on 3 Oct is already 01:30 IST on 4 Oct
        val useCase = GetTodayPlanUseCase(repository, clockAt("2026-10-03T20:00:00Z"))
        assertEquals("PRJ-01", useCase()?.id)
    }

    @Test
    fun returnsNullWhenTodayIsNotInThePlan() = runBlocking {
        val useCase = GetTodayPlanUseCase(repository, clockAt("2027-06-01T03:00:00Z"))
        assertNull(useCase())
    }
}
