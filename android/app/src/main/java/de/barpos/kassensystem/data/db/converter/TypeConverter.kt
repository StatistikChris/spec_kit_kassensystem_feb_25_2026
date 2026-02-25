package de.barpos.kassensystem.data.db.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.threeten.bp.Instant

/**
 * T014 — Room TypeConverters.
 *
 * Converts types that Room cannot store natively:
 *   - [Instant] ↔ [Long] (epoch-millis; UTC)
 *   - [Map<String,String>] ↔ JSON String (for SKU variant attributes)
 */
class TypeConverter {

    private val gson = Gson()

    // ── Instant ───────────────────────────────────────────────────────────────

    @TypeConverter
    fun instantToLong(instant: Instant?): Long? = instant?.toEpochMilli()

    @TypeConverter
    fun longToInstant(millis: Long?): Instant? = millis?.let { Instant.ofEpochMilli(it) }

    // ── Map<String,String> ────────────────────────────────────────────────────

    @TypeConverter
    fun mapToJson(map: Map<String, String>?): String {
        return gson.toJson(map ?: emptyMap<String, String>())
    }

    @TypeConverter
    fun jsonToMap(json: String?): Map<String, String> {
        if (json.isNullOrBlank()) return emptyMap()
        val type = object : TypeToken<Map<String, String>>() {}.type
        return gson.fromJson(json, type) ?: emptyMap()
    }
}
