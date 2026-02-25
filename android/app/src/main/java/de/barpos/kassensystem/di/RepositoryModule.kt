package de.barpos.kassensystem.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.barpos.kassensystem.data.repository.*
import de.barpos.kassensystem.domain.repository.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds @Singleton
    abstract fun bindTransaktionRepository(impl: TransaktionRepositoryImpl): ITransaktionRepository

    @Binds @Singleton
    abstract fun bindArtikelRepository(impl: ArtikelRepositoryImpl): IArtikelRepository

    @Binds @Singleton
    abstract fun bindTischRepository(impl: TischRepositoryImpl): ITischRepository

    @Binds @Singleton
    abstract fun bindPreisregelRepository(impl: PreisregelRepositoryImpl): IPreisregelRepository

    @Binds @Singleton
    abstract fun bindBonRepository(impl: BonRepositoryImpl): IBonRepository

    @Binds @Singleton
    abstract fun bindBedienerRepository(impl: BedienerRepositoryImpl): IBedienerRepository

    @Binds @Singleton
    abstract fun bindSchichtRepository(impl: SchichtRepositoryImpl): ISchichtRepository

    @Binds @Singleton
    abstract fun bindAusfallzeitRepository(impl: AusfallzeitRepositoryImpl): IAusfallzeitRepository

    @Binds @Singleton
    abstract fun bindTseProtokollRepository(impl: TseProtokollRepositoryImpl): ITseProtokollRepository

    @Binds @Singleton
    abstract fun bindDsFinVKArchivRepository(impl: DsFinVKArchivRepositoryImpl): IDsFinVKArchivRepository
}
