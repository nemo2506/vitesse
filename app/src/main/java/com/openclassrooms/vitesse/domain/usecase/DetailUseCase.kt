package com.openclassrooms.vitesse.domain.usecase

import android.util.Log
import androidx.sqlite.db.SimpleSQLiteQuery
import com.openclassrooms.vitesse.data.entity.toDetail
import com.openclassrooms.vitesse.data.repository.CandidateRepository
import com.openclassrooms.vitesse.data.repository.DetailRepository
import com.openclassrooms.vitesse.domain.model.Candidate
import com.openclassrooms.vitesse.domain.model.CandidateDetail
import com.openclassrooms.vitesse.domain.model.CandidateTotal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

class DetailUseCase @Inject constructor(
    private val detailRepository: DetailRepository,
    private val candidateRepository: CandidateRepository
) {

    fun getCandidateById(id: Long): Flow<Result<CandidateDetail>> {
        val query = searchCandidateQuery(id)
        return detailRepository.getCandidateById(query)
            .map { dto ->
                Result.success(convertToDetailScreen(dto.toDetail()))
            }
            .catch { e ->
                Log.d("ERROR", "executeError: $e")
                emit(Result.failure(e))
            }
    }

    fun deleteCandidate(candidateId: Long, detailId: Long): Flow<Result<Unit>> {
        return detailRepository.deleteCandidate(candidateId, detailId)
    }

    private fun convertToDetailScreen(candidateTotal: CandidateTotal): CandidateDetail{
        return CandidateDetail(
            candidateId= candidateTotal.id,
            detailId= candidateTotal.detailId,
            firstName= candidateTotal.firstName,
            lastName= candidateTotal.lastName,
            phone= candidateTotal.phone,
            email= candidateTotal.email,
            isFavorite= candidateTotal.isFavorite,
            photoUri= candidateTotal.photoUri,
            dateDescription= getDateDescription(candidateTotal.date),
            salaryClaimDescription= getSalaryClaimDescription(candidateTotal.salaryClaim),
            salaryClaimGpb= getClaimGbpDescription(candidateTotal.salaryClaim),
            note= candidateTotal.note,
        )
    }

    private fun searchCandidateQuery(
        id: Long,
        sql: String = """
        SELECT *
        FROM candidate
        LEFT JOIN detail ON candidate.id = detail.candidateId
    """.trimIndent()
    ): SimpleSQLiteQuery {
        val newSql = "$sql WHERE candidate.id = ?"
        val argsList = listOf(id)
        return SimpleSQLiteQuery(newSql, argsList.toTypedArray())
    }

    fun updateFavoriteCandidate(id: Long, fav: Boolean): Flow<Result<Unit>> {
        return detailRepository.updateFavoriteCandidate(id, !fav) // INVERSE LA VALEUR FAV POUR LA MODIFIER
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
