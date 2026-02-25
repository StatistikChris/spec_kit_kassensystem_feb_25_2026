package de.barpos.kassensystem.domain.repository

import de.barpos.kassensystem.domain.model.TseProtokollEintrag

interface ITseProtokollRepository {

    suspend fun eintragSpeichern(eintrag: TseProtokollEintrag): TseProtokollEintrag
    suspend fun findByTransaktionId(transaktionId: Long): List<TseProtokollEintrag>
    suspend fun naechsteTxNummer(): Long
}
