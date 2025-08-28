package com.openclassrooms.vitesse

import com.openclassrooms.vitesse.utils.calculateAge
import com.openclassrooms.vitesse.utils.capitalizeFirstLetter
import com.openclassrooms.vitesse.utils.isPositive
import com.openclassrooms.vitesse.utils.isValidEmail
import com.openclassrooms.vitesse.utils.toDate
import com.openclassrooms.vitesse.utils.toDateDescription
import com.openclassrooms.vitesse.utils.toEmpty
import com.openclassrooms.vitesse.utils.toFrDescription
import com.openclassrooms.vitesse.utils.toGbpDescription
import com.openclassrooms.vitesse.utils.toLocalDateString
import com.openclassrooms.vitesse.utils.toZeroOrLong
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDateTime
import java.util.Locale
import java.util.TimeZone

class UtilsTest {

    /**
     * ** TEST to toFrDescription
     */
    @Test
    fun toFrDescription_null_if_zero() {
        // WHEN
        val value = 0L
        val result = value.toFrDescription()
        // THEN
        assertNull(result)
    }

    @Test
    fun toFrDescription_positive_value() {
        // WHEN
        val result = 123_456L.toFrDescription()
        // THEN
        assertEquals("123\u202F456\u00A0€", result)
    }

    @Test
    fun toFrDescription_negative_value() {
        // WHEN
        val value = -7_890L
        val result = value.toFrDescription()
        // THEN
        assertEquals("-7\u202F890\u00A0€", result)
    }

    /**
     * ** TEST to toGbpDescription
     */
    @Test
    fun toGbpDescription_null_if_zero() {
        // WHEN
        val value = 0L
        val result = value.toGbpDescription(0.5)
        // THEN
        assertNull(result)
    }

    @Test
    fun toGbpDescription_positive_value() {
        // WHEN
        val value = 12_345L
        val result = value.toGbpDescription(0.5)
        // THEN
        assertEquals("£6,172.50", result)
    }

    @Test
    fun toGbpDescription_negative_value() {
        // WHEN
        val value = -12_345L
        val result = value.toGbpDescription(0.5)
        // THEN
        assertEquals("-£6,172.50", result)
    }

    /**
     * ** TEST to calculateAge
     */
    @Test
    fun calculateAge_return_correct_age() {
        // WHEN
        val birthDateTime = LocalDateTime.of(1990, 6, 15, 12, 0)
        val age = birthDateTime.calculateAge()
        // THEN
        assertEquals(35, age)
    }

    @Test
    fun calculateAge_return_age_minus_one_if_birthday_not_reached_yet() {
        // WHEN
        val testAge = 33
        val dateMoins33Ans: LocalDateTime =
            LocalDateTime.now().minusYears(testAge.toLong()).minusDays(1L)
        val age = dateMoins33Ans.calculateAge()
        // THEN
        assertEquals(testAge, age)
    }

    /**
     * ** TEST to calculateAge
     */
    @Test
    fun toDateDescription_returns_correct_string_in_French_locale() {
        // WHEN
        val testDate = LocalDateTime.of(2000, 1, 1, 0, 0)
        Locale.setDefault(Locale.FRENCH)
        val description = testDate.toDateDescription()
        val expectedAge = LocalDateTime.now().year - 2000
        val expected = "01/01/2000 ($expectedAge ans)"
        // THEN
        assertEquals(expected, description)
    }

    @Test
    fun toDateDescription_returns_correct_string_in_English_locale() {
        // WHEN
        val testDate = LocalDateTime.of(2000, 1, 1, 0, 0)
        Locale.setDefault(Locale.ENGLISH)
        val description = testDate.toDateDescription()
        val expectedAge = LocalDateTime.now().year - 2000
        val expected = "01/1/2000 ($expectedAge years old)"
        // THEN
        assertEquals(expected, description)
    }

    @Test
    fun toDateDescription_returns_null_when_receiver_is_null() {
        // WHEN
        val description = (null as LocalDateTime?).toDateDescription()
        // THEN
        assertEquals(null, description)
    }


    /**
     * ** TEST to toDate
     */
    @Test
    fun toDate_returns_LocalDateTime_for_valid_French_date() {
        // WHEN
        Locale.setDefault(Locale.FRENCH)
        val dateString = "31/12/2020"
        val result = dateString.toDate()
        val expected = LocalDateTime.of(2020, 12, 31, 0, 0)
        // THEN
        assertEquals(expected, result)
    }

    @Test
    fun toDate_returns_LocalDateTime_for_valid_English_date() {
        // WHEN
        Locale.setDefault(Locale.ENGLISH)
        val dateString = "12/31/2020"
        val result = dateString.toDate()
        val expected = LocalDateTime.of(2020, 12, 31, 0, 0)
        // THEN
        assertEquals(expected, result)
    }

    @Test
    fun toDate_returns_null_for_invalid_date_string() {
        // WHEN
        Locale.setDefault(Locale.ENGLISH)
        val dateString = "invalid-date"
        val result = dateString.toDate()
        // THEN
        assertNull(result)
    }


    /**
     * ** TEST to toEmpty
     */
    @Test
    fun toEmpty_returns_empty_string_when_Long_is_null() {
        // WHEN
        val value: Long? = null
        val result = value.toEmpty()
        // THEN
        assertEquals("", result)
    }

    @Test
    fun toEmpty_returns_string_representation_when_Long_is_not_null() {
        // WHEN
        val value = 12345L
        val result = value.toEmpty()
        // THEN
        assertEquals("12345", result)
    }

    /**
     * ** TEST to toZeroOrLong
     */
    @Test
    fun toZeroOrLong_returns_0_when_string_is_null() {
        // WHEN
        val value: String? = null
        val result = value.toZeroOrLong()
        // THEN
        assertEquals(0L, result)
    }

    @Test
    fun toZeroOrLong_returns_0_when_string_is_blank() {
        // WHEN
        val value = "   "
        val result = value.toZeroOrLong()
        // THEN
        assertEquals(0L, result)
    }

    @Test
    fun toZeroOrLong_converts_valid_number_string_to_Long() {
        // WHEN
        val value = "12345"
        val result = value.toZeroOrLong()
        // THEN
        assertEquals(12345L, result)
    }

    /**
     * ** TEST to toLocalDateString
     */
    @Test
    fun toLocalDateString_returns_date_formatted_in_French_locale() {
        // WHEN
        Locale.setDefault(Locale.FRENCH)
        val dateTime = LocalDateTime.of(2023, 6, 15, 10, 0)
        val formatted = dateTime.toLocalDateString()
        // THEN
        assertEquals("15/06/2023", formatted)
    }

    @Test
    fun toLocalDateString_returns_date_formatted_in_English_locale() {
        // WHEN
        Locale.setDefault(Locale.ENGLISH)
        val dateTime = LocalDateTime.of(2023, 6, 15, 10, 0)
        val formatted = dateTime.toLocalDateString()
        // THEN
        assertEquals("06/15/2023", formatted)
    }

    /**
     * ** TEST to capitalizeFirstLetter
     */
    @Test
    fun capitalizeFirstLetter_capitalizes_first_letter_if_lowercase() {
        // WHEN
        val input = "bonjour"
        val expected = "Bonjour"
        val result = input.capitalizeFirstLetter()
        // THEN
        assertEquals(expected, result)
    }

    @Test
    fun capitalizeFirstLetter_does_not_change_first_letter_if_already_uppercase() {
        // WHEN
        val input = "Bonjour"
        val expected = "Bonjour"
        val result = input.capitalizeFirstLetter()
        // THEN
        assertEquals(expected, result)
    }

    @Test
    fun capitalizeFirstLetter_handles_empty_string() {
        // WHEN
        val input = ""
        val expected = ""
        val result = input.capitalizeFirstLetter()
        // THEN
        assertEquals(expected, result)
    }

    @Test
    fun capitalizeFirstLetter_does_not_modify_strings_starting_with_non_letter() {
        // WHEN
        val input = "123abc"
        val expected = "123abc"
        val result = input.capitalizeFirstLetter()
        // THEN
        assertEquals(expected, result)
    }

    /**
     * ** TEST to isPositive
     */
    @Test
    fun isPositive_returns_false_for_null() {
        // WHEN
        val value: Long? = null
        // THEN
        assertFalse(value.isPositive())
    }

    @Test
    fun isPositive_returns_false_for_zero() {
        // WHEN
        val value = 0L
        // THEN
        assertFalse(value.isPositive())
    }

    @Test
    fun isPositive_returns_false_for_negative_number() {
        // WHEN
        val value = -10L
        // THEN
        assertFalse(value.isPositive())
    }

    @Test
    fun isPositive_returns_true_for_positive_number() {
        // WHEN
        val value = 15L
        // THEN
        assertTrue(value.isPositive())
    }

    /**
     * ** TEST to isValidEmail
     */
    @Test
    fun isValidEmail_returns_true_for_a_valid_email() {
        // WHEN
        val email = "test@example.com"
        // THEN
        assertTrue(email.isValidEmail())
    }

    @Test
    fun isValidEmail_returns_false_for_invalid_emails() {
        // WHEN
        val invalidEmails = listOf(
            "plainaddress",
            "@no-local-part.com",
            "no-at.domain.com",
            "name@domain@domain.com",
            "name@.com"
        )
        // THEN
        invalidEmails.forEach {
            assertFalse(it.isValidEmail())
        }
    }
}
