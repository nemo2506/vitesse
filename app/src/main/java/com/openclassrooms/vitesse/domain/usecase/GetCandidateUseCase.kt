package com.openclassrooms.vitesse.domain.usecase

import androidx.sqlite.db.SimpleSQLiteQuery
import com.openclassrooms.vitesse.data.repository.CandidateRepository
import com.openclassrooms.vitesse.domain.model.Candidate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCandidateUseCase @Inject constructor(
    private val candidateRepository: CandidateRepository
) {
    fun execute(fav: Int, searchTerm: String): Flow<List<Candidate>> {
        val query = searchQueryAdd(searchTerm, fav)
        val candidateFlow = candidateRepository.getCandidate(query)

        return candidateFlow
            .map { list ->
                list.map { dto ->
                    Candidate.fromDto(dto.candidate.apply {
                        note = dto.details.firstOrNull()?.note
                    })
                }
            }
            .catch { emit(emptyList()) }
    }

    private fun searchQueryAdd(
        searchTerm: String,
        fav: Int,
        sql: String = """
        SELECT candidate.*,
        detail.note as note,
        detail.date as date,
        detail.salaryClaim as salaryClaim
        FROM candidate
        LEFT JOIN detail ON candidate.id = detail.candidateId
    """.trimIndent()
    ): SimpleSQLiteQuery {
        val argsList = mutableListOf<String>()
        var newSql = sql
        val whereClauses = mutableListOf<String>()

        if (searchTerm.isBlank() && fav == 0) {
            newSql = "$newSql ORDER BY lastName ASC"
            return SimpleSQLiteQuery(newSql, argsList.toTypedArray())
        }

        if (fav == 1) {
            whereClauses.add("isFavorite = ?")
            argsList.add("1")
        }

        if (searchTerm.isNotBlank()) {
            whereClauses.add("(firstName LIKE ? OR lastName LIKE ? OR detail.note LIKE ?)")
            argsList.add("%$searchTerm%")
            argsList.add("%$searchTerm%")
            argsList.add("%$searchTerm%")
        }

        if (whereClauses.isNotEmpty()) {
            newSql += " WHERE " + whereClauses.joinToString(" AND ")
        }
        val finalSql = "$newSql ORDER BY lastName ASC"

        return SimpleSQLiteQuery(finalSql, argsList.toTypedArray())
    }
}