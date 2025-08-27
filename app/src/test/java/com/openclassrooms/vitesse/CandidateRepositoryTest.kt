package com.openclassrooms.vitesse

import com.openclassrooms.vitesse.data.dao.CandidateDao
import com.openclassrooms.vitesse.data.repository.CandidateRepository
import com.openclassrooms.vitesse.domain.model.Candidate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class CandidateRepositoryTest {

    private lateinit var candidateDao: CandidateDao
    private lateinit var repository: CandidateRepository

    @Before
    fun setup_dao() {
        candidateDao = mock(CandidateDao::class.java)
        repository = CandidateRepository(candidateDao)
    }


    @Test
    fun getCandidateByAttr_returns_candidates_from_dao() = runTest {
        val fav = 1
        val term = "John"
        val expectedList = listOf(mock(Candidate::class.java))
        val flowFromDao = flowOf(expectedList)
        // WHEN
        whenever(candidateDao.getCandidate(fav, "%John%")).thenReturn(flowFromDao)
        val results = repository.getCandidateByAttr(fav, term).toList()
        // THEN
        assertEquals(expectedList, results[0])
        verify(candidateDao).getCandidate(fav, "%John%")
    }

    @Test
    fun getCandidateByAttr_emits_empty_list_on_exception() = runTest {
        val fav = 1
        val term = "John"
        val flowFromDao = flow<List<Candidate?>> {
            throw Exception("Test Exception")
        }
        // WHEN
        whenever(candidateDao.getCandidate(fav, "%John%")).thenReturn(flowFromDao)
        val results = repository.getCandidateByAttr(fav, term).toList()
        // THEN
        assertEquals(emptyList<Candidate?>(), results[0])
        verify(candidateDao).getCandidate(fav, "%John%")
    }
}