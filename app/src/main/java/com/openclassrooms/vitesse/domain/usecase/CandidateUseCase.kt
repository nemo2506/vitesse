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
            candidateRepository.getCandidateByAttr(fav, term).collect { dtoList ->
                if (dtoList.isEmpty()) {
                    emit(Result.Failure("Aucun candidat"))
                } else {
                    emit(Result.Success(dtoList.filterNotNull()))
                }
            }
        } catch (e: Exception) {
            emit(Result.Failure(e.message ?: "Unknown error"))
        }
    }

    fun upsertCandidate(
        firstName: String? = null,
        lastName: String? = null,
        phone: String? = null,
        email: String? = null,
        isFavorite: Boolean = false,
        photoUri: String? = null,
        note: String? = null,
        date: String? = null,
        salaryClaim: String? = null
    ): Flow<Result<Long>> = flow {
        val candidate = Candidate(
            firstName = firstName?.capitalizeFirstLetter(),
            lastName = lastName?.uppercase(),
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

    private fun String.capitalizeFirstLetter(): String {
        return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }
}