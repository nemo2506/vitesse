package com.openclassrooms.vitesse.data.entity

import androidx.room.Relation
import androidx.room.Embedded

data class CandidateWithDetailDto(
    @Embedded val candidate: CandidateDto,
    @Relation( parentColumn = "id", entityColumn = "candidateId" )
    val details: List<DetailDto> = emptyList()
)