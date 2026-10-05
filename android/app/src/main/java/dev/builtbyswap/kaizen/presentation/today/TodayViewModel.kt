package dev.builtbyswap.kaizen.presentation.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.builtbyswap.kaizen.domain.usecase.GetTodayPlanUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class TodayViewModel @Inject constructor(
    private val getTodayPlan: GetTodayPlanUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(TodayState())
    val state: StateFlow<TodayState> = _state.asStateFlow()

    init {
        onIntent(TodayIntent.Load)
    }

    fun onIntent(intent: TodayIntent) {
        when (intent) {
            TodayIntent.Load -> load()
        }
    }

    private fun load() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val day = getTodayPlan()
                _state.update { it.copy(isLoading = false, day = day) }
            } catch (e: CancellationException) {
                throw e // never swallow cancellation
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message ?: "Could not load the plan") }
            }
        }
    }
}
