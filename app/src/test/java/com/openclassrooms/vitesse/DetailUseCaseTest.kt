package com.openclassrooms.vitesse

import com.openclassrooms.vitesse.data.entity.CandidateDto
import com.openclassrooms.vitesse.data.entity.CandidateWithDetailDto
import com.openclassrooms.vitesse.data.entity.DetailDto
import com.openclassrooms.vitesse.data.repository.CurrencyRepository
import com.openclassrooms.vitesse.data.repository.DetailRepository
import com.openclassrooms.vitesse.domain.usecase.DetailUseCase
import com.openclassrooms.vitesse.domain.usecase.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.whenever
import java.time.LocalDateTime

@ExperimentalCoroutinesApi
class DetailUseCaseTest {

    private lateinit var detailRepository: DetailRepository
    private lateinit var currencyRepository: CurrencyRepository
    private lateinit var detailUseCase: DetailUseCase

    @Before
    fun setup() {
        detailRepository = mock(DetailRepository::class.java)
        currencyRepository = mock(CurrencyRepository::class.java)
        detailUseCase = DetailUseCase(detailRepository, currencyRepository)
    }

    @Test
    fun getCandidateToDescription_emits_success_when_candidate_is_found() = runTest {
        // Arrange
        val date = LocalDateTime.of(2010,1,1,12,1,1)
        val candidateDto = CandidateDto(1L, "John", "Doe", true, "photoUri", "note")
        val detailDto = DetailDto(2L, date, 60000L, "0102030405", "john@example.com", candidateId = 1L)
        val dto = CandidateWithDetailDto(candidateDto, detailDto)

        whenever(detailRepository.getCandidateById(1L)).thenReturn(flow { emit(dto) })
        whenever(currencyRepository.getGbp()).thenReturn(Result.Success(1.0))

        // Act
        val resultList = mutableListOf<Result<*>>()
        detailUseCase.getCandidateToDescription(1L).collect { resultList.add(it) }

        // Assert
        assertEquals(2, resultList.size)
        assertTrue(resultList[0] is Result.Loading)
        assertTrue(resultList[1] is Result.Success)

        val result = resultList[1] as Result.Success
        assertEquals("John", result)
        assertEquals("Doe", result)
    }

    @Test
    fun getCandidateToDescription_emits_failure_when_candidate_is_null() = runTest {
        whenever(detailRepository.getCandidateById(1L)).thenReturn(flow { emit(null) })

        val resultList = mutableListOf<Result<*>>()
        detailUseCase.getCandidateToDescription(1L).collect { resultList.add(it) }

        assertEquals(2, resultList.size)
        assertTrue(resultList[1] is Result.Failure)
        assertEquals("Candidate deleted", (resultList[1] as Result.Failure).message)
    }

    @Test
    fun deleteCandidate_emits_success() = runTest {
        whenever(detailRepository.deleteCandidate(1L)).thenReturn(flow { emit(1) })

        val resultList = mutableListOf<Result<*>>()
        detailUseCase.deleteCandidate(1L).collect { resultList.add(it) }

        assertEquals(Result.Loading, resultList[0])
        assertEquals(Result.Success(1), resultList[1])
    }

    @Test
    fun updateFavoriteCandidate_emits_success() = runTest {
        whenever(detailRepository.updateFavoriteCandidate(1L, false)).thenReturn(flow { emit(1) })

        val resultList = mutableListOf<Result<*>>()
        detailUseCase.updateFavoriteCandidate(1L, true).collect { resultList.add(it) }

        assertEquals(Result.Loading, resultList[0])
        assertEquals(Result.Success(1), resultList[1])
    }

    @Test
    fun getCandidateToDetail_emits_failure_on_exception() = runTest {
        whenever(detailRepository.getCandidateById(1L)).thenThrow(RuntimeException("DB error"))

        val resultList = mutableListOf<Result<*>>()
        detailUseCase.getCandidateToDetail(1L).collect { resultList.add(it) }

        assertTrue(resultList[0] is Result.Loading)
        assertTrue(resultList[1] is Result.Failure)
        assertEquals("DB error", (resultList[1] as Result.Failure).message)
    }
}
