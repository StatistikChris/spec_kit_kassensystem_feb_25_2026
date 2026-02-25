package de.barpos.kassensystem.ui.beleg

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.barpos.kassensystem.domain.usecase.zahlung.BelegDaten
import de.barpos.kassensystem.domain.usecase.zahlung.DruckeBelegUseCase
import de.barpos.kassensystem.domain.usecase.zahlung.LadeBelegDatenUseCase
import de.barpos.kassensystem.ui.navigation.KassenNavArgs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BelegUiState(
    val belegDaten: BelegDaten? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class BelegViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val ladeBelegDatenUseCase: LadeBelegDatenUseCase,
    private val druckeBelegUseCase: DruckeBelegUseCase
) : ViewModel() {

    private val belegId: Long = checkNotNull(savedStateHandle[KassenNavArgs.BELEG_ID_ARG])

    private val _uiState = MutableStateFlow(BelegUiState())
    val uiState: StateFlow<BelegUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val daten = ladeBelegDatenUseCase(belegId)
            _uiState.value = BelegUiState(belegDaten = daten, isLoading = false)
        }
    }

    fun onPrintBeleg() {
        _uiState.value.belegDaten?.let {
            druckeBelegUseCase.invoke(it)
        }
    }
}
