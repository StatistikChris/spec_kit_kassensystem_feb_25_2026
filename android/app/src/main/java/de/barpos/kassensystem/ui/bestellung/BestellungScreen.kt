package de.barpos.kassensystem.ui.bestellung

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import de.barpos.kassensystem.domain.usecase.bestellung.ArtikelMitSkus
import de.barpos.kassensystem.ui.formatCurrency

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BestellungScreen(
    viewModel: BestellungViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bestellung für Tisch ${uiState.tischNummer}") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Zurück")
                    }
                }
            )
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                ArtikelAuswahl(
                    modifier = Modifier.weight(0.6f),
                    artikelByKategorie = uiState.artikelByKategorie,
                    onSkuSelected = { viewModel.addSkuToOrder(it) }
                )
                Warenkorb(
                    modifier = Modifier
                        .weight(0.4f)
                        .fillMaxHeight(),
                    uiState = uiState,
                    onRemovePosition = { viewModel.removePosition(it) }
                )
            }
        }
    }
}

@Composable
fun ArtikelAuswahl(
    modifier: Modifier = Modifier,
    artikelByKategorie: Map<String, List<ArtikelMitSkus>>,
    onSkuSelected: (de.barpos.kassensystem.domain.model.SKU) -> Unit
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val kategorien = artikelByKategorie.keys.toList()

    Column(modifier = modifier) {
        if (kategorien.isNotEmpty()) {
            TabRow(selectedTabIndex = selectedTabIndex) {
                kategorien.forEachIndexed { index, kategorie ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(kategorie) }
                    )
                }
            }
            LazyColumn(contentPadding = PaddingValues(8.dp)) {
                val artikel = artikelByKategorie[kategorien[selectedTabIndex]] ?: emptyList()
                items(artikel) { artikelMitSkus ->
                    Text(
                        text = artikelMitSkus.artikel.name,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    artikelMitSkus.skus.forEach { sku ->
                        ListItem(
                            headlineContent = { Text(sku.bezeichnung) },
                            supportingContent = { Text(sku.variantenAttributen["groesse"] ?: "") },
                            trailingContent = { Text(formatCurrency(sku.normalpreisInCent)) },
                            modifier = Modifier.clickable { onSkuSelected(sku) }
                        )
                    }
                    Divider()
                }
            }
        }
    }
}

@Composable
fun Warenkorb(
    modifier: Modifier = Modifier,
    uiState: BestellungUiState,
    onRemovePosition: (Long) -> Unit
) {
    Card(modifier = modifier.padding(8.dp)) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                "Warenkorb",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp)
            )
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(uiState.positionen) { position ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "${position.menge}x ${position.skuBezeichnung}",
                            modifier = Modifier.weight(1f)
                        )
                        Text(formatCurrency(position.gesamtpreisInCent))
                        IconButton(onClick = { onRemovePosition(position.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Position entfernen")
                        }
                    }
                }
            }
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Gesamt:", fontWeight = FontWeight.Bold)
                Text(formatCurrency(uiState.warenkorbSumme), fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
            // TODO: Add Buttons for "Parken", "Bezahlen"
        }
    }
}
