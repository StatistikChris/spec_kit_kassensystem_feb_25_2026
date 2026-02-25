package de.barpos.kassensystem.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import de.barpos.kassensystem.data.db.converter.TypeConverter
import de.barpos.kassensystem.data.db.dao.ArtikelDao
import de.barpos.kassensystem.data.db.dao.AusfallzeitEintragDao
import de.barpos.kassensystem.data.db.dao.BedienerDao
import de.barpos.kassensystem.data.db.dao.BonDao
import de.barpos.kassensystem.data.db.dao.DsFinVKArchivSatzDao
import de.barpos.kassensystem.data.db.dao.PreisregelDao
import de.barpos.kassensystem.data.db.dao.SchichtDao
import de.barpos.kassensystem.data.db.dao.SkuDao
import de.barpos.kassensystem.data.db.dao.TischDao
import de.barpos.kassensystem.data.db.dao.TransaktionDao
import de.barpos.kassensystem.data.db.dao.TransaktionsPositionDao
import de.barpos.kassensystem.data.db.dao.TseProtokollEintragDao
import de.barpos.kassensystem.data.db.dao.VerfahrensdokumentationDao
import de.barpos.kassensystem.data.db.dao.ZBonDao
import de.barpos.kassensystem.data.db.entity.ArtikelEntity
import de.barpos.kassensystem.data.db.entity.AusfallzeitEintragEntity
import de.barpos.kassensystem.data.db.entity.BedienerEntity
import de.barpos.kassensystem.data.db.entity.BonEntity
import de.barpos.kassensystem.data.db.entity.DsFinVKArchivSatzEntity
import de.barpos.kassensystem.data.db.entity.PreisregelEntity
import de.barpos.kassensystem.data.db.entity.SchichtEntity
import de.barpos.kassensystem.data.db.entity.SkuEntity
import de.barpos.kassensystem.data.db.entity.TischEntity
import de.barpos.kassensystem.data.db.entity.TransaktionEntity
import de.barpos.kassensystem.data.db.entity.TransaktionsPositionEntity
import de.barpos.kassensystem.data.db.entity.TseProtokollEintragEntity
import de.barpos.kassensystem.data.db.entity.VerfahrensdokumentationEntity
import de.barpos.kassensystem.data.db.entity.ZBonEntity

@Database(
    entities = [
        TransaktionEntity::class,
        TransaktionsPositionEntity::class,
        ArtikelEntity::class,
        SkuEntity::class,
        PreisregelEntity::class,
        TischEntity::class,
        BedienerEntity::class,
        SchichtEntity::class,
        BonEntity::class,
        ZBonEntity::class,
        TseProtokollEintragEntity::class,
        VerfahrensdokumentationEntity::class,
        AusfallzeitEintragEntity::class,
        DsFinVKArchivSatzEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(TypeConverter::class)
abstract class KassenDatabase : RoomDatabase() {

    abstract fun transaktionDao(): TransaktionDao
    abstract fun transaktionsPositionDao(): TransaktionsPositionDao
    abstract fun artikelDao(): ArtikelDao
    abstract fun skuDao(): SkuDao
    abstract fun preisregelDao(): PreisregelDao
    abstract fun tischDao(): TischDao
    abstract fun bedienerDao(): BedienerDao
    abstract fun schichtDao(): SchichtDao
    abstract fun bonDao(): BonDao
    abstract fun zBonDao(): ZBonDao
    abstract fun tseProtokollEintragDao(): TseProtokollEintragDao
    abstract fun verfahrensdokumentationDao(): VerfahrensdokumentationDao
    abstract fun ausfallzeitEintragDao(): AusfallzeitEintragDao
    abstract fun dsFinVKArchivSatzDao(): DsFinVKArchivSatzDao

    companion object {
        const val DATABASE_NAME = "kassen_db"
    }
}
