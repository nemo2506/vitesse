package com.openclassrooms.vitesse.data.entity

import android.util.Log
import androidx.room.Relation
import androidx.room.Embedded
import com.openclassrooms.vitesse.domain.model.CandidateSummary
import com.openclassrooms.vitesse.domain.model.CandidateTotal

data class CandidateWithDetailDto(
    @Embedded val candidateDto: CandidateDto,
    @Relation(parentColumn = "id", entityColumn = "candidateId")
    val detailDto: DetailDto? = null
)

fun CandidateWithDetailDto.toSummary(): CandidateSummary {
    val noteValue = detailDto?.note ?: ""
    return CandidateSummary(
        id = candidateDto.id,
        firstName = candidateDto.firstName,
        lastName = candidateDto.lastName,
        isFavorite = candidateDto.isFavorite,
        photoUri = candidateDto.photoUri,
        note = noteValue
    )
}

fun CandidateWithDetailDto.toDetail(): CandidateTotal? {
    Log.d("ERROR", "toDetail: $detailDto")
    detailDto ?: return null
    val detailIdValue = detailDto.candidateId
    val dateValue = detailDto.date
    val salaryClaimValue = detailDto.salaryClaim
    val noteValue = detailDto.note
    return CandidateTotal(
        id = candidateDto.id,
        firstName = candidateDto.firstName,
        lastName = candidateDto.lastName,
        email = candidateDto.email,
        phone = candidateDto.phone,
        isFavorite = candidateDto.isFavorite,
        photoUri = candidateDto.photoUri,
        detailId = detailIdValue,
        date = dateValue,
        salaryClaim = salaryClaimValue,
        note = noteValue
    )
}