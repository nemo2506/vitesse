package com.openclassrooms.vitesse.domain.usecase

import android.util.Log
import androidx.sqlite.db.SimpleSQLiteQuery
import com.openclassrooms.vitesse.data.entity.CandidateSummary
import com.openclassrooms.vitesse.data.entity.toSummary
import com.openclassrooms.vitesse.data.repository.CandidateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCandidateUseCase @Inject constructor(
    private val candidateRepository: CandidateRepository
) {
    fun execute(fav: Int, searchTerm: String): Flow<List<CandidateSummary>> {
        val query = searchQueryAdd(searchTerm, fav)
        return candidateRepository.getCandidate(query)
            .map { listDto ->
                listDto.map { dto ->
                    dto.toSummary()
                }
            }
            .catch { e ->
                Log.d("ERROR", "executeError: $e")
                emit(emptyList())
            }
    }

    private fun searchQueryAdd(
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