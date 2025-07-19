package com.openclassrooms.vitesse.data.repository

import android.util.Log
import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.vitesse.data.dao.CandidateDao
import com.openclassrooms.vitesse.data.dao.DetailDao
import com.openclassrooms.vitesse.data.entity.CandidateWithDetailDto
import com.openclassrooms.vitesse.domain.model.Candidate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class DetailRepository(
    private val detailDao: DetailDao,
    private val candidateDao: CandidateDao
) {
    // Get Candidate By Id
    fun getCandidateById(query: SupportSQLiteQuery): Flow<CandidateWithDetailDto> =
        candidateDao.getCandidateById(query)

    // Del a candidate
    fun deleteCandidate(candidateId: Long, detailId: Long): Flow<Result<Unit>> = flow {
        deleteDetailFlow(detailId)
        deleteCandidateFlow(candidateId)
        emit(Result.success(Unit))
    }.catch { e ->
        emit(Result.failure(CandidateRepositoryException("Failed to del exercise", e)))
    }

    private fun deleteDetailFlow(id: Long): Flow<Result<Unit>> = flow {
        try {
            detailDao.deleteDetail(id)
            emit(Result.success(Unit))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    private fun deleteCandidateFlow(id: Long): Flow<Result<Unit>> = flow {
        try {
            candidateDao.deleteCandidate(id)
            emit(Result.success(Unit))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    // Add or Modify a new candidate
    fun updateFavoriteCandidate(id: Long, fav: Boolean): Flow<Result<Unit>> = flow {
        candidateDao.updateCandidateFavorite(id, fav)
        emit(Result.success(Unit))
    }.catch { e ->
        Log.d("MARC", "updateFavoriteCandidate: $e")
        emit(Result.failure(CandidateRepositoryException("Failed to update exercise", e)))
    }
}