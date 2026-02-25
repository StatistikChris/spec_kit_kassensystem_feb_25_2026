package de.barpos.kassensystem.ui.kasse

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class TransaktionViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(TransaktionUiState())
    val uiState: StateFlow<TransaktionUiState> = _uiState

    // TODO: Implement viewmodel logic
}

data class TransaktionUiState(
    val positions: List<Any> = emptyList(),
    val total: Long = 0,
    val vat7: Long = 0,
    val vat19: Long = 0,
    val paymentMode: String = "cash",
    val direktkassierung: Boolean = false
)
