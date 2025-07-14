package com.openclassrooms.vitesse.domain.model

import com.openclassrooms.vitesse.data.entity.CandidateDto

data class Candidate(
    val id: Long? = null,
    var firstName: String,
    var lastName: String,
    var phone: String,
    var email: String,
    var isFavorite: Boolean = false,
    var photoUri: String = ""
) {
    companion object {
        fun fromDto(dto: CandidateDto): Candidate {
            return Candidate(
                    id = dto.id,
                    firstName = dto.firstName,
                    lastName = dto.lastName,
                    phone = dto.phone,
                    email = dto.email,
                    isFavorite = dto.isFavorite,
                    photoUri = dto.photoUri
            )
        }
    }

    fun toDto(): CandidateDto {
        return CandidateDto(
            id = this.id ?: 0,
            firstName = this.firstName,
            lastName = this.lastName,
            phone = this.phone,
            email = this.email,
            isFavorite = this.isFavorite,
            photoUri = this.photoUri
        )
    }
}