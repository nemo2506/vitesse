package com.openclassrooms.vitesse.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.vitesse.domain.model.Candidate
import com.openclassrooms.vitesse.domain.model.Detail
import com.openclassrooms.vitesse.domain.usecase.CandidateUseCase
import com.openclassrooms.vitesse.domain.usecase.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val candidateUseCase: CandidateUseCase
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
        date: LocalDateTime? = null,
        salaryClaim: Long? = null
    ) {
        val candidate = Candidate(
            firstName = firstName,
            lastName = lastName,
            phone = phone,
            email = email,
            isFavorite = isFavorite,
            photoUri = photoUri,
            note = note
        )
        val detail = if (date != null && salaryClaim != null) {
            Detail(
                date = date,
                salaryClaim = salaryClaim,
                note = note ?: "",
                candidateId = 0
            )
        } else null

        viewModelScope.launch {
            candidateUseCase.updateCandidate(candidate, detail)
                .collect { result ->
                    when (result) {
                        is Result.Loading -> {
                            _uiState.update { it.copy(isLoading = true, message = null, isUpdated = false) }
                            delay(1000)
                        }

                        is Result.Success -> {
                            _uiState.update { it.copy(isLoading = false, message = null, isUpdated = result.value) }
                        }

                        is Result.Failure -> {
                            _uiState.update { it.copy( isLoading = false, message = result.message, isUpdated = false) }
                        }
                    }
                }
        }
    }
}


data class UiState(
    var isLoading: Boolean? = false,
    var isUpdated: Boolean? = false,
    var message: String? = null
)