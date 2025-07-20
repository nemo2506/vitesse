package com.openclassrooms.vitesse.data.repository

import android.util.Log
import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.vitesse.data.dao.CandidateDao
import com.openclassrooms.vitesse.data.entity.CandidateWithDetailDto
import com.openclassrooms.vitesse.domain.model.Candidate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class CandidateRepository(
    private val candidateDao: CandidateDao
) {
    fun getCandidate(query: SupportSQLiteQuery): Flow<List<CandidateWithDetailDto>> {
        return candidateDao.getCandidate(query)
    }

    // Add or Modify a new candidate
    fun upsertCandidate(candidate: Candidate): Flow<Long> = flow {
        try {
            val result = candidateDao.upsertCandidate(candidate.toDto())
            emit(result)
        } catch (e: Exception) {
            Log.d("ERROR", "upsertCandidateError: $e")
            emit(0)
        }
    }
}