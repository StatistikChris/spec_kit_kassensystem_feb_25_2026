package de.barpos.kassensystem.domain.repository

import java.io.OutputStream

interface IDsFinVKArchivRepository {

    /** Locks a Transaktion and writes its DSFinV-K shadow row atomically. */
    suspend fun transaktionArchivieren(transaktionId: Long)

    /** Returns true if the transaction already has an archive row. */
    suspend fun istArchiviert(transaktionId: Long): Boolean

    /**
     * Streams all archive rows for the given period as DSFinV-K 2.3 CSV to [outputStream].
     * Caller is responsible for closing the stream.
     */
    suspend fun exportiereZeitraum(vonMillis: Long, bisMillis: Long, outputStream: OutputStream)
}
