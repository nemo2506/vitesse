package com.openclassrooms.vitesse.domain.usecase

import com.openclassrooms.vitesse.data.entity.CandidateDto
import com.openclassrooms.vitesse.data.entity.CandidateWithDetailDto
import com.openclassrooms.vitesse.data.repository.CurrencyRepository
import com.openclassrooms.vitesse.data.repository.DetailRepository
import org.mockito.kotlin.mock
import org.junit.Before
import org.junit.Test

class DetailUseCaseTest {

//    private val detailRepository = mock(DetailRepository::class.java)
//    private val currencyRepository = mock(CurrencyRepository::class.java)
//
//    private lateinit var detailUseCase: DetailUseCase
//
//    @Before
//    fun setup() {
//        detailUseCase = DetailUseCase(detailRepository, currencyRepository)
//    }
//
//    @Test
//    fun getCandidateToDescription_returns_correct_CandidateDescription() = runTest {
//        val id = 1L
//        val candidateDto = CandidateDto(
//            firstName = "Joe",
//            lastName = "Doe",
//            isFavorite = false,
//            note = "Example Note"
//        )
//        val testDto = CandidateWithDetailDto(candidateDto, detailDto) // avec vos objets mockés
//
//        whenever(currencyRepository.getGbp()).thenReturn(Result.Success(1.0))
//        whenever(detailRepository.getCandidateById(id)).thenReturn(flow { emit(testDto) })
//
//        val emissions = detailUseCase.getCandidateToDescription(id).toList()
//
//        assertTrue(emissions[0] is Result.Loading)
//        val success = emissions[1] as? Result.Success ?: fail("Expected Success result")
//        val description = success.value
//
//        assertEquals(testDto.candidateDto.id, description.candidateId)
//    }
}