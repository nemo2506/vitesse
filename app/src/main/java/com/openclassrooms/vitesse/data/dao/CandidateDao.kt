package com.openclassrooms.vitesse.data.dao

import android.util.Log
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.openclassrooms.vitesse.data.entity.CandidateDto
import com.openclassrooms.vitesse.data.entity.CandidateWithDetailDto
import com.openclassrooms.vitesse.data.entity.DetailDto
import com.openclassrooms.vitesse.domain.model.Candidate
import kotlinx.coroutines.flow.Flow

@Dao
interface CandidateDao {

    @Transaction
    suspend fun upsertCandidateWithDetail(candidateWithDetailDto: CandidateWithDetailDto): Long {
        val candidateId = upsertCandidate(candidateWithDetailDto.candidateDto)
        candidateWithDetailDto.detailDto.let {
            val detailDto = it.copy(candidateId = candidateId)
            upsertDetail(detailDto)
        }
        return candidateId
    }

    @Upsert
    suspend fun upsertCandidate(candidate: CandidateDto): Long

    @Upsert
    suspend fun upsertDetail(detail: DetailDto): Long

    @Query("""
    SELECT * FROM candidate 
    WHERE isFavorite = :fav 
    AND (:term = '' OR
    firstName LIKE :term COLLATE NOCASE OR
    lastName LIKE :term COLLATE NOCASE
    )""")
    fun getCandidate(fav: Int, term: String): Flow<List<Candidate>>

    @Transaction
    @Query("Select * FROM candidate WHERE id = :id")
    fun getCandidateById(id: Long): Flow<CandidateWithDetailDto>

    @Query("DELETE FROM candidate WHERE id = :id")
    suspend fun deleteCandidate(id: Long): Int

    @Query("UPDATE candidate SET isFavorite = :fav WHERE id = :id")
    suspend fun updateCandidateFavorite(id: Long, fav: Boolean): Int
}