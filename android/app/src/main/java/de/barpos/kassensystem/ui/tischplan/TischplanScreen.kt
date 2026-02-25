package de.barpos.kassensystem.ui.tischplan

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import de.barpos.kassensystem.domain.model.Tisch
import de.barpos.kassensystem.domain.model.TischStatus

/**
 * T041 — The main screen for US-1, showing a grid of all tables.
 */
@Composable
fun TischplanScreen(
    viewModel: TischplanViewModel = hiltViewModel(),
    onTischClick: (tischId: Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 128.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(uiState.tische) { tisch ->
                TischKarte(tisch = tisch, onClick = { onTischClick(tisch.id) })
            }
        }
    }
}

/**
 * T041 — A single card representing a table on the grid.
 */
@Composable
fun TischKarte(tisch: Tisch, onClick: () -> Unit) {
    val backgroundColor = when (tisch.status) {
        TischStatus.FREI -> MaterialTheme.colorScheme.surface
        TischStatus.BESETZT -> MaterialTheme.colorScheme.primaryContainer
        TischStatus.RESERVIERT -> Color.Blue.copy(alpha = 0.3f)
    }

    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = tisch.bezeichnung,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
