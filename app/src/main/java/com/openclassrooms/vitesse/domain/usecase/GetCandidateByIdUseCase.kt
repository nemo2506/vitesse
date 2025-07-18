package com.openclassrooms.vitesse.domain.usecase

import androidx.sqlite.db.SimpleSQLiteQuery
import com.openclassrooms.vitesse.data.repository.DetailRepository
import com.openclassrooms.vitesse.domain.model.Candidate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCandidateByIdUseCase @Inject constructor(
    private val detailRepository: DetailRepository
) {
    fun execute(id: Long): Flow<Candidate?> {
        val query = searchQuery(id)
        val candidateFlow = detailRepository.getCandidateById(query)
        return candidateFlow
            .map { dto ->
                Candidate.fromDto(dto.candidate.apply {
                    note = dto.details.firstOrNull()?.note
                })
            }
//            .catch { e ->
//                emit() // Envoie null en cas d'erreur
//            }
    }

    private fun searchQuery(
        id: Long,
        sql: String = """
        SELECT candidate.*,
        detail.note as note,
        detail.date as date,
        detail.salaryClaim as salaryClaim
        FROM candidate
        LEFT JOIN detail ON candidate.id = detail.candidateId
    """.trimIndent()
    ): SimpleSQLiteQuery {
        val newSql = "$sql WHERE id = ?"
        val argsList = listOf(id.toString())
        return SimpleSQLiteQuery(newSql, argsList.toTypedArray())
    }
}