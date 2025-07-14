package com.openclassrooms.vitesse.ui.candidate

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.openclassrooms.vitesse.domain.model.Candidate
import com.openclassrooms.vitesse.domain.usecase.GetAllCandidateUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CandidateViewModel @Inject constructor(
    private val getAllCandidateUseCase: GetAllCandidateUseCase
) : ViewModel(){

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        observeCandidate()
    }

    private fun observeCandidate() {
        viewModelScope.launch {
            getAllCandidateUseCase.execute()
                .catch {
                    _uiState.update { it.copy(isCandidateReady = false) }
                }
                .collect { updated ->
                    if (updated.isEmpty()) {
                        _uiState.update { it.copy(isCandidateReady = false) }
                    } else {
                        _uiState.update { it.copy(candidate = updated) }
                    }
                }
        }
    }
}
/**
 * Data class that represents the UI state for the candidate screen.
 *
 * @param candidate List of candidate to display.
 */
data class UiState(
    var candidate: List<Candidate>? = null,
    var isCandidateReady: Boolean? = null
)