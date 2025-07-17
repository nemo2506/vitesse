package com.openclassrooms.vitesse.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.openclassrooms.vitesse.data.entity.CandidateDto
import com.openclassrooms.vitesse.data.entity.CandidateWithDetailDto
import kotlinx.coroutines.flow.Flow

@Dao
interface CandidateDao {
    @Transaction
    @Query("SELECT * FROM candidate WHERE isFavorite = :fav ORDER BY lastName ASC")
    fun getCandidate(fav: Int): Flow<List<CandidateWithDetailDto>>

    @Upsert
    suspend fun updateCandidate(candidate: CandidateDto): Long

    @Query("DELETE FROM candidate WHERE id = :id")
    suspend fun deleteCandidate(id: Long)
}