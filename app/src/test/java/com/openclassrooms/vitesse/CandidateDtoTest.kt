package com.openclassrooms.vitesse

import com.openclassrooms.vitesse.data.entity.CandidateDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CandidateDtoTest {

    @Test
    fun create_candidate_with_default_values() {
        // WHEN
        val candidate = CandidateDto(isFavorite = false)

        // THEN
        assertEquals(0L, candidate.id)
        assertNull(candidate.firstName)
        assertNull(candidate.lastName)
        assertFalse(candidate.isFavorite)
        assertNull(candidate.photoUri)
        assertNull(candidate.note)
    }

    @Test
    fun create_candidate_with_custom_values() {
        // WHEN
        val candidate = CandidateDto(
            id = 1L,
            firstName = "John",
            lastName = "Doe",
            isFavorite = true,
            photoUri = "content://photo.jpg",
            note = "Top candidate"
        )

        // THEN
        assertEquals(1L, candidate.id)
        assertEquals("John", candidate.firstName)
        assertEquals("Doe", candidate.lastName)
        assertTrue(candidate.isFavorite)
        assertEquals("content://photo.jpg", candidate.photoUri)
        assertEquals("Top candidate", candidate.note)
    }

    @Test
    fun copy_candidate() {
        // WHEN
        val original = CandidateDto(
            id = 5L,
            firstName = "Alice",
            lastName = "Smith",
            isFavorite = true
        )

        val copy = original.copy(id = 6L, note = "Updated")

        // THEN
        assertNotEquals(original.id, copy.id)
        assertEquals("Alice", copy.firstName)
        assertEquals("Smith", copy.lastName)
        assertTrue(copy.isFavorite)
        assertEquals("Updated", copy.note)
    }

    @Test
    fun equality_check() {
        // WHEN
        val candidate1 = CandidateDto(id = 1L, firstName = "Bob", lastName = "Lee", isFavorite = false)
        val candidate2 = CandidateDto(id = 1L, firstName = "Bob", lastName = "Lee", isFavorite = false)

        // THEN
        assertEquals(candidate1, candidate2)
        assertEquals(candidate1.hashCode(), candidate2.hashCode())
    }
}
