package com.openclassrooms.vitesse.data.repository

import com.openclassrooms.vitesse.data.dao.CandidateDao
import com.openclassrooms.vitesse.domain.model.Candidate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class CandidateRepository(
    private val candidateDao: CandidateDao
) {
    // Get all Candidate
    fun getAllCandidate(): Flow<List<Candidate>> = flow {
        candidateDao.getAllCandidate()
            .map { dtoList -> dtoList.map { Candidate.fromDto(it) } }
            .collect { emit(it) }
    }.catch { e ->
        emit(emptyList())
    }

    // Get favorite Candidate
    fun getFavoriteCandidate(): Flow<List<Candidate>> = flow {
        candidateDao.getFavoriteCandidate()
            .map { dtoList -> dtoList.map { Candidate.fromDto(it) } }
            .collect { emit(it) }
    }.catch { e ->
        emit(emptyList())
    }

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