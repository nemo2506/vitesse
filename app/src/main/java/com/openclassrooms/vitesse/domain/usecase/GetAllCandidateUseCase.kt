package com.openclassrooms.vitesse.domain.usecase

import com.openclassrooms.vitesse.data.repository.CandidateRepository
import com.openclassrooms.vitesse.domain.model.Candidate
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetAllCandidateUseCase @Inject constructor(
    private val candidateRepository: CandidateRepository
) {
    fun execute(): Flow<List<Candidate>> {
        return candidateRepository.getAllCandidate()
    }
}