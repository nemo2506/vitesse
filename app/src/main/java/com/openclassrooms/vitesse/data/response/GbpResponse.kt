package com.openclassrooms.vitesse.data.response

import com.openclassrooms.vitesse.domain.model.GbpReportModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Represents the API response for a login attempt.
 *
 * @property gbp Indicates whether the login was successful.
 */
@JsonClass(generateAdapter = true)
data class GbpResponse(
    @Json(name = "date")
    val date: String,
    @Json(name = "eur")
    val eur: Map<String, Double>
) {
    fun toDomainModel(): GbpReportModel {
        val gbp = eur["gbp"] ?: 0.0
        return GbpReportModel(gbp)
    }
}