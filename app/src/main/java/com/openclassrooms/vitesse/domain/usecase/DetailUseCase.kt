package com.openclassrooms.vitesse.domain.usecase

import com.openclassrooms.vitesse.data.entity.CandidateWithDetailDto
import com.openclassrooms.vitesse.data.repository.CurrencyRepository
import com.openclassrooms.vitesse.data.repository.DetailRepository
import com.openclassrooms.vitesse.domain.model.CandidateDescription
import com.openclassrooms.vitesse.domain.model.CandidateDetail
import com.openclassrooms.vitesse.utils.toDateDescription
import com.openclassrooms.vitesse.utils.toEmpty
import com.openclassrooms.vitesse.utils.toFrDescription
import com.openclassrooms.vitesse.utils.toGbpDescription
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
//import android.util.Log
import com.openclassrooms.vitesse.utils.Log

class DetailUseCase @Inject constructor(
    private val detailRepository: DetailRepository,
    private val currencyRepository: CurrencyRepository,
) {

    fun getCandidateToDescription(id: Long): Flow<Result<CandidateDescription>> = flow {
        emit(Result.Loading)
        try {
            detailRepository.getCandidateById(id).collect {
                if (it != null) {
                    emit(Result.Success(convertToDescription(it)))
                } else {
                    emit(Result.Failure("Candidate deleted"))
                }
            }
        } catch (e: Throwable) {
            Log.d("MARC", "getCandidateByIdError: $e")
            emit(Result.Failure(e.message ?: "Unknown error"))
        }
    }

    fun getCandidateToDetail(id: Long): Flow<Result<CandidateDetail>> = flow {
        emit(Result.Loading)
        try {
            detailRepository.getCandidateById(id).collect { dto ->
                if (dto != null) {
                    emit(Result.Success(convertToDetail(dto)))
                } else {
                    emit(Result.Failure("Candidate not found"))
                }
            }
        } catch (e: Throwable) {
            Log.d("MARC", "getCandidateByIdError: $e")
            emit(Result.Failure(e.message ?: "Unknown error"))
        }
    }

    fun deleteCandidate(candidateId: Long): Flow<Result<Int>> = flow {
        emit(Result.Loading)
        try {
            detailRepository.deleteCandidate(candidateId).collect {
                emit(Result.Success(it))
            }
        } catch (e: Throwable) {
            Log.d("MARC", "deleteCandidateError: $e")
            emit(Result.Failure(e.message ?: "Unknown error"))
        }
    }

    fun updateFavoriteCandidate(id: Long, fav: Boolean): Flow<Result<Int>> = flow {
        emit(Result.Loading)
        try {
            detailRepository.updateFavoriteCandidate(
                id,
                !fav // REVERSE THE FAV VALUE TO CHANGE IT
            ).collect { emit(Result.Success(it)) }
        } catch (e: Throwable) {
            Log.d("MARC", "updateFavoriteCandidateError: $e")
            emit(Result.Failure(e.message ?: "Unknown error"))
        }
    }

    private suspend fun convertToDescription(dto: CandidateWithDetailDto): CandidateDescription {
        val candidate = dto.candidateDto
        val detail = dto.detailDto
        val gbpCurrency = getCurrency()

        return CandidateDescription(
            candidateId = candidate.id,
            detailId = detail.id,
            firstName = candidate.firstName,
            lastName = candidate.lastName,
            phone = detail.phone,
            email = detail.email,
            isFavorite = candidate.isFavorite,
            photoUri = candidate.photoUri,
            dateDescription = detail.date?.toDateDescription() ?: "",
            salaryClaimDescription = detail.salaryClaim?.toFrDescription() ?: "",
            salaryClaimGpb = detail.salaryClaim?.toGbpDescription(gbpCurrency) ?: "",
            note = candidate.note
        )
    }

    private suspend fun getCurrency(): Double {
        val result: Result<Double> = currencyRepository.getGbp()
        return (result as? Result.Success)?.value ?: 0.0
    }

    private fun convertToDetail(dto: CandidateWithDetailDto): CandidateDetail {
        val candidate = dto.candidateDto
        val detail = dto.detailDto

        return CandidateDetail(
            candidateId = candidate.id,
            firstName = candidate.firstName,
            lastName = candidate.lastName,
            isFavorite = candidate.isFavorite,
            photoUri = candidate.photoUri,
            note = candidate.note,

            detailId = detail.id,
            date = detail.date,
            salaryClaim = detail.salaryClaim.toEmpty(),
            phone = detail.phone,
            email = detail.email
        )
    }
}
