package com.openclassrooms.vitesse.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.room.Upsert
import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.vitesse.data.entity.CandidateDto
import com.openclassrooms.vitesse.data.entity.CandidateWithDetailDto
import kotlinx.coroutines.flow.Flow

@Dao
interface CandidateDao {
    @Transaction
    @RawQuery(observedEntities = [CandidateWithDetailDto::class])
    fun getCandidate(query: SupportSQLiteQuery): Flow<List<CandidateWithDetailDto>>

    @Upsert
    suspend fun updateCandidate(candidate: CandidateDto): Long

    @Query("DELETE FROM candidate WHERE id = :id")
    suspend fun deleteCandidate(id: Long)
}