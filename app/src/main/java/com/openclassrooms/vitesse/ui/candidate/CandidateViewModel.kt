package com.openclassrooms.vitesse.ui.candidate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.vitesse.domain.model.Candidate
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.openclassrooms.vitesse.domain.usecase.CandidateUseCase
import com.openclassrooms.vitesse.domain.usecase.Result
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay

@HiltViewModel
class CandidateViewModel @Inject constructor(
    private val candidateUseCase: CandidateUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
//    val tabStarted: Int = 0

    init {
        observeCandidate(searchTerm = "")
    }

    private fun observeCandidate(searchTerm: String) {
        viewModelScope.launch {
            candidateUseCase.getCandidate(searchTerm)
                .collect { result ->
                    when (result) {
                        is Result.Loading -> {
                            _uiState.update { it.copy(isLoading = true,  candidate = emptyList(),  candidateFav = emptyList(), message = null) }

                            delay(500) // TO TEST
                        }
                        is Result.Success -> {
                            val candidateFav = result.value.filter { it.isFavorite }
                            _uiState.update { it.copy(isLoading = false, candidate = result.value, candidateFav = candidateFav, message = null) }
                        }
                        is Result.Failure -> {
                            _uiState.update { it.copy(isLoading = false, candidate = emptyList(), candidateFav = emptyList(), message = result.message) }
                        }
                    }
                }
        }
    }

    fun getSearch(tab: Int, searchTerm: String) {
        observeCandidate(searchTerm )
    }
}

/**
 * Data class that represents the UI state for the candidate screen.
 *
 * @param candidate List of candidate to display.
 */
data class UiState(
    var candidate: List<Candidate> = emptyList(),
    var candidateFav: List<Candidate> = emptyList(),
    var isLoading: Boolean? = null,
    var message: String? = null
)