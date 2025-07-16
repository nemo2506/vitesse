package com.openclassrooms.vitesse.domain.model

import com.openclassrooms.vitesse.data.entity.DetailDto
import java.util.Date

data class Detail(
    val id: Long? = null,
    var date: Date,
    var salaryClaim: Long,
    var note: String,
    var candidateId: Long

    ) {
    companion object {
        fun fromDto(dto: DetailDto): Detail {
            return Detail(
                    id = dto.id,
                    date = dto.date,
                    salaryClaim = dto.salaryClaim,
                    note = dto.note,
                    candidateId = dto.candidateId
            )
        }
    }

    fun toDto(): DetailDto {
        return DetailDto(
            id = this.id ?: 0,
            date = this.date,
            salaryClaim = this.salaryClaim,
            note = this.note,
            candidateId = this.candidateId
        )
    }
}