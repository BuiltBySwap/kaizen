package dev.builtbyswap.kaizen.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.builtbyswap.kaizen.data.AssetPlanRepository
import dev.builtbyswap.kaizen.domain.repository.PlanRepository
import java.time.Clock

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindPlanRepository(impl: AssetPlanRepository): PlanRepository

    companion object {
        @Provides
        fun provideClock(): Clock = Clock.systemDefaultZone()
    }
}
