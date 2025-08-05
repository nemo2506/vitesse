package com.openclassrooms.vitesse

import com.openclassrooms.vitesse.data.converter.Converters
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.Instant
import java.util.TimeZone
import kotlin.math.abs

class ConvertersTest {

    private val converters = Converters()

    @Test
    fun fromTimestamp_retourneNull_siEntreeEstNull() {
        val result = converters.fromTimestamp(null)
        assertNull(result)
    }

    @Test
    fun fromTimestamp_convertitEpochMillisEnLocalDateTime() {
        val millis = System.currentTimeMillis()
        val expected = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDateTime()
        val result = converters.fromTimestamp(millis)
        assertEquals(expected, result)
    }

    @Test
    fun dateToTimestamp_retourneNull_siEntreeEstNull() {
        val result = converters.dateToTimestamp(null)
        assertNull(result)
    }

    @Test
    fun dateToTimestamp_withFixedDate_returnsCorrectTimestamp() {
        val fixedDate = LocalDateTime.of(2023, 6, 1, 12, 0, 0)
        val expected = fixedDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val result = converters.dateToTimestamp(fixedDate)

        assertEquals(expected, result)
    }

    @Test
    fun testConversionWithDaylightSaving() {
        // Fixer la zone par défaut pour le test
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Paris"))

        // Date avant changement d'heure d'été 2023 (26 mars à 2h)
        val dateBefore = LocalDateTime.of(2023, 3, 26, 1, 59, 59)
        val timestampBefore = converters.dateToTimestamp(dateBefore)
        val convertedDateBefore = converters.fromTimestamp(timestampBefore)

        // Date après changement (26 mars à 3h, heure sautée)
        val dateAfter = LocalDateTime.of(2023, 3, 26, 3, 0, 0)
        val timestampAfter = converters.dateToTimestamp(dateAfter)
        val convertedDateAfter = converters.fromTimestamp(timestampAfter)

        // Vérifier que les conversions sont cohérentes
        assertEquals(dateBefore, convertedDateBefore)
        assertEquals(dateAfter, convertedDateAfter)

        // Vérifier que la différence des timestamps correspond au saut d'heure (60 min)
        val diffMillis = timestampAfter!! - timestampBefore!!
        assertEquals(60 * 60 * 1000, diffMillis)
    }

}
