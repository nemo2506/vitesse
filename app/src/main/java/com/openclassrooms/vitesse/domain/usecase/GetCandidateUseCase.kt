package com.openclassrooms.vitesse.domain.usecase

import com.openclassrooms.vitesse.data.repository.CandidateRepository
import com.openclassrooms.vitesse.domain.model.Candidate
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCandidateUseCase @Inject constructor(
    private val candidateRepository: CandidateRepository
) {
    fun execute(favorite: Int): Flow<List<Candidate>> {
        return candidateRepository.getCandidate(favorite)
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