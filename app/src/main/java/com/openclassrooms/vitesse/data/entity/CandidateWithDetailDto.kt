package com.openclassrooms.vitesse.data.entity

import android.util.Log
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

fun CandidateWithDetailDto.toDetail(): CandidateTotal? {
    Log.d("ERROR", "toDetail: $details")
    details.firstOrNull() ?: return null
    val detailIdValue = details.firstOrNull()?.candidateId
    val dateValue = details.firstOrNull()?.date
    val salaryClaimValue = details.firstOrNull()?.salaryClaim
    val noteValue = details.firstOrNull()?.note
    return CandidateTotal(
        id = candidate.id,
        firstName = candidate.firstName,
        lastName = candidate.lastName,
        email = candidate.email,
        phone = candidate.phone,
        isFavorite = candidate.isFavorite,
        photoUri = candidate.photoUri,
        detailId = detailIdValue ?: return null,
        date = dateValue ?: return null,
        salaryClaim = salaryClaimValue ?: return null,
        note = noteValue  ?: return null
    )
}