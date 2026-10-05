package dev.builtbyswap.kaizen.presentation.today

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.builtbyswap.kaizen.domain.model.PlanDay

/** Stateless: draws a [TodayState] and reports user actions as [TodayIntent]s. */
@Composable
fun TodayScreen(
    state: TodayState,
    onIntent: (TodayIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val day = state.day
    val error = state.error
    when {
        state.isLoading -> Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }

        error != null -> Column(
            modifier = modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(error)
            Spacer(Modifier.height(12.dp))
            Button(onClick = { onIntent(TodayIntent.Load) }) { Text("Retry") }
        }

        day == null -> Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Nothing is scheduled for today.")
        }

        else -> DayContent(day, modifier)
    }
}

@Composable
private fun DayContent(day: PlanDay, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Column {
                Text("${day.id} · ${day.date}", style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.height(4.dp))
                Text(day.topic, style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(4.dp))
                Text("${day.track} · ${day.priority} · ${day.load} · ${day.hours} h")
            }
        }
        item { Section("Why it exists", day.why) }
        item { Section("Steps", day.steps.joinToString("\n\n")) }
        item { Section("Gemini tool", day.tool) }
        item { Section("Mobile Exploration question", day.mobileQuestion) }
        item { Section("LeetCode", day.leetCode) }
        item { Section("English + thinking", day.english) }
    }
}

@Composable
private fun Section(title: String, body: String) {
    if (body.isBlank()) return
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(4.dp))
            Text(body)
        }
    }
}
