package com.openclassrooms.vitesse

import com.openclassrooms.vitesse.data.entity.DetailDto
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDateTime

class DetailDtoTest {

    @Test
    fun create_detail_with_default_values() {
        val detail = DetailDto(candidateId = 0)

        assertEquals(0, detail.id)
        assertNull(detail.date)
        assertNull(detail.salaryClaim)
        assertNull(detail.phone)
        assertNull(detail.email)
        assertEquals(0, detail.candidateId)
    }

    @Test
    fun create_detail_with_custom_values() {
        val date = LocalDateTime.of(2025, 8, 24, 15, 30)
        val detail = DetailDto(
            id = 10,
            date = date,
            salaryClaim = 60000L,
            phone = "0123456789",
            email = "example@test.com",
            candidateId = 1
        )

        assertEquals(10L, detail.id)
        assertEquals(date, detail.date)
        assertEquals(60000L, detail.salaryClaim)
        assertEquals("0123456789", detail.phone)
        assertEquals("example@test.com", detail.email)
        assertEquals(1L, detail.candidateId)
    }

    @Test
    fun copy_detail_and_modify_values() {
        val original = DetailDto(
            id = 5,
            date = LocalDateTime.of(2025, 8, 24, 12, 0),
            salaryClaim = 50000L,
            phone = "0987654321",
            email = "original@test.com",
            candidateId = 2
        )

        val copy = original.copy(id = 6, salaryClaim = 55000L)

        assertNotEquals(original.id, copy.id)
        assertEquals(55000L, copy.salaryClaim)
        assertEquals(original.date, copy.date)
        assertEquals(original.phone, copy.phone)
        assertEquals(original.email, copy.email)
        assertEquals(original.candidateId, copy.candidateId)
    }

    @Test
    fun equality_check() {
        val detail1 = DetailDto(id = 1, candidateId = 1, salaryClaim = 40000)
        val detail2 = DetailDto(id = 1, candidateId = 1, salaryClaim = 40000)

        assertEquals(detail1, detail2)
        assertEquals(detail1.hashCode(), detail2.hashCode())
    }
}
