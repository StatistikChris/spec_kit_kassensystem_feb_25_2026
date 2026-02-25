package de.barpos.kassensystem.data.tse

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import de.barpos.kassensystem.domain.model.TseProtokollEintrag
import de.barpos.kassensystem.domain.model.TseVorgangTyp
import de.barpos.kassensystem.domain.repository.ITseProtokollRepository
import org.threeten.bp.Instant
import javax.inject.Inject
import javax.inject.Singleton

/**
 * T031 — Use-case that orchestrates a TSE sign round-trip.
 *
 * On TSE unavailability, the pending request is written to [TseOfflineBuffer].
 * The [TseOfflineWorker] will drain the buffer when connectivity returns.
 */
@Singleton
class TseSignUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val tseClient: TseClient,
    private val protokollRepository: ITseProtokollRepository,
    private val offlineBuffer: TseOfflineBuffer
) {
    private val credentials: SharedPreferences by lazy {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        EncryptedSharedPreferences.create(
            context,
            "tse_credentials",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private val clientId get() = credentials.getString("tse_client_id", "barpos-kasse-01") ?: "barpos-kasse-01"

    /**
     * Sign a Transaktion's start event.
     * @return the resulting [TseProtokollEintrag] (persisted).
     */
    suspend fun signStart(transaktionId: Long, processData: String): TseProtokollEintrag {
        val startRequest = TseStartRequest(
            clientId = clientId,
            processData = processData
        )
        return try {
            val response = tseClient.startTransaction(startRequest)
            val eintrag = buildEintrag(
                transaktionId = transaktionId,
                typ = TseVorgangTyp.START,
                response = response,
                vorgangsDaten = processData
            )
            protokollRepository.eintragSpeichern(eintrag)
        } catch (e: Exception) {
            handleOffline(transaktionId, processData, TseVorgangTyp.START)
        }
    }

    /**
     * Sign a Transaktion's finish event.
     */
    suspend fun signFinish(transaktionId: Long, txNummer: Long, processData: String): TseProtokollEintrag {
        val finishRequest = TseFinishRequest(
            clientId = clientId,
            transactionNumber = txNummer,
            processData = processData
        )
        return try {
            val response = tseClient.finishTransaction(finishRequest)
            val eintrag = buildEintrag(
                transaktionId = transaktionId,
                typ = TseVorgangTyp.FINISH,
                response = response,
                vorgangsDaten = processData
            )
            protokollRepository.eintragSpeichern(eintrag)
        } catch (e: Exception) {
            offlineBuffer.enqueue(
                TseOfflineBuffer.PendingRequest(
                    transaktionId = transaktionId,
                    processType = "Kassenbeleg-V1",
                    processData = processData,
                    txNummer = txNummer
                )
            )
            protokollPlatzhalter(transaktionId, TseVorgangTyp.FINISH, processData)
        }
    }

    private fun buildEintrag(
        transaktionId: Long,
        typ: TseVorgangTyp,
        response: TseSignResponse,
        vorgangsDaten: String
    ) = TseProtokollEintrag(
        transaktionId = transaktionId,
        typ = typ,
        tseSerialNumber = response.serialNumber,
        signatur = response.signatureBase64,
        zeitpunktStart = Instant.now(),
        zeitpunktEnd = Instant.now(),
        txNummer = response.transactionNumber,
        signaturZaehler = response.signatureCounter,
        vorgangsDaten = vorgangsDaten
    )

    private suspend fun handleOffline(
        transaktionId: Long,
        processData: String,
        typ: TseVorgangTyp
    ): TseProtokollEintrag {
        val placeholder = protokollPlatzhalter(transaktionId, typ, processData)
        offlineBuffer.enqueue(
            TseOfflineBuffer.PendingRequest(
                transaktionId = transaktionId,
                processType = "Kassenbeleg-V1",
                processData = processData,
                txNummer = 0L
            )
        )
        return placeholder
    }

    private suspend fun protokollPlatzhalter(
        transaktionId: Long,
        typ: TseVorgangTyp,
        processData: String
    ): TseProtokollEintrag {
        val now = Instant.now()
        val eintrag = TseProtokollEintrag(
            transaktionId = transaktionId,
            typ = typ,
            tseSerialNumber = "OFFLINE",
            signatur = "OFFLINE_PLACEHOLDER",
            zeitpunktStart = now,
            zeitpunktEnd = now,
            txNummer = protokollRepository.naechsteTxNummer(),
            signaturZaehler = 0L,
            vorgangsDaten = processData
        )
        return protokollRepository.eintragSpeichern(eintrag)
    }
}
