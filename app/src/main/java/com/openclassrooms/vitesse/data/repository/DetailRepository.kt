package com.openclassrooms.vitesse.data.repository

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

    // Add or Modify a new candidate
    fun updateCandidate(candidate: Candidate): Flow<Result<Unit>> = flow {
        candidateDao.updateCandidate(candidate.toDto())
        emit(Result.success(Unit))
    }.catch { e ->
        emit(Result.failure(CandidateRepositoryException("Failed to add/modify candidate", e)))
    }

    // Del a candidate
    fun deleteCandidate(candidate: Candidate): Flow<Result<Unit>> = flow {
        val id = candidate.id ?: throw MissingCandidateIdException()
        candidateDao.deleteCandidate(id)
        emit(Result.success(Unit))
    }.catch { e ->
        emit(Result.failure(CandidateRepositoryException("Failed to del exercise", e)))
    }
}