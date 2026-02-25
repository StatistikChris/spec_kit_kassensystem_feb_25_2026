package de.barpos.kassensystem.ui.beleg

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Print
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import de.barpos.kassensystem.ui.formatCurrency
import de.barpos.kassensystem.ui.formatDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BelegScreen(
    viewModel: BelegViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Beleg") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Zurück zum Tischplan")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.onPrintBeleg() }) {
                        Icon(Icons.Default.Print, contentDescription = "Beleg drucken")
                    }
                }
            )
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (uiState.belegDaten == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Beleg nicht gefunden.")
            }
        } else {
            val belegDaten = uiState.belegDaten!!
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                item {
                    Text("Bar POS", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Text("Musterstraße 1, 12345 Musterstadt")
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Beleg-Nr: ${belegDaten.beleg.uuid.substring(0, 8)}")
                    Text("Datum: ${formatDateTime(belegDaten.beleg.createdAt)}")
                    Text("Bediener: ${belegDaten.bedienerName}")
                    Spacer(modifier = Modifier.height(16.dp))
                    Divider()
                }

                items(belegDaten.positionen) { position ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(
                            "${position.menge}x ${position.skuBezeichnung}",
                            modifier = Modifier.weight(1f)
                        )
                        Text(formatCurrency(position.gesamtpreisInCent))
                    }
                }

                item {
                    Divider()
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text("Gesamt: ", fontWeight = FontWeight.Bold)
                        Text(formatCurrency(belegDaten.positionen.sumOf { it.gesamtpreisInCent }), fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("TSE Informationen", style = MaterialTheme.typography.titleSmall)
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text("Client-ID: ${belegDaten.beleg.tseClientId}", style = MaterialTheme.typography.bodySmall, fontFamily = FontFamily.Monospace)
                            Text("Transaktion-ID: ${belegDaten.beleg.tseTransaktionId}", style = MaterialTheme.typography.bodySmall, fontFamily = FontFamily.Monospace)
                            Text("Signatur-Zähler: ${belegDaten.beleg.tseSignatureCounter}", style = MaterialTheme.typography.bodySmall, fontFamily = FontFamily.Monospace)
                            Text("Zeitpunkt: ${belegDaten.beleg.tseTimestamp}", style = MaterialTheme.typography.bodySmall, fontFamily = FontFamily.Monospace)
                            Text("Signatur: ${belegDaten.beleg.tseSignature}", style = MaterialTheme.typography.bodySmall, fontFamily = FontFamily.Monospace, maxLines = 2)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onNavigateBack, modifier = Modifier.fillMaxWidth()) {
                        Text("Schließen")
                    }
                }
            }
        }
    }
}
