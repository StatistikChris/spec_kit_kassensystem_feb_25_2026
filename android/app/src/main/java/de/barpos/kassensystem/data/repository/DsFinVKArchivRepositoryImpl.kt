package de.barpos.kassensystem.data.repository

import de.barpos.kassensystem.data.db.dao.DsFinVKArchivSatzDao
import de.barpos.kassensystem.data.db.dao.TransaktionDao
import de.barpos.kassensystem.data.db.entity.DsFinVKArchivSatzEntity
import de.barpos.kassensystem.domain.repository.IDsFinVKArchivRepository
import org.threeten.bp.Instant
import java.io.OutputStream
import javax.inject.Inject

class DsFinVKArchivRepositoryImpl @Inject constructor(
    private val archivDao: DsFinVKArchivSatzDao,
    private val transaktionDao: TransaktionDao
) : IDsFinVKArchivRepository {

    override suspend fun transaktionArchivieren(transaktionId: Long) {
        if (archivDao.existsFuerTransaktion(transaktionId)) return
        val entity = transaktionDao.findById(transaktionId) ?: return

        // Build minimal DSFinV-K row JSON — full implementation in Phase 9 (US-7)
        val csvRow = buildString {
            append("{\"Z_KASSE_ID\":\"barpos-01\"")
            append(",\"Z_ERSTELLUNG\":\"${Instant.ofEpochMilli(entity.createdAt)}\"")
            append(",\"Z_NR\":\"${entity.id}\"")
            append(",\"BON_ID\":\"${entity.uuid}\"")
            append(",\"BON_TYP\":\"${entity.status}\"")
            append(",\"UMSATZ_BRUTTO\":\"${entity.gesamtInCent}\"}")
        }

        archivDao.insert(
            DsFinVKArchivSatzEntity(
                transaktionId = transaktionId,
                csvRowJson = csvRow,
                exportZeit = Instant.now().toEpochMilli()
            )
        )
        transaktionDao.lock(transaktionId)
    }

    override suspend fun istArchiviert(transaktionId: Long): Boolean =
        archivDao.existsFuerTransaktion(transaktionId)

    override suspend fun exportiereZeitraum(vonMillis: Long, bisMillis: Long, outputStream: OutputStream) {
        val saetze = archivDao.findInZeitraum(vonMillis, bisMillis)
        outputStream.bufferedWriter().use { writer ->
            writer.write("Z_KASSE_ID,Z_ERSTELLUNG,Z_NR,BON_ID,BON_TYP,UMSATZ_BRUTTO\n")
            for (satz in saetze) {
                // Full DSFinV-K CSV rendering delegated to Phase 9 implementation
                writer.write("${satz.csvRowJson}\n")
            }
        }
    }
}
