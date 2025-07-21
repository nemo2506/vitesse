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
    val detail: DetailDto? = null
)

fun CandidateWithDetailDto.toSummary(): CandidateSummary {
    val noteValue = detail?.note ?: ""
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
    Log.d("ERROR", "toDetail: $detail")
    detail ?: return null
    val detailIdValue = detail.candidateId
    val dateValue = detail.date
    val salaryClaimValue = detail.salaryClaim
    val noteValue = detail.note
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