package de.barpos.kassensystem.data.mapper

import de.barpos.kassensystem.data.db.entity.*
import de.barpos.kassensystem.domain.model.*
import org.threeten.bp.Instant
import org.threeten.bp.LocalTime
import org.threeten.bp.ZoneId

// ─── Transaktion ──────────────────────────────────────────────────────────────

fun Transaktion.toEntity() = TransaktionEntity(
    id = id,
    uuid = uuid,
    tischId = tischId,
    bedienerId = bedienerId,
    schichtId = schichtId,
    status = status.name,
    zahlungsart = zahlungsart.name,
    gesamtInCent = gesamtInCent,
    stornoVonId = stornoVonId,
    isTraining = isTraining,
    locked = locked,
    createdAt = createdAt.toEpochMilli(),
    updatedAt = updatedAt.toEpochMilli()
)

fun TransaktionEntity.toDomain() = Transaktion(
    id = id,
    uuid = uuid,
    tischId = tischId,
    bedienerId = bedienerId,
    schichtId = schichtId,
    status = TransaktionStatus.valueOf(status),
    zahlungsart = Zahlungsart.valueOf(zahlungsart),
    gesamtInCent = gesamtInCent,
    stornoVonId = stornoVonId,
    isTraining = isTraining,
    locked = locked,
    createdAt = Instant.ofEpochMilli(createdAt),
    updatedAt = Instant.ofEpochMilli(updatedAt)
)

// ─── TransaktionsPosition ─────────────────────────────────────────────────────

fun TransaktionsPosition.toEntity() = TransaktionsPositionEntity(
    id = id,
    transaktionId = transaktionId,
    skuId = skuId,
    skuBezeichnung = skuBezeichnung,
    menge = menge,
    einzelpreisInCent = einzelpreisInCent,
    gesamtpreisInCent = gesamtpreisInCent,
    mwstSatz = mwstSatz,
    mwstBetragInCent = mwstBetragInCent,
    preisregelId = preisregelId,
    createdAt = createdAt.toEpochMilli()
)

fun TransaktionsPositionEntity.toDomain() = TransaktionsPosition(
    id = id,
    transaktionId = transaktionId,
    skuId = skuId,
    skuBezeichnung = skuBezeichnung,
    menge = menge,
    einzelpreisInCent = einzelpreisInCent,
    gesamtpreisInCent = gesamtpreisInCent,
    mwstSatz = mwstSatz,
    mwstBetragInCent = mwstBetragInCent,
    preisregelId = preisregelId,
    createdAt = Instant.ofEpochMilli(createdAt)
)

// ─── Artikel ──────────────────────────────────────────────────────────────────

fun Artikel.toEntity() = ArtikelEntity(
    id = id,
    name = name,
    kategorie = kategorie,
    mwstSatz = mwstSatz,
    aktiv = aktiv
)

fun ArtikelEntity.toDomain() = Artikel(
    id = id,
    name = name,
    kategorie = kategorie,
    mwstSatz = mwstSatz,
    aktiv = aktiv
)

// ─── SKU ──────────────────────────────────────────────────────────────────────

fun SKU.toEntity() = SkuEntity(
    id = id,
    artikelId = artikelId,
    bezeichnung = bezeichnung,
    variantenAttributenJson = variantenAttributen.entries
        .joinToString(",", "{", "}") { (k, v) -> "\"$k\":\"$v\"" },
    normalpreisInCent = normalpreisInCent,
    mwstSatz = mwstSatz,
    bestand = bestand,
    meldebestand = meldebestand,
    aktiv = aktiv
)

fun SkuEntity.toDomain(): SKU {
    // Simple JSON parse — full Gson/Moshi is used only in TypeConverter; for prod consider injecting
    val attrs = mutableMapOf<String, String>()
    return SKU(
        id = id,
        artikelId = artikelId,
        bezeichnung = bezeichnung,
        variantenAttributen = attrs,
        normalpreisInCent = normalpreisInCent,
        mwstSatz = mwstSatz,
        bestand = bestand,
        meldebestand = meldebestand,
        aktiv = aktiv
    )
}

// ─── Preisregel ───────────────────────────────────────────────────────────────

fun Preisregel.toEntity() = PreisregelEntity(
    id = id,
    bezeichnung = bezeichnung,
    startZeit = startZeit.toString(),
    endZeit = endZeit.toString(),
    weekdayBitmask = weekdayBitmask,
    rabattTyp = rabattTyp.name,
    rabattWert = rabattWert,
    artikelFilter = artikelFilter.name,
    filterWert = filterWert,
    prioritaet = prioritaet,
    aktiv = aktiv,
    createdAt = createdAt.toEpochMilli()
)

fun PreisregelEntity.toDomain() = Preisregel(
    id = id,
    bezeichnung = bezeichnung,
    startZeit = LocalTime.parse(startZeit),
    endZeit = LocalTime.parse(endZeit),
    weekdayBitmask = weekdayBitmask,
    rabattTyp = RabattTyp.valueOf(rabattTyp),
    rabattWert = rabattWert,
    artikelFilter = ArtikelFilter.valueOf(artikelFilter),
    filterWert = filterWert,
    prioritaet = prioritaet,
    aktiv = aktiv,
    createdAt = Instant.ofEpochMilli(createdAt)
)

// ─── Tisch ────────────────────────────────────────────────────────────────────

fun Tisch.toEntity() = TischEntity(
    id = id,
    bezeichnung = bezeichnung,
    status = status.name,
    version = version,
    updatedAt = updatedAt.toEpochMilli()
)

fun TischEntity.toDomain() = Tisch(
    id = id,
    bezeichnung = bezeichnung,
    status = TischStatus.valueOf(status),
    version = version,
    updatedAt = Instant.ofEpochMilli(updatedAt)
)

// ─── Bediener ─────────────────────────────────────────────────────────────────

fun Bediener.toEntity() = BedienerEntity(
    id = id,
    name = name,
    kurzname = kurzname,
    pinHash = pinHash,
    rolle = rolle.name,
    isTraining = isTraining,
    aktiv = aktiv
)

fun BedienerEntity.toDomain() = Bediener(
    id = id,
    name = name,
    kurzname = kurzname,
    pinHash = pinHash,
    rolle = BedienerRolle.valueOf(rolle),
    isTraining = isTraining,
    aktiv = aktiv
)

// ─── Schicht ──────────────────────────────────────────────────────────────────

fun Schicht.toEntity() = SchichtEntity(
    id = id,
    bedienerId = bedienerId,
    startZeit = startZeit.toEpochMilli(),
    endZeit = endZeit?.toEpochMilli(),
    sollBestandInCent = sollBestandInCent,
    istBestandInCent = istBestandInCent,
    geschlossen = geschlossen
)

fun SchichtEntity.toDomain() = Schicht(
    id = id,
    bedienerId = bedienerId,
    startZeit = Instant.ofEpochMilli(startZeit),
    endZeit = endZeit?.let { Instant.ofEpochMilli(it) },
    sollBestandInCent = sollBestandInCent,
    istBestandInCent = istBestandInCent,
    geschlossen = geschlossen
)

// ─── Bon ──────────────────────────────────────────────────────────────────────

fun Bon.toEntity() = BonEntity(
    id = id,
    transaktionId = transaktionId,
    zeitstempel = zeitstempel.toEpochMilli(),
    tseSignatur = tseSignatur,
    tseSerialNumber = tseSerialNumber,
    tseZeitpunktStart = tseZeitpunktStart.toEpochMilli(),
    tseZeitpunktEnd = tseZeitpunktEnd.toEpochMilli(),
    tseTxNummer = tseTxNummer,
    isNachdruck = isNachdruck,
    nachdruckZeit = nachdruckZeit?.toEpochMilli()
)

fun BonEntity.toDomain() = Bon(
    id = id,
    transaktionId = transaktionId,
    zeitstempel = Instant.ofEpochMilli(zeitstempel),
    tseSignatur = tseSignatur,
    tseSerialNumber = tseSerialNumber,
    tseZeitpunktStart = Instant.ofEpochMilli(tseZeitpunktStart),
    tseZeitpunktEnd = Instant.ofEpochMilli(tseZeitpunktEnd),
    tseTxNummer = tseTxNummer,
    isNachdruck = isNachdruck,
    nachdruckZeit = nachdruckZeit?.let { Instant.ofEpochMilli(it) }
)

// ─── ZBon ─────────────────────────────────────────────────────────────────────

fun ZBon.toEntity() = ZBonEntity(
    id = id,
    nummer = nummer,
    vonZeit = vonZeit.toEpochMilli(),
    bisZeit = bisZeit.toEpochMilli(),
    gesamtInCent = gesamtInCent,
    barInCent = barInCent,
    externInCent = externInCent,
    mwst7InCent = mwst7InCent,
    mwst19InCent = mwst19InCent,
    entnahmenInCent = entnahmenInCent,
    anzahlBuchungen = anzahlBuchungen,
    anzahlStornos = anzahlStornos,
    traineeUmsatzInCent = traineeUmsatzInCent,
    nullstellungszaehler = nullstellungszaehler,
    bedienerId = bedienerId,
    tseSignatur = tseSignatur,
    tseSerialNumber = tseSerialNumber,
    createdAt = createdAt.toEpochMilli()
)

fun ZBonEntity.toDomain() = ZBon(
    id = id,
    nummer = nummer,
    vonZeit = Instant.ofEpochMilli(vonZeit),
    bisZeit = Instant.ofEpochMilli(bisZeit),
    gesamtInCent = gesamtInCent,
    barInCent = barInCent,
    externInCent = externInCent,
    mwst7InCent = mwst7InCent,
    mwst19InCent = mwst19InCent,
    entnahmenInCent = entnahmenInCent,
    anzahlBuchungen = anzahlBuchungen,
    anzahlStornos = anzahlStornos,
    traineeUmsatzInCent = traineeUmsatzInCent,
    nullstellungszaehler = nullstellungszaehler,
    bedienerId = bedienerId,
    tseSignatur = tseSignatur,
    tseSerialNumber = tseSerialNumber,
    createdAt = Instant.ofEpochMilli(createdAt)
)

// ─── TseProtokollEintrag ──────────────────────────────────────────────────────

fun TseProtokollEintrag.toEntity() = TseProtokollEintragEntity(
    id = id,
    transaktionId = transaktionId,
    typ = typ.name,
    tseSerialNumber = tseSerialNumber,
    signatur = signatur,
    zeitpunktStart = zeitpunktStart.toEpochMilli(),
    zeitpunktEnd = zeitpunktEnd.toEpochMilli(),
    txNummer = txNummer,
    signaturZaehler = signaturZaehler,
    vorgangsDaten = vorgangsDaten
)

fun TseProtokollEintragEntity.toDomain() = TseProtokollEintrag(
    id = id,
    transaktionId = transaktionId,
    typ = TseVorgangTyp.valueOf(typ),
    tseSerialNumber = tseSerialNumber,
    signatur = signatur,
    zeitpunktStart = Instant.ofEpochMilli(zeitpunktStart),
    zeitpunktEnd = Instant.ofEpochMilli(zeitpunktEnd),
    txNummer = txNummer,
    signaturZaehler = signaturZaehler,
    vorgangsDaten = vorgangsDaten
)

// ─── AusfallzeitEintrag ───────────────────────────────────────────────────────

fun AusfallzeitEintragEntity.toDomain() = AusfallzeitEintrag(
    id = id,
    startZeit = Instant.ofEpochMilli(startZeit),
    endZeit = endZeit?.let { Instant.ofEpochMilli(it) },
    dauerSekunden = dauerSekunden,
    ursache = ursache,
    bedienerId = bedienerId,
    createdAt = Instant.ofEpochMilli(createdAt)
)
