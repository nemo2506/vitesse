package com.openclassrooms.vitesse.domain.usecase

import android.util.Log
import com.openclassrooms.vitesse.data.entity.CandidateWithDetailDto
import kotlinx.coroutines.flow.Flow
import com.openclassrooms.vitesse.data.repository.DetailRepository
import com.openclassrooms.vitesse.domain.model.CandidateDescription
import kotlinx.coroutines.flow.flow
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.Locale
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

    fun updateFavoriteCandidate(id: Long, fav: Boolean): Flow<Result<Int>>  = flow {
        emit(Result.Loading)
        try {
            detailRepository.updateFavoriteCandidate(id, !fav) // INVERSE LA VALEUR FAV POUR LA MODIFIER
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
            email = detail.email ?: "",
            isFavorite = candidate.isFavorite,
            photoUri = candidate.photoUri,
            dateDescription = detail.date?.let { getDateDescription(it) } ?: "",
            salaryClaimDescription = detail.salaryClaim?.let { getSalaryClaimDescription(it) } ?: "",
            salaryClaimGpb = detail.salaryClaim?.let { getClaimGbpDescription(it) } ?: "",
            note = candidate.note
        )
    }

    private fun getSalaryClaimDescription(salaryClaim: Long): String {
        val symbols = DecimalFormatSymbols(Locale.FRANCE).apply {
            groupingSeparator = ' '
            decimalSeparator = ','
            currencySymbol = "€"
        }
        val decimalFormat = DecimalFormat("#,### €", symbols)
        decimalFormat.maximumFractionDigits = 0
        return decimalFormat.format(salaryClaim)
    }

    private fun getClaimGbpDescription(salaryClaim: Long): String{
        val converted = salaryClaim * 0.86705
        return "soit £ $converted"
    }

    private fun getDateDescription(dateTime: LocalDateTime): String{
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val age = calculateAge(dateTime)
        val date = dateTime.format(formatter)
        return "$date ($age ans)"
    }

    private fun calculateAge(birthDate: LocalDateTime): Int {
        val birthLocalDate: LocalDate = birthDate.toLocalDate()
        val today = LocalDate.now()
        return Period.between(birthLocalDate, today).years
    }

}
