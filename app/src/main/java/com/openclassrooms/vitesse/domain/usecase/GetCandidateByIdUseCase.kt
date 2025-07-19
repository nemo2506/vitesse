package com.openclassrooms.vitesse.domain.usecase

import android.util.Log
import androidx.sqlite.db.SimpleSQLiteQuery
import com.openclassrooms.vitesse.data.entity.CandidateTotal
import com.openclassrooms.vitesse.data.entity.toDetail
import com.openclassrooms.vitesse.data.repository.DetailRepository
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

class GetCandidateByIdUseCase @Inject constructor(
    private val detailRepository: DetailRepository
) {
    fun execute(id: Long): Flow<Result<CandidateTotal>> {
        val query = searchQuery(id)
        return detailRepository.getCandidateById(query)
            .map { dto ->
                Result.success(dto.toDetail())
            }
            .catch { e ->
                Log.d("ERROR", "executeError: $e")
                emit(Result.failure(e))
            }
    }

    private fun searchQuery(
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

    fun getSalary(salaryClaim: Long): CharSequence {
        val symbols = DecimalFormatSymbols(Locale.FRANCE).apply {
            groupingSeparator = ' '
            decimalSeparator = ','
            currencySymbol = "€"
        }
        val decimalFormat = DecimalFormat("#,### €", symbols)
        decimalFormat.maximumFractionDigits = 0
        return decimalFormat.format(salaryClaim)
    }

    fun getSalaryGbp(salaryClaim: Long): CharSequence{
        val converted = salaryClaim * 0.86705
        return "soit £ $converted"
    }

    fun getBirth(dateTime: LocalDateTime): CharSequence {
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

    fun getTitle(firstName: String, lastName: String): CharSequence {
        return "$firstName $lastName"
    }
}