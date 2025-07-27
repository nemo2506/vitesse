package com.openclassrooms.vitesse.domain.model

data class GbpCurrency (
    val gbp: Double? = 0.0
)

/**
 * Represents the balance report for a user account.
 *
 * @property gbp The available gbp of the json. Can be null if no main account is found.
 */
data class GbpReportModel(val gbp: Double?)
