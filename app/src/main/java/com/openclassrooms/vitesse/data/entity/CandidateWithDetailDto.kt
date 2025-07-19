package com.openclassrooms.vitesse.data.entity

import androidx.room.Relation
import androidx.room.Embedded
import java.time.LocalDateTime
import com.openclassrooms.vitesse.domain.model.CandidateSummary
import com.openclassrooms.vitesse.domain.model.CandidateTotal

data class CandidateWithDetailDto(
    @Embedded val candidate: CandidateDto,
    @Relation(parentColumn = "id", entityColumn = "candidateId")
    val details: List<DetailDto> = emptyList()
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

fun CandidateWithDetailDto.toDetail(): CandidateTotal {
    val detailIdValue = details.firstOrNull()?.candidateId ?: 0
    val dateValue = details.firstOrNull()?.date ?: LocalDateTime.of(1970, 1, 1, 0, 0)
    val salaryClaimValue = details.firstOrNull()?.salaryClaim ?: 0
    val noteValue = details.firstOrNull()?.note ?: ""
    return CandidateTotal(
        id = candidate.id,
        firstName = candidate.firstName,
        lastName = candidate.lastName,
        email = candidate.email,
        phone = candidate.phone,
        isFavorite = candidate.isFavorite,
        photoUri = candidate.photoUri,
        detailId = detailIdValue,
        date = dateValue,
        salaryClaim = salaryClaimValue,
        note = noteValue
    )
}