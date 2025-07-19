package com.openclassrooms.vitesse.ui.detail

import android.util.Log
import androidx.core.app.NotificationCompat.MessagingStyle.Message
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.vitesse.domain.model.CandidateTotal
import com.openclassrooms.vitesse.domain.usecase.DetailUseCase
import com.openclassrooms.vitesse.ui.ConstantsApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
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
                    result.fold(
                        onSuccess = { candidate ->
                            _uiState.update {
                                it.copy(
                                    candidate = candidate,
                                    isCandidateReady = true
                                )
                            }
                        },
                        onFailure = { error ->
                            _uiState.update {
                                it.copy(
                                    candidate = null,
                                    isCandidateReady = false,
                                    message = error.toString()
                                )
                            }
                        }
                    )
                }
        }
    }
    
    fun updateFavorite( id: Long, fav: Boolean ){
        viewModelScope.launch {
            detailUseCase.updateFavoriteCandidate(id, fav).collect { result ->
                    result.fold(
                        onSuccess = {
                            _uiState.update {
                                it.copy(
                                    isFavoriteUpdated = true
                                )
                            }
                        },
                        onFailure = { error ->
                            _uiState.update {
                                it.copy(
                                    isFavoriteUpdated = false,
                                    message = error.toString()
                                )
                            }
                        }
                    )
            }
        }
    }
    fun setSalary(salary: Long) = detailUseCase.getSalary(salary)
    fun setBirth(birthDate: LocalDateTime) = detailUseCase.getBirth(birthDate)
    fun setSalaryGbp(salary: Long) = detailUseCase.getSalaryGbp(salary)
}

/**
 * Data class that represents the UI state for the candidate screen.
 *
 * @param candidate List of candidate to display.
 */
data class UiState(
    var candidate: CandidateTotal? = null,
    var isCandidateReady: Boolean? = null,
    var isFavoriteReady: Boolean? = null,
    var isFavoriteUpdated: Boolean? = null,
    var isUpdated: Boolean? = null,
    var message: String? = null
)