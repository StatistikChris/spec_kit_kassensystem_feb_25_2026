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
        ArtikelEntity::class,
        AusfallzeitEintragEntity::class,
        BedienerEntity::class,
        BonEntity::class,
        DsFinVKArchivSatzEntity::class,
        PreisregelEntity::class,
        SchichtEntity::class,
        SkuEntity::class,
        TischEntity::class,
        TransaktionEntity::class,
        TransaktionsPositionEntity::class,
        TseProtokollEintragEntity::class,
        ZBonEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(TypeConverter::class)
abstract class KassenDatabase : RoomDatabase() {

    abstract fun artikelDao(): ArtikelDao
    abstract fun ausfallzeitEintragDao(): AusfallzeitEintragDao
    abstract fun bedienerDao(): BedienerDao
    abstract fun bonDao(): BonDao
    abstract fun dsFinVKArchivSatzDao(): DsFinVKArchivSatzDao
    abstract fun preisregelDao(): PreisregelDao
    abstract fun schichtDao(): SchichtDao
    abstract fun skuDao(): SkuDao
    abstract fun tischDao(): TischDao
    abstract fun transaktionDao(): TransaktionDao
    abstract fun transaktionsPositionDao(): TransaktionsPositionDao
    abstract fun tseProtokollEintragDao(): TseProtokollEintragDao
    abstract fun zBonDao(): ZBonDao

    companion object {
        const val DATABASE_NAME = "kassen_db"
    }
}
