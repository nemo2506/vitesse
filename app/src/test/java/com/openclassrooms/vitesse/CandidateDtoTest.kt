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
        val candidate = CandidateDto(isFavorite = false)

        assertEquals(0, candidate.id)
        assertNull(candidate.firstName)
        assertNull(candidate.lastName)
        assertFalse(candidate.isFavorite)
        assertNull(candidate.photoUri)
        assertNull(candidate.note)
    }

    @Test
    fun create_candidate_with_custom_values() {
        val candidate = CandidateDto(
            id = 1L,
            firstName = "John",
            lastName = "Doe",
            isFavorite = true,
            photoUri = "content://photo.jpg",
            note = "Top candidate"
        )

        assertEquals(1L, candidate.id)
        assertEquals("John", candidate.firstName)
        assertEquals("Doe", candidate.lastName)
        assertTrue(candidate.isFavorite)
        assertEquals("content://photo.jpg", candidate.photoUri)
        assertEquals("Top candidate", candidate.note)
    }

    @Test
    fun copy_candidate() {
        val original = CandidateDto(
            id = 5L,
            firstName = "Alice",
            lastName = "Smith",
            isFavorite = true
        )

        val copy = original.copy(id = 6, note = "Updated")

        assertNotEquals(original.id, copy.id)
        assertEquals("Alice", copy.firstName)
        assertEquals("Smith", copy.lastName)
        assertTrue(copy.isFavorite)
        assertEquals("Updated", copy.note)
    }

    @Test
    fun equality_check() {
        val candidate1 = CandidateDto(id = 1L, firstName = "Bob", lastName = "Lee", isFavorite = false)
        val candidate2 = CandidateDto(id = 1L, firstName = "Bob", lastName = "Lee", isFavorite = false)

        assertEquals(candidate1, candidate2)
        assertEquals(candidate1.hashCode(), candidate2.hashCode())
    }
}
