package de.barpos.kassensystem.domain

import java.math.BigDecimal
import java.math.RoundingMode

/**
 * T013 — Pure integer-cent arithmetic utilities.
 *
 * ALL monetary computations go through this object.
 * No Float or Double is ever used for money (constitution mandate, FR-002).
 *
 * Display formatting uses HALF_UP rounding only at the last step.
 */
object CentMath {

    // ── Arithmetic ────────────────────────────────────────────────────────────

    fun multiply(priceInCent: Long, quantity: Int): Long = priceInCent * quantity

    fun add(a: Long, b: Long): Long = a + b

    fun subtract(a: Long, b: Long): Long = a - b

    fun sum(values: Iterable<Long>): Long = values.fold(0L, ::add)

    // ── VAT (gross method — all prices are gross) ────────────────────────────

    /**
     * Extract the VAT amount from a gross price.
     * Formula: VAT = gross − gross / (1 + rate/100), rounded HALF_UP.
     */
    fun vatAmountFromGross(grossInCent: Long, vatRatePercent: Int): Long {
        require(vatRatePercent in 0..100) { "VAT rate must be 0–100" }
        val gross = BigDecimal.valueOf(grossInCent)
        val divisor = BigDecimal.ONE + BigDecimal.valueOf(vatRatePercent.toLong(), 2)
        val net = gross.divide(divisor, 10, RoundingMode.HALF_UP)
        val vat = gross - net
        return vat.setScale(0, RoundingMode.HALF_UP).toLong()
    }

    /**
     * Net price (excl. VAT) from a gross price.
     */
    fun netFromGross(grossInCent: Long, vatRatePercent: Int): Long {
        return grossInCent - vatAmountFromGross(grossInCent, vatRatePercent)
    }

    // ── Discount ──────────────────────────────────────────────────────────────

    /**
     * Apply a percentage discount to a gross price.
     * E.g. 20 % off 500 ct → 400 ct.
     */
    fun applyPercentDiscount(priceInCent: Long, discountPercent: Int): Long {
        require(discountPercent in 0..100) { "Discount percent must be 0–100" }
        val factor = BigDecimal(100 - discountPercent)
        return (BigDecimal.valueOf(priceInCent) * factor)
            .divide(BigDecimal(100), 0, RoundingMode.HALF_UP)
            .toLong()
    }

    /**
     * Subtract a fixed amount (floor at 0 — price cannot go negative).
     */
    fun applyFixedDiscount(priceInCent: Long, discountInCent: Long): Long {
        return maxOf(0L, priceInCent - discountInCent)
    }

    // ── Display formatting ─────────────────────────────────────────────────────

    /**
     * Format euro-cent as "12,34" (German locale, no currency symbol).
     * Used only for display — never for further arithmetic.
     */
    fun formatEuro(cents: Long): String {
        val absValue = Math.abs(cents)
        val euros = absValue / 100
        val remainder = absValue % 100
        val sign = if (cents < 0) "-" else ""
        return "%s%d,%02d".format(sign, euros, remainder)
    }

    /**
     * Format euro-cent as "12,34 €".
     */
    fun formatEuroWithSymbol(cents: Long): String = "${formatEuro(cents)} €"
}
