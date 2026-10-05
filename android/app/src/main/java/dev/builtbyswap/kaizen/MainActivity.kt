package dev.builtbyswap.kaizen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import dev.builtbyswap.kaizen.presentation.today.TodayScreen
import dev.builtbyswap.kaizen.presentation.today.TodayViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: TodayViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Swap MaterialTheme for the generated KaizenTheme later.
            MaterialTheme {
                val state by viewModel.state.collectAsState()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TodayScreen(
                        state = state,
                        onIntent = viewModel::onIntent,
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}
