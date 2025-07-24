package com.openclassrooms.vitesse.domain.usecase

import android.util.Log
import com.openclassrooms.vitesse.data.repository.CandidateRepository
import com.openclassrooms.vitesse.domain.model.Candidate
import com.openclassrooms.vitesse.domain.model.Detail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CandidateUseCase @Inject constructor(
    private val candidateRepository: CandidateRepository
) {

    fun getCandidate(fav: Int, term: String): Flow<Result<List<Candidate>>> = flow {
        emit(Result.Loading)
        try {
            candidateRepository.getCandidateByAttr(fav, term).collect {
                emit(Result.Success(it))
            }

        } catch (e: Exception) {
            emit(Result.Failure(e.message ?: "Unknown error"))
        }
    }

    fun updateCandidate(candidate: Candidate, detail: Detail): Flow<Result<Long>> = flow {
        emit(Result.Loading)
        try {
            candidateRepository.upsertCandidate(candidate, detail).collect {
                emit(Result.Success(it))
            }
        } catch (e: Throwable) {
            Log.d("ERROR", "updateCandidate: $e")
            emit(Result.Failure(e.message ?: "Unknown error"))
        }
    }
}