package com.openclassrooms.vitesse

import com.openclassrooms.vitesse.domain.model.Detail
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDateTime

class DetailTest {

    @Test
    fun create_detail_with_default_values() {
        val detail = Detail(candidateId = 1L)

        assertEquals(0L, detail.id)
        assertNull(detail.date)
        assertNull(detail.salaryClaim)
        assertNull(detail.phone)
        assertNull(detail.email)
        assertEquals(1L, detail.candidateId)
    }

    @Test
    fun create_detail_with_custom_values() {
        val date = LocalDateTime.of(1990, 1, 1, 12, 0)
        val detail = Detail(
            id = 5L,
            date = date,
            salaryClaim = 50000,
            phone = "0601020304",
            email = "test@example.com",
            candidateId = 10L
        )

        assertEquals(5L, detail.id)
        assertEquals(date, detail.date)
        assertEquals(50000L, detail.salaryClaim)
        assertEquals("0601020304", detail.phone)
        assertEquals("test@example.com", detail.email)
        assertEquals(10L, detail.candidateId)
    }

    @Test
    fun detail_toDto_and_fromDto() {
        val date = LocalDateTime.of(2000, 5, 15, 9, 30)
        val detail = Detail(
            id = 3L,
            date = date,
            salaryClaim = 40000L,
            phone = "0612345678",
            email = "detail@example.com",
            candidateId = 7L
        )

        val dto = detail.toDto()
        val fromDto = Detail.fromDto(dto)

        assertEquals(detail.id, fromDto.id)
        assertEquals(detail.date, fromDto.date)
        assertEquals(detail.salaryClaim, fromDto.salaryClaim)
        assertEquals(detail.phone, fromDto.phone)
        assertEquals(detail.email, fromDto.email)
        assertEquals(detail.candidateId, fromDto.candidateId)
    }
}
