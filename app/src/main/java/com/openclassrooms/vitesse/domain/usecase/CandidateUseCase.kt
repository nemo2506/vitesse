package com.openclassrooms.vitesse.domain.usecase

import android.util.Log
import androidx.sqlite.db.SimpleSQLiteQuery
import kotlinx.coroutines.flow.Flow
import com.openclassrooms.vitesse.data.entity.toSummary
import com.openclassrooms.vitesse.data.repository.CandidateRepository
import com.openclassrooms.vitesse.domain.model.Candidate
import com.openclassrooms.vitesse.domain.model.CandidateSummary
import com.openclassrooms.vitesse.domain.model.Detail
import com.openclassrooms.vitesse.domain.usecase.Result
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class CandidateUseCase @Inject constructor(
    private val candidateRepository: CandidateRepository
) {

    fun getCandidate(fav: Int, searchTerm: String): Flow<Result<List<CandidateSummary>>> = flow {

        emit(Result.Loading)
        try {
            val query = searchCandidateAddQuery(searchTerm, fav)
            candidateRepository.getCandidate(query).collect { listDto ->
                val summaries = listDto.map { it.toSummary() }
                emit(Result.Success(summaries))
            }
        } catch (e: Throwable) {
            Log.d("ERROR", "executeError: $e")
            emit(Result.Failure(e.message ?: "Unknown error"))
        }
    }

    fun updateCandidate(candidate: Candidate, detail: Detail?): Flow<Result<Boolean>> = flow {
        emit(Result.Loading)
        try {
            candidateRepository.upsertCandidateTotal(candidate, detail).collect {
                emit(Result.Success(it > 0L))
            }
        } catch (e: Throwable) {
            Log.d("ERROR", "updateCandidate: $e")
            emit(Result.Failure(e.message ?: "Unknown error"))
        }
    }

    private fun searchCandidateAddQuery(
        searchTerm: String,
        fav: Int,
        sql: String = """
        SELECT candidate.*
        FROM candidate
        LEFT JOIN detail ON candidate.id = detail.candidateId
    """.trimIndent()
    ): SimpleSQLiteQuery {
        val argsList = mutableListOf<Any>()
        var newSql = sql
        val whereClauses = mutableListOf<String>()

        if (searchTerm.isBlank() && fav == 0) {
            newSql = "$newSql ORDER BY lastName ASC"
            return SimpleSQLiteQuery(newSql, argsList.toTypedArray())
        }

        if (fav == 1) {
            whereClauses.add("isFavorite = ?")
            argsList.add(1)
        }

        if (searchTerm.isNotBlank()) {
            whereClauses.add("(firstName LIKE ? COLLATE NOCASE OR lastName LIKE ? COLLATE NOCASE OR detail.note LIKE ? COLLATE NOCASE)")
            repeat(3) { argsList.add("%$searchTerm%") }
        }

        if (whereClauses.isNotEmpty()) {
            newSql += " WHERE " + whereClauses.joinToString(" AND ")
        }
        val finalSql = "$newSql ORDER BY lastName ASC"

        return SimpleSQLiteQuery(finalSql, argsList.toTypedArray())
    }

}