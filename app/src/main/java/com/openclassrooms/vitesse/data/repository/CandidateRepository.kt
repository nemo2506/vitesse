package com.openclassrooms.vitesse.data.repository

import android.util.Log
import com.openclassrooms.vitesse.data.dao.CandidateDao
import com.openclassrooms.vitesse.domain.model.Candidate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class CandidateRepository(
    private val candidateDao: CandidateDao
) {
    fun getCandidate(fav: Int): Flow<List<Candidate>> =
        candidateDao.getCandidate(fav)
            .map { list ->
                list.map { dto ->
                    Candidate.fromDto(
                        dto.candidate.apply {
                            Log.d("MARC", "getCandidate/dto $dto")
                            note = dto.details.firstOrNull()?.note
                        }
                    )
                }
            }
            .catch { emit(emptyList()) }

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