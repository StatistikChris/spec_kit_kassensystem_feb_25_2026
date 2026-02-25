package de.barpos.kassensystem.data.tse

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

private val gson = Gson()

private val Context.tseBufferStore by preferencesDataStore(name = "tse_offline_buffer")

private val KEY_PENDING_COUNT = longPreferencesKey("pending_count")
private val KEY_PENDING_JSON = stringPreferencesKey("pending_json")

/**
 * T029 — Durable offline buffer for TSE sign requests.
 * Persisted in DataStore so pending requests survive process death.
 * WorkManager's [TseOfflineWorker] drains this buffer when connectivity returns.
 */
@Singleton
class TseOfflineBuffer @Inject constructor(
    @ApplicationContext private val context: Context
) {
    data class PendingRequest(
        val transaktionId: Long,
        val processType: String,
        val processData: String,
        val txNummer: Long
    )

    suspend fun enqueue(request: PendingRequest) {
        context.tseBufferStore.edit { prefs ->
            val existing = prefs[KEY_PENDING_JSON]?.let {
                val type = object : TypeToken<List<PendingRequest>>() {}.type
                runCatching { gson.fromJson<List<PendingRequest>>(it, type) }.getOrDefault(emptyList())
            } ?: emptyList()
            val updated = existing + request
            prefs[KEY_PENDING_JSON] = gson.toJson(updated)
            prefs[KEY_PENDING_COUNT] = updated.size.toLong()
        }
    }

    suspend fun peekAll(): List<PendingRequest> {
        val prefs = context.tseBufferStore.data.first()
        return prefs[KEY_PENDING_JSON]?.let {
            val type = object : TypeToken<List<PendingRequest>>() {}.type
            runCatching { gson.fromJson<List<PendingRequest>>(it, type) }.getOrDefault(emptyList())
        } ?: emptyList()
    }

    suspend fun clear() {
        context.tseBufferStore.edit { prefs ->
            prefs.remove(KEY_PENDING_JSON)
            prefs[KEY_PENDING_COUNT] = 0L
        }
    }

    suspend fun pendingCount(): Long {
        val prefs = context.tseBufferStore.data.first()
        return prefs[KEY_PENDING_COUNT] ?: 0L
    }
}
