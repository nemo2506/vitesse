package com.openclassrooms.vitesse.domain.usecase

import android.util.Log
import com.openclassrooms.vitesse.data.repository.CandidateRepository
import com.openclassrooms.vitesse.domain.model.Candidate
import com.openclassrooms.vitesse.domain.model.Detail
import com.openclassrooms.vitesse.domain.usecase.utils.toLocalDateTime
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

    fun upsertCandidate(
        firstName: String,
        lastName: String,
        phone: String,
        email: String,
        isFavorite: Boolean = false,
        photoUri: String = "",
        note: String? = null,
        date: String? = null,
        salaryClaim: String? = null
    ): Flow<Result<Long>> = flow {
        val candidate = Candidate(
            firstName = firstName,
            lastName = lastName,
            isFavorite = isFavorite,
            photoUri = photoUri,
            note = note
        )
        val detail =
            Detail(
                date = date?.toLocalDateTime(),
                salaryClaim = salaryClaim?.toLongOrNull(),
                phone = phone,
                email = email,
                candidateId = 0
            )
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