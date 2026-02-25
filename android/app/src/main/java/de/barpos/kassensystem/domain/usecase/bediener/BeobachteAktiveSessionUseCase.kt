package de.barpos.kassensystem.domain.usecase.bediener

import de.barpos.kassensystem.domain.model.Bediener
import de.barpos.kassensystem.domain.model.Schicht
import de.barpos.kassensystem.domain.repository.IBedienerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

data class AktiveSession(
    val bediener: Bediener,
    val schicht: Schicht
)

/**
 * T053 - Use-case to observe the currently active user session (Bediener + Schicht).
 * In a real app, this would involve some authentication logic.
 * For now, we'll just fetch the first user and their latest shift.
 */
class BeobachteAktiveSessionUseCase @Inject constructor(
    private val bedienerRepository: IBedienerRepository
) {
    fun invoke(): Flow<AktiveSession?> {
        // Simplified: We assume user with ID 1 is logged in and we take their latest shift.
        val bedienerId = 1L
        return combine(
            bedienerRepository.beobachteBediener(bedienerId),
            bedienerRepository.beobachteLetzteSchicht(bedienerId)
        ) { bediener, schicht ->
            if (bediener != null && schicht != null) {
                AktiveSession(bediener, schicht)
            } else {
                null
            }
        }
    }
}
