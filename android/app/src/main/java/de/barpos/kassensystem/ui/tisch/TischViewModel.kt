package de.barpos.kassensystem.ui.tisch

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class TischViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(TischUiState())
    val uiState: StateFlow<TischUiState> = _uiState

    // TODO: Implement viewmodel logic
}

data class TischUiState(
    val tables: List<Any> = emptyList()
)
