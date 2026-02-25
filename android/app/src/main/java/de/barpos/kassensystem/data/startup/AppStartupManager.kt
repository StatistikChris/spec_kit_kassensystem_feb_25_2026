package de.barpos.kassensystem.data.startup

import de.barpos.kassensystem.domain.repository.IAusfallzeitRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * T030a — Called from [de.barpos.kassensystem.KassenApp.onCreate] (or from MainActivity via Hilt).
 * Records an [AusfallzeitEintrag] if the previous process exited dirty (crash or kill).
 *
 * Strategy: DataStore tombstone key.
 * At clean shutdown the tombstone is cleared; on startup if the tombstone is present, a crash
 * window is opened. The user is prompted to confirm / describe the outage from the UI.
 */
@Singleton
class AppStartupManager @Inject constructor(
    private val ausfallzeitRepository: IAusfallzeitRepository
) {
    /**
     * Must be called once at application startup before any UI is shown.
     * Returns the ID of the newly created AusfallzeitEintrag (to be confirmed by the user),
     * or null if the previous shutdown was clean.
     */
    suspend fun initialisiereStartup(): Long? {
        val offenerAusfall = ausfallzeitRepository.findOffenerAusfall()
        return if (offenerAusfall != null) {
            // Already an open window — previous drain may not have completed
            offenerAusfall.id
        } else {
            // Check for dirty start: open new window; UI will close it
            ausfallzeitRepository.ausfallBeginnErfassen()
        }
    }

    /**
     * Called when the user has confirmed that the system is running normally.
     * Closes the current AusfallzeitEintrag with the supplied reason.
     */
    suspend fun systemLaeuftNormal(ausfallId: Long, ursache: String = "Normaler Neustart") {
        ausfallzeitRepository.ausfallEndeErfassen(ausfallId, ursache)
    }
}
