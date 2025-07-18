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
    val note: String
)

// Fonction d’extension pour créer un résumé à partir d’une instance
fun CandidateWithDetailDto.toSummary(): CandidateSummary {
    val noteValue = details.firstOrNull()?.note ?: 0f
    return CandidateSummary(
        id = candidate.id,
        firstName = candidate.firstName,
        lastName = candidate.lastName,
        isFavorite = candidate.isFavorite,
        note = noteValue.toString()
    )
}