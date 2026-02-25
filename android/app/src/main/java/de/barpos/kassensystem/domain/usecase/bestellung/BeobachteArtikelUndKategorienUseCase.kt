package de.barpos.kassensystem.domain.usecase.bestellung

import de.barpos.kassensystem.domain.model.Artikel
import de.barpos.kassensystem.domain.model.SKU
import de.barpos.kassensystem.domain.repository.IArtikelRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

data class ArtikelMitSkus(
    val artikel: Artikel,
    val skus: List<SKU>
)

/**
 * T048 — Use-case to observe all active articles and their SKUs, grouped by category.
 */
class BeobachteArtikelUndKategorienUseCase @Inject constructor(
    private val artikelRepository: IArtikelRepository
) {
    operator fun invoke(): Flow<Map<String, List<ArtikelMitSkus>>> {
        return artikelRepository.beobachteAktiveArtikel().combine(artikelRepository.beobachteAktiveSKUs()) { artikel, skus ->
            val skusByArtikelId = skus.groupBy { it.artikelId }
            artikel
                .map { art -> ArtikelMitSkus(art, skusByArtikelId[art.id] ?: emptyList()) }
                .filter { it.skus.isNotEmpty() }
                .groupBy { it.artikel.kategorie }
        }
    }
}
