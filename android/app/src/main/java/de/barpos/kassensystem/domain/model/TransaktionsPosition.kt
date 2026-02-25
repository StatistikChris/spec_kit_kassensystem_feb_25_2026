package de.barpos.kassensystem.domain.model

import org.threeten.bp.Instant

/**
 * A single line item within a [Transaktion].
 *
 * [preisInCent] is a snapshot of the price at time of booking (including any applied
 * [preisregelId] discount). It MUST NOT be recalculated after the transaction is closed.
 */
data class TransaktionsPosition(
    val id: Long = 0,
    val transaktionId: Long,
    val skuId: Long,
    val skuBezeichnung: String,                // snapshot of SKU name at booking time
    val menge: Int,
    val einzelpreisInCent: Long,               // per-unit price (after discount applied)
    val gesamtpreisInCent: Long,               // einzelpreisInCent × menge
    val mwstSatz: Int,                         // 7 or 19
    val mwstBetragInCent: Long,                // pre-computed VAT amount (gross method)
    val preisregelId: Long? = null,            // if discount was applied, reference the rule
    val createdAt: Instant
)
