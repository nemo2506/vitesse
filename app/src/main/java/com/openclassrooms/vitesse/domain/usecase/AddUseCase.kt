package com.openclassrooms.vitesse.domain.usecase

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class AddUseCase @Inject constructor(){

    fun getDateTime(birthDate: String): LocalDateTime {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val localDate: LocalDate = LocalDate.parse(birthDate, formatter)
        return localDate.atStartOfDay()
    }
}