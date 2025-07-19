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
    fun deleteDetail(candidate: Candidate): Flow<Result<Unit>> = flow {
        val id = candidate.id ?: throw MissingCandidateIdException()
        candidateDao.deleteCandidate(id)
        emit(Result.success(Unit))
    }.catch { e ->
        emit(Result.failure(CandidateRepositoryException("Failed to del exercise", e)))
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