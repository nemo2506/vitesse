package com.openclassrooms.vitesse.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.room.Upsert
import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.vitesse.data.entity.CandidateDto
import com.openclassrooms.vitesse.data.entity.CandidateWithDetailDto
import com.openclassrooms.vitesse.data.entity.DetailDto
import kotlinx.coroutines.flow.Flow

@Dao
interface CandidateDao {
    @Transaction
    @RawQuery(observedEntities = [CandidateWithDetailDto::class, DetailDto::class])
    fun getCandidate(query: SupportSQLiteQuery): Flow<List<CandidateWithDetailDto>>

    @Transaction
    @RawQuery(observedEntities = [CandidateWithDetailDto::class, DetailDto::class])
    fun getCandidateById(query: SupportSQLiteQuery): Flow<CandidateWithDetailDto>

    @Upsert
    suspend fun upsertCandidate(candidate: CandidateDto): Long

    @Insert
    suspend fun insertCandidate(candidate: CandidateDto): Long

    @Query("DELETE FROM candidate WHERE id = :id")
    suspend fun deleteCandidate(id: Long): Int

    @Query("UPDATE candidate SET isFavorite = :fav WHERE id = :id")
    suspend fun updateCandidateFavorite(id: Long, fav: Boolean): Int
}