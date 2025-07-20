package com.openclassrooms.vitesse.ui.detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.vitesse.domain.model.CandidateDetail
import com.openclassrooms.vitesse.domain.usecase.DetailUseCase
import com.openclassrooms.vitesse.ui.ConstantsApp
import com.openclassrooms.vitesse.domain.usecase.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val detailUseCase: DetailUseCase,
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
            detailUseCase.getCandidateById(id)
                .collect { result ->
                    when (result) {
                        is Result.Loading -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = true,
                                    candidate = null,
                                    isFavoriteUpdated = false,
                                    isDeleted = false,
                                    message = null
                                )
                            }
                            delay(1000)  // TO TEST
                        }

                        is Result.Success -> {
                            _uiState.update {
                                it.copy(
                                    candidate = result.value,
                                    isLoading = false,
                                    isFavoriteUpdated = false,
                                    isDeleted = false,
                                    message = null
                                )
                            }
                        }

                        is Result.Failure -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    isFavoriteUpdated = false,
                                    isDeleted = false,
                                    candidate = null,
                                    message = result.message
                                )
                            }
                        }
                    }
                }
        }
    }

    fun updateFavorite(id: Long, fav: Boolean) {
        viewModelScope.launch {
            detailUseCase.updateFavoriteCandidate(id, fav)
                .collect { result ->
                    when (result) {
                        is Result.Loading -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = true,
                                    isFavoriteUpdated = false,
                                    isDeleted = false,
                                    candidate = null,
                                    message = null
                                )
                            }
                            delay(1000)  // TO TEST
                        }

                        is Result.Success -> {
                            _uiState.update {
                                it.copy(
                                    isFavoriteUpdated = true,
                                    isLoading = false,
                                    candidate = null,
                                    isDeleted = false,
                                    message = null
                                )
                            }
                        }

                        is Result.Failure -> {
                            _uiState.update {
                                it.copy(
                                    candidate = null,
                                    isFavoriteUpdated = true,
                                    isDeleted = false,
                                    isLoading = false,
                                    message = result.message
                                )
                            }
                        }
                    }
                }
        }
    }

    fun deleteCandidate(candidateId: Long) {
        viewModelScope.launch {
            detailUseCase.deleteCandidate(candidateId)
                .collect { result ->
                    when (result) {
                        is Result.Loading -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = true,
                                    isDeleted = false,
                                    candidate = null,
                                    isFavoriteUpdated = false,
                                    message = null
                                )
                            }
                            delay(1000)  // TO TEST
                        }

                        is Result.Success -> {
                            _uiState.update {
                                it.copy(
                                    isDeleted = true,
                                    candidate = null,
                                    isFavoriteUpdated = false,
                                    isLoading = false,
                                    message = null
                                )
                            }
                        }

                        is Result.Failure -> {
                            _uiState.update {
                                it.copy(
                                    isDeleted = false,
                                    candidate = null,
                                    isFavoriteUpdated = false,
                                    isLoading = false,
                                    message = result.message
                                )
                            }
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
    var candidate: CandidateDetail? = null,
    var isFavoriteUpdated: Boolean? = null,
    var isDeleted: Boolean? = null,
    var isLoading: Boolean? = null,
    var message: String? = null,
)