package de.barpos.kassensystem.data.repository

import de.barpos.kassensystem.data.db.dao.ArtikelDao
import de.barpos.kassensystem.data.db.dao.SkuDao
import de.barpos.kassensystem.data.mapper.toDomain
import de.barpos.kassensystem.data.mapper.toEntity
import de.barpos.kassensystem.domain.model.Artikel
import de.barpos.kassensystem.domain.model.SKU
import de.barpos.kassensystem.domain.repository.IArtikelRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ArtikelRepositoryImpl @Inject constructor(
    private val artikelDao: ArtikelDao,
    private val skuDao: SkuDao
) : IArtikelRepository {

    override suspend fun artikelErstellen(artikel: Artikel): Artikel {
        val id = artikelDao.insert(artikel.toEntity())
        return artikel.copy(id = id)
    }

    override suspend fun artikelAktualisieren(artikel: Artikel) {
        artikelDao.update(artikel.toEntity())
    }

    override suspend fun artikelDeaktivieren(id: Long) {
        artikelDao.deaktivieren(id)
    }

    override suspend fun findArtikelById(id: Long): Artikel? =
        artikelDao.findById(id)?.toDomain()

    override fun beobachteAktiveArtikel(): Flow<List<Artikel>> =
        artikelDao.beobachteAktive().map { it.map { e -> e.toDomain() } }

    override fun beobachteAlleArtikel(): Flow<List<Artikel>> =
        artikelDao.beobachteAlle().map { it.map { e -> e.toDomain() } }

    override suspend fun skuErstellen(sku: SKU): SKU {
        val id = skuDao.insert(sku.toEntity())
        return sku.copy(id = id)
    }

    override suspend fun skuAktualisieren(sku: SKU) {
        skuDao.update(sku.toEntity())
    }

    override suspend fun findSkuById(id: Long): SKU? =
        skuDao.findById(id)?.toDomain()

    override suspend fun skusFuerArtikel(artikelId: Long): List<SKU> =
        skuDao.findByArtikelId(artikelId).map { it.toDomain() }

    override fun beobachteAktiveSKUs(): Flow<List<SKU>> =
        skuDao.beobachteAktive().map { it.map { e -> e.toDomain() } }

    override suspend fun bestandVerringern(skuId: Long, menge: Int): Int =
        skuDao.bestandVerringern(skuId, menge)

    override suspend fun bestandErhoehen(skuId: Long, menge: Int) {
        skuDao.bestandErhoehen(skuId, menge)
    }

    override fun beobachteMeldebestandSKUs(): Flow<List<SKU>> =
        skuDao.beobachteMeldebestands().map { it.map { e -> e.toDomain() } }
}
