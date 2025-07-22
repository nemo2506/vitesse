package com.openclassrooms.vitesse.domain.model

import java.time.LocalDateTime

data class CandidateTotal(
    val id: Long,
    val firstName: String?,
    val lastName: String,
    val email: String,
    val phone: String?,
    val photoUri: String?,
    val isFavorite: Boolean,
    val detailId: Long,
    val date: LocalDateTime?,
    val salaryClaim: Long?,
    val note: String?
)

data class CandidateSummary(
    val id: Long,
    val firstName: String?,
    val lastName: String,
    val isFavorite: Boolean,
    val photoUri: String?,
    val note: String?
)

data class CandidateDetail(
    val candidateId: Long? = null,
    val detailId: Long? = null,
    var firstName: String?,
    var lastName: String,
    var phone: String?,
    var email: String,
    var isFavorite: Boolean = false,
    var photoUri: String?,
    var dateDescription: String?,
    var salaryClaimDescription: String?,
    var salaryClaimGpb: String?,
    var note: String?
)