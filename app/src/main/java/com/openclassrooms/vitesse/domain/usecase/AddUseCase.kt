package com.openclassrooms.vitesse.domain.usecase

import android.util.Log
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import javax.inject.Inject

class AddUseCase @Inject constructor(){

    fun getDateTime(birthDate: String?): LocalDateTime? {
        if (birthDate.isNullOrBlank()) {
            Log.d("MARC", "getDateTime/isNullOrBlank: $birthDate")
            return null
        }
        return try {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val localDate: LocalDate = LocalDate.parse(birthDate, formatter)
            localDate.atStartOfDay()
        } catch (e: DateTimeParseException) {
            Log.d("MARC", "getDateTime/DateTimeParseException: $e")
            null
        }
    }
}