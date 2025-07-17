package com.openclassrooms.vitesse.domain.usecase

import android.util.Log
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
    fun execute(fav: Int): Flow<List<Candidate>> {
        val (sql, argsList) = searchQueryAdd("emma", fav, "SELECT * FROM candidate")
        val query = SimpleSQLiteQuery(sql, argsList.toTypedArray())
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

    private fun searchQueryAdd(searchTerm: String, fav: Int, sql: String): Pair<String, MutableList<String>> {
        if (searchTerm.isBlank() && fav == 0) return Pair(sql, mutableListOf())

        val argsList = mutableListOf<String>()
        var newSql = sql
        val whereClauses = mutableListOf<String>()

        if (fav == 1) {
            whereClauses.add("isFavorite = ?")
            argsList.add("1")
        }
        if (searchTerm.isNotBlank()) {
            whereClauses.add("(firstName LIKE ? OR lastName LIKE ?)")
            argsList.add("%$searchTerm%")
            argsList.add("%$searchTerm%")
        }

        newSql += " WHERE " + whereClauses.joinToString(" AND ")
        Log.d("MARC", "searchQueryAdd/NEWCLAUSE: $newSql")

        return Pair(newSql, argsList)
    }
}