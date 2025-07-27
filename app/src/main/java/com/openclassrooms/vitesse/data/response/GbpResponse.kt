package com.openclassrooms.vitesse.data.response

import com.openclassrooms.vitesse.domain.model.GbpReportModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Represents the API response for a login attempt.
 *
 * @property granted Indicates whether the login was successful.
 */
@JsonClass(generateAdapter = true)
data class GbpResponse(
    @Json(name = "gbp")
    val gbp: Double
) {
    /**
     * Converts the API response model to the domain model [GbpReportModel].
     *
     * @return A domain-level representation of the login result.
     */
    fun toDomainModel(): GbpReportModel {
        return GbpReportModel(gbp)
    }
}