package com.openclassrooms.vitesse.data.entity

import android.util.Log
import androidx.room.Relation
import androidx.room.Embedded
import com.openclassrooms.vitesse.domain.model.CandidateSummary
import com.openclassrooms.vitesse.domain.model.CandidateTotal

data class CandidateWithDetailDto(
    @Embedded val candidateDto: CandidateDto,
    @Relation(parentColumn = "id", entityColumn = "candidateId")
    val detailDto: DetailDto
)

fun CandidateWithDetailDto.toSummary(): CandidateSummary {
    return CandidateSummary(
        id = candidateDto.id,
        firstName = candidateDto.firstName,
        lastName = candidateDto.lastName,
        isFavorite = candidateDto.isFavorite,
        photoUri = candidateDto.photoUri,
        note = detailDto.note
    )
}

fun CandidateWithDetailDto.toDetail(): CandidateTotal {
    Log.d("ERROR", "toDetail: $detailDto")
    return CandidateTotal(
        id = candidateDto.id,
        firstName = candidateDto.firstName,
        lastName = candidateDto.lastName,
        email = candidateDto.email,
        phone = candidateDto.phone,
        isFavorite = candidateDto.isFavorite,
        photoUri = candidateDto.photoUri,
        detailId = detailDto.id,
        date = detailDto.date,
        salaryClaim = detailDto.salaryClaim,
        note = detailDto.note
    )
}