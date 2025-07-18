package com.openclassrooms.vitesse.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.vitesse.data.entity.CandidateTotal
import com.openclassrooms.vitesse.domain.usecase.GetCandidateByIdUseCase
import com.openclassrooms.vitesse.ui.ConstantsApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getCandidateByIdUseCase: GetCandidateByIdUseCase,
    appState: SavedStateHandle
) : ViewModel() {

    val candidateId: Long? = appState.get<Long>(ConstantsApp.CANDIDATE_ID)
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        if (candidateId != null) {
            observeCandidateTotal(candidateId)
        }
    }

    private fun observeCandidateTotal(id: Long) {
        viewModelScope.launch {
            launch {
                getCandidateByIdUseCase.execute(id)
                    .catch {
                        _uiState.update {
                            it.copy(
                                isCandidateReady = false,
                                candidate = null
                            )
                        }
                    }
                    .collect { updated ->
                        _uiState.update {
                            it.copy(
                                candidate = updated,
                                isCandidateReady = updated.isNotEmpty()
                            )
                        }
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
    var candidate: List<CandidateTotal>? = null,
    var isCandidateReady: Boolean? = null,
    var isFavoriteReady: Boolean? = null
)