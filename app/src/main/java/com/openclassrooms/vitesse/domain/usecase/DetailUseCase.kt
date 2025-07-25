package com.openclassrooms.vitesse.domain.usecase

import android.util.Log
import com.openclassrooms.vitesse.data.entity.CandidateWithDetailDto
import kotlinx.coroutines.flow.Flow
import com.openclassrooms.vitesse.data.repository.DetailRepository
import com.openclassrooms.vitesse.domain.model.CandidateDescription
import com.openclassrooms.vitesse.domain.model.CandidateDetail
import com.openclassrooms.vitesse.domain.usecase.utils.toformatSalary
import com.openclassrooms.vitesse.domain.usecase.utils.toDateDescription
import com.openclassrooms.vitesse.domain.usecase.utils.toGbpDescription
import com.openclassrooms.vitesse.domain.usecase.utils.toLocalDateTime
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DetailUseCase @Inject constructor(
    private val detailRepository: DetailRepository
) {

    fun getCandidateToDescription(id: Long): Flow<Result<CandidateDescription>> = flow {
        emit(Result.Loading)
        try {
            detailRepository.getCandidateById(id).collect {
                emit(Result.Success(convertToDescription(it)))
            }
        } catch (e: Throwable) {
            Log.d("ERROR", "getCandidateByIdError: $e")
            emit(Result.Failure(e.message ?: "Unknown error"))
        }
    }

    fun getCandidateToDetail(id: Long): Flow<Result<CandidateDetail>> = flow {
        emit(Result.Loading)
        try {
            detailRepository.getCandidateById(id).collect {
                emit(Result.Success(convertToDeDetail(it)))
            }
        } catch (e: Throwable) {
            Log.d("ERROR", "getCandidateByIdError: $e")
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
            Log.d("ERROR", "deleteCandidateError: $e")
            emit(Result.Failure(e.message ?: "Unknown error"))
        }
    }

    fun updateFavoriteCandidate(id: Long, fav: Boolean): Flow<Result<Int>> = flow {
        emit(Result.Loading)
        try {
            detailRepository.updateFavoriteCandidate(
                id,
                !fav
            ) // INVERSE LA VALEUR FAV POUR LA MODIFIER
                .collect { emit(Result.Success(it)) }
        } catch (e: Throwable) {
            Log.d("ERROR", "updateFavoriteCandidateError: $e")
            emit(Result.Failure(e.message ?: "Unknown error"))
        }
    }

    private fun convertToDescription(dto: CandidateWithDetailDto): CandidateDescription {
        val candidate = dto.candidateDto
        val detail = dto.detailDto

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
            salaryClaimDescription = detail.salaryClaim?.toformatSalary() ?: "",
            salaryClaimGpb = detail.salaryClaim?.toGbpDescription() ?: "",
            note = candidate.note
        )
    }

    private fun convertToDeDetail(dto: CandidateWithDetailDto): CandidateDetail {
        val candidate = dto.candidateDto
        val detail = dto.detailDto
        Log.d("MARc", "convertToDeDetail: $detail")

        return CandidateDetail(
            candidateId = candidate.id,
            firstName = candidate.firstName,
            lastName = candidate.lastName,
            isFavorite = candidate.isFavorite,
            photoUri = candidate.photoUri,
            note = candidate.note,

            detailId = detail.id,
            date = detail.date,
            salaryClaim = detail.salaryClaim,
            phone = detail.phone,
            email = detail.email
        )
    }
}
