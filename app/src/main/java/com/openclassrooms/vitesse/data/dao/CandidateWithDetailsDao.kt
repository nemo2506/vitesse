package com.openclassrooms.vitesse.data.dao

import android.util.Log
import androidx.room.Dao
import androidx.room.Transaction
import androidx.room.Upsert
import com.openclassrooms.vitesse.data.entity.CandidateDto
import com.openclassrooms.vitesse.data.entity.CandidateWithDetailDto
import com.openclassrooms.vitesse.data.entity.DetailDto

@Dao
interface CandidateWithDetailsDao {

    @Transaction
    suspend fun upsertCandidateWithDetails(candidateWithDetail: CandidateWithDetailDto): Long {
        val candidateId = upsertCandidate(candidateWithDetail.candidate)
        val detail = candidateWithDetail.detail
        detail?.candidateId = candidateId
        if (detail != null) {
            upsertDetail(detail)
        }
        return candidateId
    }

    @Upsert
    suspend fun upsertCandidate(candidate: CandidateDto): Long

    @Upsert
    suspend fun upsertDetail(detail: DetailDto): Long
}