package de.barpos.kassensystem.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import de.barpos.kassensystem.data.db.KassenDatabase
import de.barpos.kassensystem.data.db.dao.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideKassenDatabase(@ApplicationContext context: Context): KassenDatabase =
        Room.databaseBuilder(context, KassenDatabase::class.java, KassenDatabase.DATABASE_NAME)
            .setJournalMode(androidx.room.RoomDatabase.JournalMode.WRITE_AHEAD_LOGGING)
            .fallbackToDestructiveMigrationOnDowngrade()
            .build()

    @Provides fun provideTransaktionDao(db: KassenDatabase): TransaktionDao = db.transaktionDao()
    @Provides fun provideTransaktionsPositionDao(db: KassenDatabase): TransaktionsPositionDao = db.transaktionsPositionDao()
    @Provides fun provideArtikelDao(db: KassenDatabase): ArtikelDao = db.artikelDao()
    @Provides fun provideSkuDao(db: KassenDatabase): SkuDao = db.skuDao()
    @Provides fun providePreisregelDao(db: KassenDatabase): PreisregelDao = db.preisregelDao()
    @Provides fun provideTischDao(db: KassenDatabase): TischDao = db.tischDao()
    @Provides fun provideBedienerDao(db: KassenDatabase): BedienerDao = db.bedienerDao()
    @Provides fun provideSchichtDao(db: KassenDatabase): SchichtDao = db.schichtDao()
    @Provides fun provideBonDao(db: KassenDatabase): BonDao = db.bonDao()
    @Provides fun provideZBonDao(db: KassenDatabase): ZBonDao = db.zBonDao()
    @Provides fun provideTseProtokollEintragDao(db: KassenDatabase): TseProtokollEintragDao = db.tseProtokollEintragDao()
    @Provides fun provideVerfahrensdokumentationDao(db: KassenDatabase): VerfahrensdokumentationDao = db.verfahrensdokumentationDao()
    @Provides fun provideAusfallzeitEintragDao(db: KassenDatabase): AusfallzeitEintragDao = db.ausfallzeitEintragDao()
    @Provides fun provideDsFinVKArchivSatzDao(db: KassenDatabase): DsFinVKArchivSatzDao = db.dsFinVKArchivSatzDao()
}
