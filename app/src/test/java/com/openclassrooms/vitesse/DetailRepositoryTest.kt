package com.openclassrooms.vitesse

import com.openclassrooms.vitesse.data.dao.CandidateDao
import com.openclassrooms.vitesse.data.entity.CandidateWithDetailDto
import com.openclassrooms.vitesse.data.repository.DetailRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class DetailRepositoryTest {

    private lateinit var candidateDao: CandidateDao
    private lateinit var repository: DetailRepository

    @Before
    fun setup_dao() {
        candidateDao = mock(CandidateDao::class.java)
        repository = DetailRepository(candidateDao)
    }

    @Test
    fun getCandidateById_returns_candidate() = runTest {
        // GIVEN
        val candidateId = 42L
        val expected = mock(CandidateWithDetailDto::class.java)
        whenever(candidateDao.getCandidateById(candidateId)).thenReturn(flowOf(expected))

        // WHEN
        val result = repository.getCandidateById(candidateId).toList()

        // THEN
        assertEquals(expected, result[0])
        verify(candidateDao).getCandidateById(candidateId)
    }

    @Test
    fun getCandidateById_returns_null_on_exception() = runTest {
        // GIVEN
        val candidateId = 42L
        whenever(candidateDao.getCandidateById(candidateId)).thenReturn(
            flow { throw RuntimeException("Test error") }
        )

        // WHEN
        val result = repository.getCandidateById(candidateId).toList()

        // THEN
        assertEquals(null, result[0])
        verify(candidateDao).getCandidateById(candidateId)
    }

    @Test
    fun deleteCandidate_returns_rows_deleted() = runTest {
        // GIVEN
        val candidateId = 99L
        whenever(candidateDao.deleteCandidate(candidateId)).thenReturn(1)

        // WHEN
        val result = repository.deleteCandidate(candidateId).toList()

        // THEN
        assertEquals(1, result[0])
        verify(candidateDao).deleteCandidate(candidateId)
    }

    @Test
    fun deleteCandidate_returns_zero_on_exception() = runTest {
        // GIVEN
        val candidateId = 99L
        whenever(candidateDao.deleteCandidate(candidateId)).thenThrow(RuntimeException("DB error"))

        // WHEN
        val result = repository.deleteCandidate(candidateId).toList()

        // THEN
        assertEquals(0, result[0])
        verify(candidateDao).deleteCandidate(candidateId)
    }

    @Test
    fun updateFavoriteCandidate_returns_rows_updated() = runTest {
        // GIVEN
        val candidateId = 123L
        whenever(candidateDao.updateCandidateFavorite(candidateId, true)).thenReturn(1)

        // WHEN
        val result = repository.updateFavoriteCandidate(candidateId, true).toList()

        // THEN
        assertEquals(1, result[0])
        verify(candidateDao).updateCandidateFavorite(candidateId, true)
    }

    @Test
    fun updateFavoriteCandidate_returns_zero_on_exception() = runTest {
        // GIVEN
        val candidateId = 123L
        whenever(candidateDao.updateCandidateFavorite(candidateId, true))
            .thenThrow(RuntimeException("DB error"))

        // WHEN
        val result = repository.updateFavoriteCandidate(candidateId, true).toList()

        // THEN
        assertEquals(0, result[0])
        verify(candidateDao).updateCandidateFavorite(candidateId, true)
    }
}
