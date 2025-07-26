package com.openclassrooms.vitesse.domain.model

import com.openclassrooms.vitesse.data.entity.CandidateDto
import java.time.LocalDateTime

data class Candidate(
    val id: Long? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var isFavorite: Boolean = false,
    var photoUri: String? = null,
    var note: String? = null
) {
    companion object {
        fun fromDto(dto: CandidateDto): Candidate {
            return Candidate(
                id = dto.id,
                firstName = dto.firstName,
                lastName = dto.lastName,
                isFavorite = dto.isFavorite,
                photoUri = dto.photoUri,
                note = dto.note
            )
        }
    }

    fun toDto(): CandidateDto {
        return CandidateDto(
            id = this.id ?: 0L,
            firstName = this.firstName,
            lastName = this.lastName,
            isFavorite = this.isFavorite,
            photoUri = this.photoUri,
            note = this.note
        )
    }
}

data class CandidateDescription(
    val candidateId: Long? = null,
    val detailId: Long? = null,
    var firstName: String? = "",
    var lastName: String? = "",
    var phone: String? = "",
    var email: String? = "",
    var isFavorite: Boolean = false,
    var photoUri: String? = "",
    var dateDescription: String? = "",
    var salaryClaimDescription: String? = "",
    var salaryClaimGpb: String? = "",
    var note: String? = "",
)

data class CandidateDetail(
    val candidateId: Long? = null,
    var firstName: String? = "",
    var lastName: String? = "",
    var isFavorite: Boolean = false,
    var photoUri: String? = "",
    var note: String? = "",
    val detailId: Long? = null,
    var date: LocalDateTime? = null,
    var salaryClaim: String? = "",
    var phone: String? = "",
    var email: String? = "",
)