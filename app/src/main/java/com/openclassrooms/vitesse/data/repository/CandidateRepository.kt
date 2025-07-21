package com.openclassrooms.vitesse.data.repository

import android.util.Log
import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.vitesse.domain.usecase.Result
import com.openclassrooms.vitesse.data.dao.CandidateDao
import com.openclassrooms.vitesse.data.dao.CandidateWithDetailsDao
import com.openclassrooms.vitesse.data.dao.DetailDao
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
//    private fun upsertCandidateTotal(candidate: Candidate, detail: Detail): Flow<Long> = flow {
//        try {
//            val candidateWithDetail = CandidateWithDetailDto(candidate.toDto(), detail.toDto())
//            val candidateId = candidateWithDetailsDao.upsertCandidate(candidateWithDetail)
//            if (candidateId.toInt() != 0) {
//                try {
//                    upsertDetail(detail).collect { long ->
//                        emit(long)
//                    }
//                } catch (e: Exception) {
//                    Log.d("ERROR", "Constraint violation: $e")
//                    emit(0)
//                }
//            }
//        } catch (e: android.database.sqlite.SQLiteConstraintException) {
//            Log.d("ERROR", "Constraint violation: $e")
//            emit(0)
//        } catch (e: Exception) {
//            Log.d("ERROR", "upsertCandidateError: $e")
//            emit(0)
//        }
//    }
}