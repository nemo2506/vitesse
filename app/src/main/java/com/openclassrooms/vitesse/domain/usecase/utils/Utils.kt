package com.openclassrooms.vitesse.domain.usecase.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.Locale


fun Long?.toFormatSalary(
    groupingSeparator: Char = ' ',
    decimalSeparator: Char = ',',
    currencySymbol: String = "€",
    pattern: String = "#,### €",
    maxFractionDigits: Int = 0
): String? {
    if (this == null) return null
    val symbols = DecimalFormatSymbols(Locale.FRANCE).apply {
        this.groupingSeparator = groupingSeparator
        this.decimalSeparator = decimalSeparator
        this.currencySymbol = currencySymbol
    }
    val decimalFormat = DecimalFormat(pattern, symbols)
    decimalFormat.maximumFractionDigits = maxFractionDigits
    return decimalFormat.format(this)
}

fun Long.toGbpDescription(): String? {
    if (this == 0L) return null
    val converted = this * 0.86705
    return "soit £ $converted"
}

fun LocalDateTime.calculateAge(): Int {
    val birthLocalDate: LocalDate = this.toLocalDate()
    val today = LocalDate.now()
    return Period.between(birthLocalDate, today).years
}

fun LocalDateTime?.toDateDescription(): String? {
    if (this == null) return null
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val age = this.calculateAge()
    val date = this.format(formatter)
    return "$date ($age ans)"
}

fun String.toLocalDateTime(): LocalDateTime? {
    if (this.isBlank()) return null
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val localDate: LocalDate = LocalDate.parse(this, formatter)
    return localDate.atStartOfDay()
}

fun Long?.toEmpty(): String {
    if (this == null) return ""
    return this.toString()
}

