package com.openclassrooms.vitesse.data.entity

import androidx.room.Relation
import androidx.room.Embedded

data class CandidateWithDetailDto(
    @Embedded val candidate: CandidateDto,
    @Relation(parentColumn = "id", entityColumn = "candidateId")
    val details: List<DetailDto> = emptyList()
)

// Classe conteneur résumé
data class CandidateSummary(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val isFavorite: Boolean,
    val photoUri: String,
    val note: String
)

fun CandidateWithDetailDto.toSummary(): CandidateSummary {
    val noteValue = details.firstOrNull()?.note ?: ""
    return CandidateSummary(
        id = candidate.id,
        firstName = candidate.firstName,
        lastName = candidate.lastName,
        isFavorite = candidate.isFavorite,
        photoUri = candidate.photoUri,
        note = noteValue
    )
}