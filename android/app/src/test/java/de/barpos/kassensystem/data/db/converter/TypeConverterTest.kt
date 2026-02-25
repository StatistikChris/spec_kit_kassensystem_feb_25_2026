package de.barpos.kassensystem.data.db.converter

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.threeten.bp.Instant

/**
 * T007 — TypeConverter round-trip tests (Instant ↔ Long, Map ↔ JSON).
 * These tests MUST fail until TypeConverter.kt is created.
 */
class TypeConverterTest {

    private val converter = TypeConverter()

    // ── Instant ↔ Long ─────────────────────────────────────────────────────

    @Test
    fun `Instant to Long returns epoch millis`() {
        val instant = Instant.ofEpochMilli(1_740_434_400_000L) // 2026-02-25 00:00:00 UTC
        assertEquals(1_740_434_400_000L, converter.instantToLong(instant))
    }

    @Test
    fun `Long to Instant returns correct Instant`() {
        val millis = 1_740_434_400_000L
        assertEquals(Instant.ofEpochMilli(millis), converter.longToInstant(millis))
    }

    @Test
    fun `null Long toInstant returns null`() {
        assertNull(converter.longToInstant(null))
    }

    @Test
    fun `null Instant toLong returns null`() {
        assertNull(converter.instantToLong(null))
    }

    // ── Map ↔ JSON ──────────────────────────────────────────────────────────

    @Test
    fun `Map to JSON and back round-trips correctly`() {
        val original = mapOf("groesse" to "0.5l", "farbe" to "hell")
        val json = converter.mapToJson(original)
        val restored = converter.jsonToMap(json)
        assertEquals(original, restored)
    }

    @Test
    fun `empty Map to JSON and back returns empty map`() {
        val json = converter.mapToJson(emptyMap())
        val restored = converter.jsonToMap(json)
        assertEquals(emptyMap<String, String>(), restored)
    }

    @Test
    fun `null JSON to Map returns empty map`() {
        assertEquals(emptyMap<String, String>(), converter.jsonToMap(null))
    }
}
