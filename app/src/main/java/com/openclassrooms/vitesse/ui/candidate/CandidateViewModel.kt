package com.openclassrooms.vitesse.ui.candidate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.openclassrooms.vitesse.domain.model.CandidateSummary
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
    val tabStarted: Int = 0

    init {
        observeCandidate(tabStarted, searchTerm = "")
    }

    private fun observeCandidate(fav: Int, searchTerm: String) {
        viewModelScope.launch {
            candidateUseCase.getCandidate(fav, searchTerm)
                .collect { result ->
                    when (result) {
                        is Result.Loading -> {
                            _uiState.update { it.copy(isLoading = true,  candidate = emptyList(), message = null) }

                            delay(1000) // TO TEST
                        }
                        is Result.Success -> {
                            _uiState.update { it.copy(isLoading = false, candidate = result.value, message = null) }
                        }
                        is Result.Failure -> {
                            _uiState.update { it.copy(isLoading = false,  candidate = emptyList(), message = result.message) }
                        }
                    }
                }
        }
    }

    fun getSearch(tab: Int, searchTerm: String) {
        observeCandidate(tab, searchTerm )
    }
}

/**
 * Data class that represents the UI state for the candidate screen.
 *
 * @param candidate List of candidate to display.
 */
data class UiState(
    var candidate: List<CandidateSummary> = emptyList(),
    var isLoading: Boolean? = null,
    var message: String? = null
)