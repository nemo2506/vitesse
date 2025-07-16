package com.openclassrooms.vitesse.ui.candidate

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.openclassrooms.vitesse.domain.model.Candidate
import com.openclassrooms.vitesse.domain.usecase.GetAllCandidateUseCase
import com.openclassrooms.vitesse.domain.usecase.GetFavoriteCandidateUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CandidateViewModel @Inject constructor(
    private val getAllCandidateUseCase: GetAllCandidateUseCase,
    private val getFavoriteCandidateUseCase: GetFavoriteCandidateUseCase,
) : ViewModel(){

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        observeCandidate()
    }

    private fun observeCandidate() {
        viewModelScope.launch {
            launch {
                getAllCandidateUseCase.execute()
                    .catch {
                        _uiState.update { it.copy(isCandidateReady = false, candidate = null) }
                    }
                    .collect { updated ->
                        if (updated.isEmpty()) {
                            _uiState.update { it.copy(isCandidateReady = false, candidate = null) }
                        } else {
                            _uiState.update { it.copy(candidate = updated, isCandidateReady = true) }
                        }
                    }
            }

            launch {
                getFavoriteCandidateUseCase.execute()
                    .catch {
                        _uiState.update { it.copy(isFavoriteReady = false, favorite = null) }
                    }
                    .collect { updated ->
                        Log.d("MARC", "observeCandidate / FAVORITE: " + updated.count())
                        if (updated.isEmpty()) {
                            _uiState.update { it.copy(isFavoriteReady = false, favorite = null) }
                        } else {
                            _uiState.update { it.copy(favorite = updated, isFavoriteReady = true) }
                        }
                    }
            }
        }
    }

    fun getTab(position: Int) {
        _uiState.update { it.copy(tabNum = position) }
    }

}

/**
 * Data class that represents the UI state for the candidate screen.
 *
 * @param candidate List of candidate to display.
 */
data class UiState(
    var candidate: List<Candidate>? = null,
    var favorite: List<Candidate>? = null,
    var isCandidateReady: Boolean? = null,
    var isFavoriteReady: Boolean? = null,
    var tabNum: Int = 0
)