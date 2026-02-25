package de.barpos.kassensystem.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

/**
 * T008 — CentMath integer arithmetic tests.
 * All monetary calculations in the system use Long (euro-cent) — no floating-point.
 * Tests MUST fail until CentMath.kt is created.
 */
class CentMathTest {

    // ── Basic arithmetic ─────────────────────────────────────────────────────

    @Test
    fun `multiply price by quantity returns correct cents`() {
        // 4.80 € × 3 = 14.40 €
        assertEquals(1_440L, CentMath.multiply(480L, 3))
    }

    @Test
    fun `add two cent amounts`() {
        assertEquals(710L, CentMath.add(480L, 230L))
    }

    @Test
    fun `subtract cent amounts`() {
        assertEquals(250L, CentMath.subtract(480L, 230L))
    }

    // ── VAT calculation ──────────────────────────────────────────────────────

    @Test
    fun `vatAmount for 19 percent on 119 cents is 19 cents`() {
        // gross 119 ct → VAT = gross - gross/1.19 = 19 ct
        assertEquals(19L, CentMath.vatAmountFromGross(119L, 19))
    }

    @Test
    fun `vatAmount for 7 percent on 107 cents is 7 cents`() {
        assertEquals(7L, CentMath.vatAmountFromGross(107L, 7))
    }

    @Test
    fun `netFromGross 19 percent on 119 cents is 100 cents`() {
        assertEquals(100L, CentMath.netFromGross(119L, 19))
    }

    // ── Discount ─────────────────────────────────────────────────────────────

    @Test
    fun `applyPercentDiscount 20 percent off 500 cents returns 400 cents`() {
        assertEquals(400L, CentMath.applyPercentDiscount(500L, 20))
    }

    @Test
    fun `applyPercentDiscount 100 percent returns 0`() {
        assertEquals(0L, CentMath.applyPercentDiscount(500L, 100))
    }

    @Test
    fun `applyPercentDiscount negative throws IllegalArgumentException`() {
        assertThrows<IllegalArgumentException> {
            CentMath.applyPercentDiscount(500L, -1)
        }
    }

    @Test
    fun `applyPercentDiscount over 100 throws IllegalArgumentException`() {
        assertThrows<IllegalArgumentException> {
            CentMath.applyPercentDiscount(500L, 101)
        }
    }

    // ── Display formatting ────────────────────────────────────────────────────

    @Test
    fun `formatEuro converts 100 cents to 1,00`() {
        assertEquals("1,00", CentMath.formatEuro(100L))
    }

    @Test
    fun `formatEuro converts 1234 cents to 12,34`() {
        assertEquals("12,34", CentMath.formatEuro(1_234L))
    }

    @Test
    fun `formatEuro handles zero`() {
        assertEquals("0,00", CentMath.formatEuro(0L))
    }
}
