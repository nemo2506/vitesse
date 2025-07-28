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
                firstName = firstName,
                lastName = lastName,
                phone = phone,
                email = email,
                isFavorite = isFavorite,
                photoUri = photoUri,
                note = note,
                date = date,
                salaryClaim = salaryClaim
            )
                .collect { result ->
                    when (result) {
                        is Result.Loading -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = true,
                                    message = null,
                                    isUpdated = null
                                )
                            }
                            delay(1000)
                        }

                        is Result.Success -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    message = null,
                                    isUpdated = result.value
                                )
                            }
                        }

                        is Result.Failure -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    message = result.message,
                                    isUpdated = null
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
    var isUpdated: Boolean? = null,
    var message: String? = null
)