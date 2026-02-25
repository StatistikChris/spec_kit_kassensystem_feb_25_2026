package de.barpos.kassensystem.domain.usecase.zahlung

import javax.inject.Inject

/**
 * T057 - Placeholder use-case for printing a receipt.
 * In a real app, this would interact with a printer SDK.
 */
class DruckeBelegUseCase @Inject constructor() {
    fun invoke(belegDaten: BelegDaten) {
        // TODO: Implement receipt printing logic
        println("--- BELEG DRUCKEN ---")
        println("Beleg-ID: ${belegDaten.beleg.uuid}")
        println("Bediener: ${belegDaten.bedienerName}")
        println("---------------------")
        belegDaten.positionen.forEach {
            println("${it.menge}x ${it.skuBezeichnung}\t\t${it.gesamtpreisInCent / 100.0} EUR")
        }
        println("---------------------")
        println("QR Code: ${belegDaten.beleg.qrCodeData}")
        println("--- ENDE ---")
    }
}
