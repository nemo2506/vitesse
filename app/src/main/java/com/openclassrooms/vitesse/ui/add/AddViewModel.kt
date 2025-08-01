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
                                    isUpdated = null,
                                    isCandidateFull = null
                                )
                            }
                            delay(500)
                        }

                        is Result.Success -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    message = null,
                                    isUpdated = result.value,
                                    isCandidateFull = null
                                )
                            }
                        }

                        is Result.Failure -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    message = result.message,
                                    isUpdated = null,
                                    isCandidateFull = null
                                )
                            }
                        }
                    }
                }
        }
    }

    fun checkFirstName(etField: String?) {
        candidateUseCase.validateInfo(etField).let { result ->
            _uiState.update {
                it.copy(
                    isFirstNameCheck = result
                )
            }
        }
    }

    fun checkLastName(etField: String?) {
        candidateUseCase.validateInfo(etField).let { result ->
            _uiState.update {
                it.copy(
                    isLastNameCheck = result
                )
            }
        }
    }

    fun checkPhone(etField: String?) {
        candidateUseCase.validateInfo(etField).let { result ->
            _uiState.update {
                it.copy(
                    isPhoneCheck = result
                )
            }
        }
    }

    fun checkDate(etField: String?) {
        candidateUseCase.validateInfo(etField).let { result ->
            _uiState.update {
                it.copy(
                    isDateCheck = result
                )
            }
        }
    }

    fun validateEmail(etEmail: String) {
        val isReady = candidateUseCase.validateInfo(etEmail)
        val isFormatValid = if (isReady) candidateUseCase.validateEmail(etEmail) else false

        val newState = when {
            !isReady -> EmailState.MandatoryField
            !isFormatValid -> EmailState.InvalidFormat
            else -> EmailState.Valid
        }

        _uiState.update {
            it.copy(isValidEmail = newState)
        }
    }

    fun isCandidateReadyToSave(){
        val full = _uiState.value.isFirstNameCheck == true &&
                _uiState.value.isLastNameCheck == true &&
                _uiState.value.isPhoneCheck == true &&
                _uiState.value.isDateCheck == true &&
                _uiState.value.isValidEmail == EmailState.Valid

        _uiState.update {
            it.copy(isCandidateFull = full)
        }
    }
}

data class UiState(
    var isLoading: Boolean? = false,
    var isUpdated: Boolean? = null,
    var isFirstNameCheck: Boolean? = null,
    var isLastNameCheck: Boolean? = null,
    var isPhoneCheck: Boolean? = null,
    var isDateCheck: Boolean? = null,
    var isValidEmail: EmailState = EmailState.Valid,
    var isCandidateFull: Boolean? = null,
    var message: String? = null
)

sealed class EmailState {
    data object MandatoryField : EmailState()
    data object InvalidFormat : EmailState()
    data object Valid : EmailState()
}