package com.openclassrooms.vitesse.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.openclassrooms.vitesse.data.entity.CandidateDto
import kotlinx.coroutines.flow.Flow

@Dao
interface CandidateDao {

    @Query("SELECT * FROM candidate ORDER BY lastname ASC")
    fun getAllCandidate(): Flow<List<CandidateDto>>

    @Upsert
    suspend fun updateCandidate(candidate: CandidateDto): Long

    @Query("DELETE FROM candidate WHERE id = :id")
    suspend fun deleteCandidate(id: Long)
}