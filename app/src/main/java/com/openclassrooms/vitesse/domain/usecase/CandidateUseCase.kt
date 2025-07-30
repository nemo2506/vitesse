package com.openclassrooms.vitesse.domain.usecase

import android.util.Log
import com.openclassrooms.vitesse.data.repository.CandidateRepository
import com.openclassrooms.vitesse.domain.model.Candidate
import com.openclassrooms.vitesse.utils.toZeroOrLong
import com.openclassrooms.vitesse.utils.capitalizeFirstLetter
import com.openclassrooms.vitesse.utils.isPositive
import com.openclassrooms.vitesse.utils.isValidEmail
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
        candidateId: Long = 0L,
        detailId: Long = 0L,
        firstName: String? = null,
        lastName: String? = null,
        phone: String? = null,
        email: String? = null,
        isFavorite: Boolean = false,
        photoUri: String? = null,
        note: String? = null,
        date: String? = null,
        salaryClaim: String? = null
    ): Flow<Result<Boolean>> = flow {

        emit(Result.Loading)
        try {
            candidateRepository.upsertCandidateAll(
                candidateId = candidateId,
                detailId = detailId,
                firstName = firstName?.capitalizeFirstLetter(),
                lastName = lastName?.uppercase(),
                phone = phone,
                email = email,
                isFavorite = isFavorite,
                photoUri = photoUri,
                note = note,
                date = date,
                salaryClaim = salaryClaim.toZeroOrLong()
            ).collect {
                emit(Result.Success(it.isPositive()))
            }
        } catch (e: Throwable) {
            emit(Result.Failure(e.message ?: "Unknown error"))
        }
    }

    fun validateInfo(etText: String?): Boolean {
        return etText.isNullOrBlank()
    }

    fun validateEmail(email: String): Boolean {
        return email.isValidEmail()
    }
}