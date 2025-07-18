package com.openclassrooms.vitesse.data.repository

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
    fun getCandidate(query: SupportSQLiteQuery): Flow<List<CandidateWithDetailDto>> =
        candidateDao.getCandidate(query)

    // Add or Modify a new candidate
    fun updateCandidate(candidate: Candidate): Flow<Result<Unit>> = flow {
        candidateDao.updateCandidate(candidate.toDto())
        emit(Result.success(Unit))
    }.catch { e ->
        emit(Result.failure(ExerciseRepositoryException("Failed to add/modify candidate", e)))
    }

    // Del a candidate
    fun deleteCandidate(candidate: Candidate): Flow<Result<Unit>> = flow {
        val id = candidate.id ?: throw MissingExerciseIdException()
        candidateDao.deleteCandidate(id)
        emit(Result.success(Unit))
    }.catch { e ->
        emit(Result.failure(ExerciseRepositoryException("Failed to del exercise", e)))
    }
}