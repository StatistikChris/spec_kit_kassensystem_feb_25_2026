package de.barpos.kassensystem.data.hardware

import de.barpos.kassensystem.domain.model.Bon
import de.barpos.kassensystem.domain.model.Schicht
import de.barpos.kassensystem.domain.model.ZBon
import de.barpos.kassensystem.domain.repository.IBonDrucker
import javax.inject.Inject

class BonDruckerImpl @Inject constructor() : IBonDrucker {
    override suspend fun printBon(bon: Bon) {
        TODO("Not yet implemented")
    }

    override suspend fun printZBon(zBon: ZBon) {
        TODO("Not yet implemented")
    }

    override suspend fun printSchichtbericht(schicht: Schicht) {
        TODO("Not yet implemented")
    }
}
