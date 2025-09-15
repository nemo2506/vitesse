package com.openclassrooms.vitesse

import com.openclassrooms.vitesse.data.dao.CandidateDao
import com.openclassrooms.vitesse.data.entity.CandidateDto
import com.openclassrooms.vitesse.data.entity.CandidateWithDetailDto
import com.openclassrooms.vitesse.data.entity.DetailDto
import com.openclassrooms.vitesse.domain.model.Candidate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.whenever
import java.time.LocalDateTime

@ExperimentalCoroutinesApi
class CandidateDaoUnitTest {

    private lateinit var candidateDao: CandidateDao

    @Before
    fun setup_dao() {
        candidateDao = mock(CandidateDao::class.java)
    }

    @Test
    fun upsert_candidate_all_returns_expected_id() = runTest {
        val candidate = CandidateDto(0L, "John", "Doe", false, "uri", "note")
        val detail = DetailDto(0L, LocalDateTime.now(), 50000, "0123456789", "john@example.com", 0L)
        val dto = CandidateWithDetailDto(candidate, detail)
        // WHEN
        whenever(candidateDao.upsertCandidateAll(dto)).thenReturn(10L)
        val result = candidateDao.upsertCandidateAll(dto)
        // THEN
        assertEquals(10L, result)
    }

    @Test
    fun getCandidate_returns_flow_of_candidates() = runTest {
        val candidate = Candidate(
            id = 1L,
            firstName = "Alice",
            lastName = "Smith",
            isFavorite = true,
            photoUri = "uri",
            note = "note"
        )
        // WHEN
        whenever(candidateDao.getCandidate("Alice"))
            .thenReturn(flow { emit(listOf(candidate)) })
        val result = candidateDao.getCandidate("Alice").first()
        // THEN
        assertEquals(1, result.size)
        assertEquals("Alice", result[0]?.firstName)
    }

    @Test
    fun getCandidate_when_empty_returns_empty() = runTest {
        // WHEN
        whenever(candidateDao.getCandidate( ""))
            .thenReturn(flow { emit(listOf()) })
        val result = candidateDao.getCandidate("").first()
        // THEN
        assertEquals(0, result.size)
    }

    @Test
    fun getCandidate_by_id_returns_candidate_with_detail() = runTest {
        val candidate = CandidateDto(1L, "Bob", "Martin", false, "uri", "note")
        val detail = DetailDto(1L, LocalDateTime.now(), 40000, "0102030405", "bob@example.com", 1L)
        val dto = CandidateWithDetailDto(candidate, detail)
        // WHEN
        whenever(candidateDao.getCandidateById(1L)).thenReturn(flow { emit(dto) })
        val result = candidateDao.getCandidateById(1L).first()
        // THEN
        assertNotNull(result)
        assertEquals("Bob", result?.candidateDto?.firstName)
        assertEquals("0102030405", result?.detailDto?.phone)
    }

    @Test
    fun getCandidate_by_id_returns_null_when_is_deleted() = runTest {
        // WHEN
        whenever(candidateDao.getCandidateById(1L)).thenReturn(flow { emit(null) })
        val result = candidateDao.getCandidateById(1L).first()
        // THEN
        assertNull(result)
    }

    @Test
    fun delete_candidate_returns_deleted_count() = runTest {
        // WHEN
        whenever(candidateDao.deleteCandidate(5L)).thenReturn(1)
        val result = candidateDao.deleteCandidate(5L)
        // THEN
        assertEquals(1, result)
    }

    @Test
    fun update_candidate_favorite_returns_updated_count() = runTest {
        // WHEN
        whenever(candidateDao.updateCandidateFavorite(7L, true)).thenReturn(1)
        val result = candidateDao.updateCandidateFavorite(7L, true)
        // THEN
        assertEquals(1, result)
    }
}
