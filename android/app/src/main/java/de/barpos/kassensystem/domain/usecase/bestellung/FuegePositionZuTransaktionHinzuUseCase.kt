package de.barpos.kassensystem.domain.usecase.bestellung

import de.barpos.kassensystem.domain.model.SKU
import de.barpos.kassensystem.domain.model.TransaktionsPosition
import de.barpos.kassensystem.domain.repository.IArtikelRepository
import de.barpos.kassensystem.domain.repository.ITransaktionRepository
import org.threeten.bp.Instant
import javax.inject.Inject

/**
 * T050 — Use-case to add a SKU to a transaction.
 * This also handles updating the total price of the transaction.
 */
class FuegePositionZuTransaktionHinzuUseCase @Inject constructor(
    private val transaktionRepository: ITransaktionRepository,
    private val artikelRepository: IArtikelRepository
) {
    suspend operator fun invoke(transaktionId: Long, sku: SKU, menge: Int) {
        val transaktion = transaktionRepository.findById(transaktionId) ?: return

        // TODO: Implement PreisregelEngine to calculate the actual price
        val einzelpreis = sku.normalpreisInCent
        val gesamtpreis = einzelpreis * menge

        val position = TransaktionsPosition(
            transaktionId = transaktionId,
            skuId = sku.id,
            skuBezeichnung = "${sku.bezeichnung} (${sku.variantenAttributen["groesse"]})",
            menge = menge,
            einzelpreisInCent = einzelpreis,
            gesamtpreisInCent = gesamtpreis,
            mwstSatz = sku.mwstSatz,
            mwstBetragInCent = 0, // TODO: Calculate VAT
            preisregelId = null,
            createdAt = Instant.now()
        )
        transaktionRepository.positionHinzufuegen(position)
        artikelRepository.bestandVerringern(sku.id, menge)

        // Update transaction total
        val allePositionen = transaktionRepository.positionenFuerTransaktion(transaktionId)
        val neuerGesamt = allePositionen.sumOf { it.gesamtpreisInCent }
        transaktionRepository.aktualisieren(transaktion.copy(gesamtInCent = neuerGesamt, updatedAt = Instant.now()))
    }
}
