package com.openclassrooms.vitesse

import com.openclassrooms.vitesse.data.entity.CandidateDto
import com.openclassrooms.vitesse.data.entity.DetailDto
import com.openclassrooms.vitesse.data.entity.CandidateWithDetailDto
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDateTime

class CandidateWithDetailDtoTest {

    @Test
    fun create_candidate_with_detail_default_values() {
        val candidate = CandidateDto(isFavorite = false)
        val detail = DetailDto(candidateId = candidate.id)
        val candidateWithDetail = CandidateWithDetailDto(candidateDto = candidate, detailDto = detail)

        // Vérifie candidateDto
        assertEquals(0, candidateWithDetail.candidateDto.id)
        assertNull(candidateWithDetail.candidateDto.firstName)
        assertNull(candidateWithDetail.candidateDto.lastName)
        assertFalse(candidateWithDetail.candidateDto.isFavorite)
        assertNull(candidateWithDetail.candidateDto.photoUri)
        assertNull(candidateWithDetail.candidateDto.note)

        // Vérifie detailDto
        assertEquals(0, candidateWithDetail.detailDto.id)
        assertNull(candidateWithDetail.detailDto.date)
        assertNull(candidateWithDetail.detailDto.salaryClaim)
        assertNull(candidateWithDetail.detailDto.phone)
        assertNull(candidateWithDetail.detailDto.email)
        assertEquals(0, candidateWithDetail.detailDto.candidateId)
    }

    @Test
    fun create_candidate_with_detail_custom_values() {
        val candidate = CandidateDto(
            id = 1,
            firstName = "John",
            lastName = "Doe",
            isFavorite = true,
            photoUri = "content://photo.jpg",
            note = "Top candidate"
        )
        val detail = DetailDto(
            id = 10,
            date = LocalDateTime.of(2025, 8, 24, 12, 0),
            salaryClaim = 50000L,
            phone = "0123456789",
            email = "john.doe@example.com",
            candidateId = candidate.id
        )
        val candidateWithDetail = CandidateWithDetailDto(candidateDto = candidate, detailDto = detail)

        // Vérifie candidateDto
        assertEquals(1, candidateWithDetail.candidateDto.id)
        assertEquals("John", candidateWithDetail.candidateDto.firstName)
        assertEquals("Doe", candidateWithDetail.candidateDto.lastName)
        assertTrue(candidateWithDetail.candidateDto.isFavorite)
        assertEquals("content://photo.jpg", candidateWithDetail.candidateDto.photoUri)
        assertEquals("Top candidate", candidateWithDetail.candidateDto.note)

        // Vérifie detailDto
        assertEquals(10, candidateWithDetail.detailDto.id)
        assertEquals(LocalDateTime.of(2025, 8, 24, 12, 0), candidateWithDetail.detailDto.date)
        assertEquals(50000L, candidateWithDetail.detailDto.salaryClaim)
        assertEquals("0123456789", candidateWithDetail.detailDto.phone)
        assertEquals("john.doe@example.com", candidateWithDetail.detailDto.email)
        assertEquals(1, candidateWithDetail.detailDto.candidateId)
    }

    @Test
    fun equality_check_candidate_with_detail() {
        val candidate1 = CandidateDto(id = 1, firstName = "Alice", lastName = "Smith", isFavorite = true)
        val detail1 = DetailDto(id = 5, candidateId = 1, salaryClaim = 40000)
        val dto1 = CandidateWithDetailDto(candidate1, detail1)

        val candidate2 = CandidateDto(id = 1, firstName = "Alice", lastName = "Smith", isFavorite = true)
        val detail2 = DetailDto(id = 5, candidateId = 1, salaryClaim = 40000)
        val dto2 = CandidateWithDetailDto(candidate2, detail2)

        assertEquals(dto1, dto2)
        assertEquals(dto1.hashCode(), dto2.hashCode())
    }
}
