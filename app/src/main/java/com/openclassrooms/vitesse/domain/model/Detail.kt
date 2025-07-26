package com.openclassrooms.vitesse.domain.model

import com.openclassrooms.vitesse.data.entity.DetailDto
import java.time.LocalDateTime

data class Detail(
    val id: Long? = 0,
    var date: LocalDateTime? = null,
    var salaryClaim: Long? = null,
    var phone: String? = null,
    var email: String? = null,
    var candidateId: Long

) {
    companion object {
        fun fromDto(dto: DetailDto): Detail {
            return Detail(
                id = dto.id,
                date = dto.date,
                salaryClaim = dto.salaryClaim,
                phone = dto.phone,
                email = dto.email,
                candidateId = dto.candidateId
            )
        }
    }

    fun toDto(): DetailDto {
        return DetailDto(
            id = this.id ?: 0,
            date = this.date,
            salaryClaim = this.salaryClaim,
            phone = this.phone,
            email = this.email,
            candidateId = this.candidateId
        )
    }
}
