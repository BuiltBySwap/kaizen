package dev.builtbyswap.kaizen.domain.repository

import dev.builtbyswap.kaizen.domain.model.PlanDay

interface PlanRepository {
    suspend fun getAll(): List<PlanDay>
}
