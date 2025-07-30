package com.openclassrooms.vitesse.ui.edit

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
    private val candidateId: Long? = appState.get<Long>(ConstantsApp.CANDIDATE_ID)
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        if (candidateId != null) {
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
                                    isUpdated = null,
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
                                    isUpdated = null,
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
                                    isUpdated = null,
                                    message = result.message
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

    fun isCandidateReadyToSave() {
        val full = _uiState.value.isFirstNameCheck == true &&
                _uiState.value.isLastNameCheck == true &&
                _uiState.value.isPhoneCheck == true &&
                _uiState.value.isDateCheck == true &&
                _uiState.value.isValidEmail == EmailState.Valid

        _uiState.update {
            it.copy(isCandidateFull = full)
        }
    }

    fun modifyCandidate(
        candidateId: Long,
        detailId: Long,
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
                candidateId = candidateId,
                detailId = detailId,
                firstName = firstName,
                lastName = lastName,
                phone = phone,
                email = email,
                isFavorite = isFavorite,
                photoUri = photoUri,
                note = note,
                date = date,
                salaryClaim = salaryClaim
            ).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update {
                            it.copy(
                                isLoading = true,
                                message = null,
                                isUpdated = null,
                                candidate = null,
                                isCandidateFull = null
                            )
                        }
                        delay(1000)
                    }

                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                message = null,
                                isUpdated = result.value,
                                candidate = null,
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
                                candidate = null,
                                isCandidateFull = null
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
    var isUpdated: Boolean? = null,
    var isFavoriteUpdated: Boolean? = null,
    var isDeleted: Boolean? = null,
    var isLoading: Boolean? = null,
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