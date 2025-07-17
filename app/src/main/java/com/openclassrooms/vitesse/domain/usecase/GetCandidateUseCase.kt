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
    fun execute(favorite: Int): Flow<List<Candidate>> {
        val searchTerm = "Emma"
        val sql = "SELECT * FROM candidate ORDER BY name ASC LIKE ?"
        val args = arrayOf("%$searchTerm%")
        val query = SimpleSQLiteQuery(sql, args)
        return candidateRepository.getCandidate(query)
            .map { list ->
                list.map { dto ->
                    Candidate.fromDto(
                        dto.candidate.apply {
                            note = dto.details.firstOrNull()?.note
                        }
                    )
                }
            }
            .catch { emit(emptyList()) }
    }


//    fun execute(searchKey: String = ""): Flow<List<Candidate>> {
//        return candidateRepository.getAllCandidate()
//            .map { list -> list.filter { matchesSearchKey(it, searchKey) } }
//    }
//    private fun matchesSearchKey(candidate: Candidate, key: String): Boolean {
//        if (key.isBlank()) return true
//        return candidate.firstName.contains(key, ignoreCase = true) ||
//                candidate.lastName.contains(key, ignoreCase = true) ||
//                candidate.email.contains(key, ignoreCase = true) ||
//                candidate.note?.contains(key, ignoreCase = true) == true
//    }
}