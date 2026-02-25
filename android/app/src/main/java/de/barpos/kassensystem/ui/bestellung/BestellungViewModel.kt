package de.barpos.kassensystem.ui.bestellung

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.barpos.kassensystem.domain.model.SKU
import de.barpos.kassensystem.domain.model.Transaktion
import de.barpos.kassensystem.domain.model.TransaktionsPosition
import de.barpos.kassensystem.domain.usecase.bestellung.ArtikelMitSkus
import de.barpos.kassensystem.domain.usecase.bestellung.BeobachteArtikelUndKategorienUseCase
import de.barpos.kassensystem.domain.usecase.bestellung.BerechneWarenkorbUseCase
import de.barpos.kassensystem.domain.usecase.bestellung.EntfernePositionAusTransaktionUseCase
import de.barpos.kassensystem.domain.usecase.bestellung.ErstelleOderHoleTransaktionUseCase
import de.barpos.kassensystem.domain.usecase.bestellung.FuegePositionZuTransaktionHinzuUseCase
import de.barpos.kassensystem.domain.usecase.tischplan.UpdateTischStatusUseCase
import de.barpos.kassensystem.domain.usecase.bediener.BeobachteAktiveSessionUseCase
import de.barpos.kassensystem.domain.usecase.bestellung.BeobachteBestellungUseCase
import de.barpos.kassensystem.domain.usecase.bestellung.Bestellung
import de.barpos.kassensystem.domain.usecase.zahlung.SchliesseTransaktionAbUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BestellungUiState(
    val tischId: Long,
    val tischNummer: String,
    val transaktion: Transaktion? = null,
    val positionen: List<TransaktionsPosition> = emptyList(),
    val artikelByKategorie: Map<String, List<ArtikelMitSkus>> = emptyMap(),
    val warenkorbSumme: Long = 0L,
    val isLoading: Boolean = true
)

@HiltViewModel
class BestellungViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val erstelleOderHoleTransaktionUseCase: ErstelleOderHoleTransaktionUseCase,
    private val beobachteArtikelUndKategorienUseCase: BeobachteArtikelUndKategorienUseCase,
    private val fuegePositionZuTransaktionHinzuUseCase: FuegePositionZuTransaktionHinzuUseCase,
    private val entfernePositionAusTransaktionUseCase: EntfernePositionAusTransaktionUseCase,
    private val berechneWarenkorbUseCase: BerechneWarenkorbUseCase,
    private val updateTischStatusUseCase: UpdateTischStatusUseCase,
    private val beobachteBestellungUseCase: BeobachteBestellungUseCase,
    private val beobachteAktiveSessionUseCase: BeobachteAktiveSessionUseCase
) : ViewModel() {

    private val tischId: Long = checkNotNull(savedStateHandle[KassenNavArgs.TISCH_ID_ARG])
    private val tischNummer: String = checkNotNull(savedStateHandle[KassenNavArgs.TISCH_NUMMER_ARG])

    private val _uiState = MutableStateFlow(BestellungUiState(tischId = tischId, tischNummer = tischNummer))
    val uiState: StateFlow<BestellungUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val session = beobachteAktiveSessionUseCase.invoke().filterNotNull().first()

            val initialTransaktion = erstelleOderHoleTransaktionUseCase(tischId, session.bediener.id, session.schicht.id)

            val bestellungFlow = beobachteBestellungUseCase(initialTransaktion.id).filterNotNull()
            val artikelFlow = beobachteArtikelUndKategorienUseCase()

            combine(bestellungFlow, artikelFlow) { bestellung, artikel ->
                BestellungUiState(
                    tischId = tischId,
                    tischNummer = tischNummer,
                    transaktion = bestellung.transaktion,
                    positionen = bestellung.positionen,
                    artikelByKategorie = artikel,
                    warenkorbSumme = berechneWarenkorbUseCase(bestellung.positionen),
                    isLoading = false
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = _uiState.value
            ).collect {
                _uiState.value = it
            }
        }
    }

    fun addSkuToOrder(sku: SKU) {
        viewModelScope.launch {
            val transaktion = _uiState.value.transaktion ?: return@launch
            fuegePositionZuTransaktionHinzuUseCase(transaktion.id, sku, 1)
        }
    }

    fun removePosition(positionId: Long) {
        viewModelScope.launch {
            entfernePositionAusTransaktionUseCase(positionId)
        }
    }
}
