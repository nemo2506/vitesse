package com.openclassrooms.vitesse.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.vitesse.domain.usecase.CandidateUseCase
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
class AddViewModel @Inject constructor(
    private val candidateUseCase: CandidateUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun addCandidate(
        firstName: String,
        lastName: String,
        phone: String,
        email: String,
        isFavorite: Boolean = false,
        photoUri: String = "",
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

data class UiState(
    var isLoading: Boolean? = false,
    var candidateId: Long? = null,
    var message: String? = null
)