package com.openclassrooms.vitesse.domain.usecase

import com.openclassrooms.vitesse.data.repository.CandidateRepository
import com.openclassrooms.vitesse.data.repository.DetailRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateFavoriteUseCase @Inject constructor(
    private val detailRepository: DetailRepository
) {
    fun execute(id: Long, fav: Boolean): Flow<Result<Unit>> {
        return detailRepository.updateFavoriteCandidate(id, !fav) // INVERSE LA VALEUR FAV POUR LA MODIFIER
    }
}