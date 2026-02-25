package de.barpos.kassensystem

import android.app.Application
import androidx.work.WorkManager
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp
import de.barpos.kassensystem.di.WorkerModule
import javax.inject.Inject

/**
 * Application entry point.
 *
 * Responsibilities:
 * - Initialise Hilt dependency-injection graph.
 * - Bootstrap ThreeTenABP (java.time backport) for java.time / Instant support on API 29+.
 * - Schedule [TseOfflineWorker] via WorkManager for periodic offline-TSE drain.
 * - Trigger [AppStartupManager] to record any outage interval since last shutdown (EC-06, FR-016).
 *
 * NOTE: allowBackup is intentionally disabled in AndroidManifest.xml to comply with
 * GoBD immutability requirements (§ 147 AO). The DSFinV-K export is the only approved
 * archival mechanism.
 */
@HiltAndroidApp
class KassenApp : Application() {

    @Inject lateinit var workManager: WorkManager

    override fun onCreate() {
        super.onCreate()
        // ThreeTenABP: must be initialised before any Instant / ZonedDateTime usage.
        AndroidThreeTen.init(this)
        // Schedule periodic TSE drain worker (runs every 15 min when network is available).
        WorkerModule.scheduleTseDrainWorker(workManager)
    }
}
