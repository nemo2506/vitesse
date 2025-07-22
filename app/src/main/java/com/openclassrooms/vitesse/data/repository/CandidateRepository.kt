package com.openclassrooms.vitesse.data.repository

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.vitesse.data.dao.CandidateDao
import com.openclassrooms.vitesse.data.dao.CandidateWithDetailsDao
import com.openclassrooms.vitesse.data.entity.CandidateWithDetailDto
import com.openclassrooms.vitesse.domain.model.Candidate
import com.openclassrooms.vitesse.domain.model.Detail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CandidateRepository(
    private val candidateDao: CandidateDao,
    private val candidateWithDetailsDao: CandidateWithDetailsDao,
) {
    fun getCandidate(query: SupportSQLiteQuery): Flow<List<CandidateWithDetailDto>> {
        return candidateDao.getCandidate(query)
    }

    // Add or Modify a new candidate
    fun upsertCandidateTotal(candidate: Candidate, detail: Detail?): Flow<Long> = flow {
        try {
            val candidateWithDetail = CandidateWithDetailDto(
                candidate.toDto(),
                detail?.toDto()
            )
            val validate = candidateWithDetailsDao.upsertCandidateWithDetails(candidateWithDetail)
            emit(validate)
        } catch (e: SQLiteConstraintException) {
            Log.d("ERROR", "Constraint violation: $e")
            emit(0L)
        } catch (e: Exception) {
            Log.d("ERROR", "upsertCandidateError: $e")
            emit(0L)
        }
    }
}