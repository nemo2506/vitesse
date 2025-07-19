package com.openclassrooms.vitesse.ui.upsert

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.openclassrooms.vitesse.domain.model.Candidate
import com.openclassrooms.vitesse.domain.usecase.CandidateUseCase
import com.openclassrooms.vitesse.ui.ConstantsApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class UpsertViewModel @Inject constructor(
    private val candidateUseCase: CandidateUseCase,
    appState: SavedStateHandle
) : ViewModel() {

    val candidateId: Long? = appState.get<Long>(ConstantsApp.CANDIDATE_ID)
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

}

/**
 * Data class that represents the UI state for the candidate screen.
 *
 * @param candidate List of candidate to display.
 */
data class UiState(
    var candidate: List<Candidate>? = null,
    var isCandidateReady: Boolean? = null,
    var isFavoriteReady: Boolean? = null,
    var searchKey: String? = null
)