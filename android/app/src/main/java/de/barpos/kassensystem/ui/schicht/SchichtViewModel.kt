package de.barpos.kassensystem.ui.schicht

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SchichtViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SchichtUiState())
    val uiState: StateFlow<SchichtUiState> = _uiState

    // TODO: Implement viewmodel logic
}

data class SchichtUiState(
    val currentBediener: String? = null,
    val shiftStartTime: Long? = null
)
