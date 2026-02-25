package de.barpos.kassensystem.data.tse

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import de.barpos.kassensystem.domain.repository.ITseProtokollRepository

/**
 * T030 — Periodic WorkManager worker that drains the [TseOfflineBuffer].
 * Runs every 15 minutes when the device has network access.
 * Scheduled from [de.barpos.kassensystem.di.WorkerModule].
 */
@HiltWorker
class TseOfflineWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val tseClient: TseClient,
    private val buffer: TseOfflineBuffer,
    private val protokollRepository: ITseProtokollRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val pending = buffer.peekAll()
        if (pending.isEmpty()) return Result.success()

        return try {
            for (req in pending) {
                tseClient.finishTransaction(
                    TseFinishRequest(
                        clientId = req.transaktionId.toString(),
                        transactionNumber = req.txNummer,
                        processData = req.processData
                    )
                )
                // In production we'd also update the TseProtokollEintrag with the real signature
            }
            buffer.clear()
            Result.success()
        } catch (e: Exception) {
            // Retry on next schedule; WorkManager handles back-off
            Result.retry()
        }
    }
}
