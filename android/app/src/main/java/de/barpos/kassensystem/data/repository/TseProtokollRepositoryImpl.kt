package de.barpos.kassensystem.data.repository

import de.barpos.kassensystem.data.db.dao.TseProtokollEintragDao
import de.barpos.kassensystem.data.mapper.toDomain
import de.barpos.kassensystem.data.mapper.toEntity
import de.barpos.kassensystem.domain.model.TseProtokollEintrag
import de.barpos.kassensystem.domain.repository.ITseProtokollRepository
import javax.inject.Inject

class TseProtokollRepositoryImpl @Inject constructor(
    private val dao: TseProtokollEintragDao
) : ITseProtokollRepository {

    override suspend fun eintragSpeichern(eintrag: TseProtokollEintrag): TseProtokollEintrag {
        val id = dao.insert(eintrag.toEntity())
        return eintrag.copy(id = id)
    }

    override suspend fun findByTransaktionId(transaktionId: Long): List<TseProtokollEintrag> =
        dao.findByTransaktionId(transaktionId).map { it.toDomain() }

    override suspend fun naechsteTxNummer(): Long =
        (dao.maxTxNummer() ?: 0L) + 1L
}
