package com.openclassrooms.vitesse.di

import android.content.Context
import com.openclassrooms.vitesse.data.dao.CandidateDao
import com.openclassrooms.vitesse.data.dao.DetailDao
import com.openclassrooms.vitesse.data.database.AppDatabase
import com.openclassrooms.vitesse.data.repository.CandidateRepository
import com.openclassrooms.vitesse.data.repository.DetailRepository
import com.openclassrooms.vitesse.domain.usecase.CandidateUseCase
import com.openclassrooms.vitesse.domain.usecase.DetailUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideCoroutineScope(): CoroutineScope = CoroutineScope(SupervisorJob())

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        coroutineScope: CoroutineScope
    ): AppDatabase {
        return try {
            AppDatabase.getDatabase(context, coroutineScope)
        } catch (e: Exception) {
            throw RuntimeException("Failed to provide AppDatabase", e)
        }
    }

    @Provides
    fun provideCandidateDao(appDatabase: AppDatabase): CandidateDao {
        return try {
            appDatabase.candidateDao()
        } catch (e: Exception) {
            throw RuntimeException("Failed to provide CandidateDao", e)
        }
    }

    @Provides
    fun provideDetailDao(appDatabase: AppDatabase): DetailDao {
        return try {
            appDatabase.detailDao()
        } catch (e: Exception) {
            throw RuntimeException("Failed to provide DetailDao", e)
        }
    }

    @Provides
    @Singleton
    fun provideCandidateRepository(
        candidateDao: CandidateDao
    ): CandidateRepository {
        return try {
            CandidateRepository(candidateDao)
        } catch (e: Exception) {
            throw RuntimeException("Failed to provide CandidateRepository", e)
        }
    }

    @Provides
    @Singleton
    fun provideDetailRepository(
        detailDao: DetailDao,
        candidateDao: CandidateDao
    ): DetailRepository {
        return try {
            DetailRepository(detailDao, candidateDao)
        } catch (e: Exception) {
            throw RuntimeException("Failed to provide DetailRepository", e)
        }
    }

    @Provides
    @Singleton
    fun provideDetailUseCase(
        detailRepository: DetailRepository
    ): DetailUseCase {
        return try {
            DetailUseCase(detailRepository)
        } catch (e: Exception) {
            throw RuntimeException("Failed to provide DetailUseCase", e)
        }
    }

    @Provides
    @Singleton
    fun provideCandidateUseCase(
        candidateRepository: CandidateRepository
    ): CandidateUseCase {
        return try {
            CandidateUseCase(candidateRepository)
        } catch (e: Exception) {
            throw RuntimeException("Failed to provide CandidateUseCase", e)
        }
    }
}