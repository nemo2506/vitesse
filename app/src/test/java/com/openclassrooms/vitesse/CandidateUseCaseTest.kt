package com.openclassrooms.vitesse

import com.openclassrooms.vitesse.data.repository.CandidateRepository
import com.openclassrooms.vitesse.domain.model.Candidate
import com.openclassrooms.vitesse.domain.usecase.CandidateUseCase
import com.openclassrooms.vitesse.domain.usecase.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class CandidateUseCaseTest {

    private lateinit var candidateRepository: CandidateRepository
    private lateinit var candidateUseCase: CandidateUseCase

    @Before
    fun setup() {
        candidateRepository = mock(CandidateRepository::class.java)
        candidateUseCase = CandidateUseCase(candidateRepository)
    }

    @Test
    fun get_candidate_emits_success_when_repository_returns_non_empty_list() = runTest {
        // WHEN
        val candidate = Candidate(1L, "John", "DOE", true, "photoUri", "comments")
        whenever(candidateRepository.getCandidateByTerm( "John"))
            .thenReturn(flow { emit(listOf(candidate)) })
        // Act
        val results = mutableListOf<Result<List<Candidate>>>()
        candidateUseCase.getCandidate("John").collect { results.add(it) }
        // THEN
        assertEquals(2, results.size)
        assertTrue(results[0] is Result.Loading)
        assertTrue(results[1] is Result.Success)
        val result = results[1] as Result.Success
        val candidates = result.value
        // THEN
        assertEquals(1, candidates.size)
        assertEquals("John", candidates[0].firstName)
    }

    @Test
    fun get_candidate_emits_failure_when_repository_returns_empty_list() = runTest {
        // WHEN
        whenever(candidateRepository.getCandidateByTerm(""))
            .thenReturn(flow { emit(emptyList<Candidate>()) })
        val results = mutableListOf<Result<*>>()
        candidateUseCase.getCandidate("").collect { results.add(it) }
        // THEN
        assertEquals(2, results.size)
        assertTrue(results[1] is Result.Failure)
        assertEquals("Aucun candidat", (results[1] as Result.Failure).message)
    }

    @Test
    fun upsert_candidate_emits_success_true_when_repository_returns_positive_id() = runTest {
        // WHEN
        whenever(
            candidateRepository.upsertCandidateAll(
                candidateId = 0L,
                detailId = 0L,
                firstName = "John",
                lastName = "DOE",
                phone = "0123456789",
                email = "john@example.com",
                isFavorite = true,
                photoUri = null,
                note = null,
                date = null,
                salaryClaim = 1000L
            )
        ).thenReturn(flow { emit(1L) })
        val results = mutableListOf<Result<*>>()
        candidateUseCase.upsertCandidate(
            firstName = "john",
            lastName = "doe",
            phone = "0123456789",
            email = "john@example.com",
            isFavorite = true,
            salaryClaim = "1000"
        ).collect { results.add(it) }
        // THEN
        assertEquals(Result.Loading, results[0])
        assertEquals(Result.Success(true), results[1])
    }

    @Test
    fun upsert_candidate_emits_success_false_when_repository_returns_0() = runTest {
        // WHEN
        whenever(
            candidateRepository.upsertCandidateAll(
                candidateId = 0L,
                detailId = 0L,
                firstName = "John",
                lastName = "DOE",
                phone = null,
                email = null,
                isFavorite = false,
                photoUri = null,
                note = null,
                date = null,
                salaryClaim = 0L
            )
        ).thenReturn(flow { emit(0L) })
        val results = mutableListOf<Result<*>>()
        candidateUseCase.upsertCandidate(
            firstName = "john",
            lastName = "doe"
        ).collect { results.add(it) }
        // THEN
        assertEquals(Result.Loading, results[0])
        assertEquals(Result.Success(false), results[1])
    }

    @Test
    fun validate_info_returns_false_for_blank_or_null() {
        // THEN
        assertFalse(candidateUseCase.validateInfo(null))
        assertFalse(candidateUseCase.validateInfo(""))
        assertFalse(candidateUseCase.validateInfo("   "))
    }

    @Test
    fun validate_info_returns_true_for_non_empty() {
        // THEN
        assertTrue(candidateUseCase.validateInfo("John"))
    }

    @Test
    fun validate_email_returns_true_for_valid_emails() {
        // THEN
        assertTrue(candidateUseCase.validateEmail("test@example.com"))
    }

    @Test
    fun validate_email_returns_false_for_invalid_emails() {
        // THEN
        assertFalse(candidateUseCase.validateEmail("notAnEmail"))
        assertFalse(candidateUseCase.validateEmail("invalid@"))
    }
}
