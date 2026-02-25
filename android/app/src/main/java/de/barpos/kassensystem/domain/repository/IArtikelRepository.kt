package de.barpos.kassensystem.domain.repository

import de.barpos.kassensystem.domain.model.Artikel
import de.barpos.kassensystem.domain.model.SKU
import kotlinx.coroutines.flow.Flow

interface IArtikelRepository {

    suspend fun artikelErstellen(artikel: Artikel): Artikel
    suspend fun artikelAktualisieren(artikel: Artikel)
    suspend fun artikelDeaktivieren(id: Long)
    suspend fun findArtikelById(id: Long): Artikel?
    fun beobachteAktiveArtikel(): Flow<List<Artikel>>
    fun beobachteAlleArtikel(): Flow<List<Artikel>>

    suspend fun skuErstellen(sku: SKU): SKU
    suspend fun skuAktualisieren(sku: SKU)
    suspend fun findSkuById(id: Long): SKU?
    suspend fun skusFuerArtikel(artikelId: Long): List<SKU>
    fun beobachteAktiveSKUs(): Flow<List<SKU>>

    /** Returns 0 on success, -1 if stock insufficient. */
    suspend fun bestandVerringern(skuId: Long, menge: Int): Int
    suspend fun bestandErhoehen(skuId: Long, menge: Int)
    fun beobachteMeldebestandSKUs(): Flow<List<SKU>>
}
