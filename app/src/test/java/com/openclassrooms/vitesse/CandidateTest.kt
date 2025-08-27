package com.openclassrooms.vitesse

import com.openclassrooms.vitesse.domain.model.Candidate
import com.openclassrooms.vitesse.domain.model.CandidateDescription
import com.openclassrooms.vitesse.domain.model.CandidateDetail
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDateTime

class CandidateTest {

    @Test
    fun create_candidate_with_default_values() {
        val candidate = Candidate()

        assertEquals(0L, candidate.id)
        assertNull(candidate.firstName)
        assertNull(candidate.lastName)
        assertFalse(candidate.isFavorite)
        assertNull(candidate.photoUri)
        assertNull(candidate.note)
    }

    @Test
    fun create_candidate_with_custom_values() {
        val candidate = Candidate(
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
    fun candidate_toDto_and_fromDto() {
        val candidate = Candidate(
            id = 2L,
            firstName = "Alice",
            lastName = "Smith",
            isFavorite = true,
            note = "Promising"
        )
        val dto = candidate.toDto()
        val fromDto = Candidate.fromDto(dto)

        assertEquals(candidate.id, fromDto.id)
        assertEquals(candidate.firstName, fromDto.firstName)
        assertEquals(candidate.lastName, fromDto.lastName)
        assertEquals(candidate.isFavorite, fromDto.isFavorite)
        assertEquals(candidate.note, fromDto.note)
    }

    @Test
    fun create_candidateDescription_with_default_values() {
        val desc = CandidateDescription()

        assertEquals(0L, desc.candidateId)
        assertEquals(0L, desc.detailId)
        assertEquals("", desc.firstName)
        assertEquals("", desc.lastName)
        assertEquals("", desc.phone)
        assertEquals("", desc.email)
        assertFalse(desc.isFavorite)
        assertEquals("", desc.photoUri)
        assertEquals("", desc.dateDescription)
        assertEquals("", desc.salaryClaimDescription)
        assertEquals("", desc.salaryClaimGpb)
        assertEquals("", desc.note)
    }

    @Test
    fun create_candidateDetail_with_default_values() {
        val detail = CandidateDetail()

        assertEquals(0L, detail.candidateId)
        assertEquals(0L, detail.detailId)
        assertEquals("", detail.firstName)
        assertEquals("", detail.lastName)
        assertFalse(detail.isFavorite)
        assertEquals("", detail.photoUri)
        assertEquals("", detail.note)
        assertNull(detail.date)
        assertEquals("", detail.salaryClaim)
        assertEquals("", detail.phone)
        assertEquals("", detail.email)
    }

    @Test
    fun candidateDetail_with_custom_values() {
        val date = LocalDateTime.of(1990,1,1,10,0)
        val detail = CandidateDetail(
            candidateId = 1L,
            detailId = 2L,
            firstName = "Bob",
            lastName = "Lee",
            isFavorite = true,
            photoUri = "content://photo.jpg",
            note = "Detail note",
            date = date,
            salaryClaim = "50000",
            phone = "0601020304",
            email = "bob@example.com"
        )

        assertEquals(1L, detail.candidateId)
        assertEquals(2L, detail.detailId)
        assertEquals("Bob", detail.firstName)
        assertEquals("Lee", detail.lastName)
        assertTrue(detail.isFavorite)
        assertEquals("content://photo.jpg", detail.photoUri)
        assertEquals("Detail note", detail.note)
        assertEquals(date, detail.date)
        assertEquals("50000", detail.salaryClaim)
        assertEquals("0601020304", detail.phone)
        assertEquals("bob@example.com", detail.email)
    }
}
