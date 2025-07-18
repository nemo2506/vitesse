package com.openclassrooms.vitesse.domain.usecase

import android.util.Log
import androidx.sqlite.db.SimpleSQLiteQuery
import com.openclassrooms.vitesse.data.entity.CandidateTotal
import com.openclassrooms.vitesse.data.entity.toDetail
import com.openclassrooms.vitesse.data.repository.CandidateRepository
import com.openclassrooms.vitesse.data.repository.DetailRepository
import com.openclassrooms.vitesse.domain.model.Candidate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCandidateByIdUseCase @Inject constructor(
    private val detailRepository: DetailRepository,
    private val candidateRepository: CandidateRepository
) {
    fun execute(id: Long): Flow<List<CandidateTotal>> {
        val query = searchQuery(id)
        return candidateRepository.getCandidate(query)
            .map { listDto ->
                listDto.map { dto ->
                    dto.toDetail()
                }
            }
            .catch { e ->
                Log.d("ERROR", "executeError: $e")
                emit(emptyList())
            }
    }

    private fun searchQuery(
        id: Long,
        sql: String = """
        SELECT candidate.*
        FROM candidate
        LEFT JOIN detail ON candidate.id = detail.candidateId
    """.trimIndent()
    ): SimpleSQLiteQuery {
        val newSql = "$sql WHERE id = ?"
        val argsList = listOf(id)
        return SimpleSQLiteQuery(newSql, argsList.toTypedArray())
    }
}