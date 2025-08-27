package com.openclassrooms.vitesse

import com.openclassrooms.vitesse.data.converter.Converters
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.Instant
import java.util.TimeZone

class ConvertersTest {

    private val converters = Converters()

    @Test
    fun fromTimestamp_return_null_if_input_is_null() {
        // WHEN
        val result = converters.fromTimestamp(null)
        // THEN
        assertNull(result)
    }

    @Test
    fun fromTimeStamp_convert_epoch_millis_to_local_date_time() {
        // WHEN
        val millis = System.currentTimeMillis()
        val expected = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDateTime()
        val result = converters.fromTimestamp(millis)
        // THEN
        assertEquals(expected, result)
    }

    @Test
    fun dateToTimeStamp_returns_null_if_input_is_null() {
        // WHEN
        val result = converters.dateToTimestamp(null)
        // THEN
        assertNull(result)
    }

    @Test
    fun dateToTimeStamp_with_fixed_date_returns_correct_timestamp() {
        // WHEN
        val fixedDate = LocalDateTime.of(2023, 6, 1, 12, 0, 0)
        val expected = fixedDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val result = converters.dateToTimestamp(fixedDate)
        // THEN
        assertEquals(expected, result)
    }

    @Test
    fun date_to_timestamp_handles_daylight_saving_time_change_correctly() {
        // WHEN
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Paris"))
        // SUMMER HOURS
        val dateBefore = LocalDateTime.of(2023, 3, 26, 1, 59, 59)
        val timestampBefore = converters.dateToTimestamp(dateBefore)
        val convertedDateBefore = converters.fromTimestamp(timestampBefore)
        // WINTER HOURS
        val dateAfter = LocalDateTime.of(2023, 3, 26, 3, 0, 0)
        val timestampAfter = converters.dateToTimestamp(dateAfter)
        val convertedDateAfter = converters.fromTimestamp(timestampAfter)
        // THEN
        assertEquals(dateBefore, convertedDateBefore)
        assertEquals(dateAfter, convertedDateAfter)
        // WHEN
        val diffMillis = timestampAfter!! - timestampBefore!!
        // THEN
        assertEquals(1000, diffMillis)
    }

}
