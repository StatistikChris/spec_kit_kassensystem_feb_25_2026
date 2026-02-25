package de.barpos.kassensystem.ui.tischplan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.barpos.kassensystem.domain.model.Tisch
import de.barpos.kassensystem.domain.usecase.tischplan.BeobachteTischeUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class TischplanUiState(
    val tische: List<Tisch> = emptyList(),
    val isLoading: Boolean = true
)

/**
 * T041 — ViewModel for the table overview screen.
 * Exposes the list of tables as a [StateFlow].
 */
@HiltViewModel
class TischplanViewModel @Inject constructor(
    beobachteTischeUseCase: BeobachteTischeUseCase
) : ViewModel() {

    val uiState: StateFlow<TischplanUiState> = beobachteTischeUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TischplanUiState(isLoading = true)
        )
}
