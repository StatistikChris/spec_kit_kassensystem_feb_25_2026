package de.barpos.kassensystem.domain.engine

import de.barpos.kassensystem.domain.model.Preisregel
import java.time.Instant
import javax.inject.Inject

class PreisregelEngine @Inject constructor() {
    fun calculatePrice(skuId: Long, basePrice: Long, time: Instant, rules: List<Preisregel>): Long {
        // TODO: Implement engine
        return basePrice
    }
}
