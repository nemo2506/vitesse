package com.openclassrooms.vitesse.ui.edit

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.vitesse.domain.model.CandidateDetail
import com.openclassrooms.vitesse.domain.usecase.CandidateUseCase
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
class EditViewModel @Inject constructor(
    private val detailUseCase: DetailUseCase,
    private val candidateUseCase: CandidateUseCase,
    appState: SavedStateHandle
) : ViewModel() {
    val candidateId: Long? = appState.get<Long>(ConstantsApp.CANDIDATE_ID)
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        if (candidateId != null) {
            Log.d("MARC", "EditViewModel/$candidateId")
            observeCandidate(candidateId)
        }
    }

    private fun observeCandidate(id: Long) {
        viewModelScope.launch {
            detailUseCase.getCandidateToDetail(id)
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

    fun addCandidate(
        firstName: String? = null,
        lastName: String? = null,
        phone: String? = null,
        email: String? = null,
        isFavorite: Boolean = false,
        photoUri: String? = null,
        note: String? = null,
        date: String? = null,
        salaryClaim: String? = null
    ) {
        viewModelScope.launch {
            candidateUseCase.upsertCandidate(
                firstName,
                lastName,
                phone,
                email,
                isFavorite,
                photoUri,
                note,
                date,
                salaryClaim
            )
                .collect { result ->
                    when (result) {
                        is Result.Loading -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = true,
                                    message = null,
                                    candidateId = null
                                )
                            }
                            delay(1000)
                        }

                        is Result.Success -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    message = null,
                                    candidateId = result.value
                                )
                            }
                        }

                        is Result.Failure -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    message = result.message,
                                    candidateId = null
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
    var candidateId: Long? = null,
    var isFavoriteUpdated: Boolean? = null,
    var isDeleted: Boolean? = null,
    var isLoading: Boolean? = null,
    var message: String? = null,
)