package com.openclassrooms.vitesse.domain.usecase

import com.openclassrooms.vitesse.data.repository.CandidateRepository
import com.openclassrooms.vitesse.domain.model.Candidate
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpsertCandidateUseCase @Inject constructor(
    private val candidateRepository: CandidateRepository
) {
    fun execute(candidate: Candidate): Flow<Result<Unit>> {
        return candidateRepository.upsertCandidate(candidate)
    }
}