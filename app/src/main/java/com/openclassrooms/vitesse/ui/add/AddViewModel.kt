package com.openclassrooms.vitesse.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.vitesse.domain.model.Candidate
import com.openclassrooms.vitesse.domain.model.Detail
import com.openclassrooms.vitesse.domain.usecase.AddUseCase
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
    private val candidateUseCase: CandidateUseCase,
    private val addUseCase: AddUseCase,
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
        val detail =
            Detail(
                date = date,
                salaryClaim = salaryClaim,
                note = note,
                candidateId = 0
            )

        viewModelScope.launch {
            candidateUseCase.updateCandidate(candidate, detail)
                .collect { result ->
                    when (result) {
                        is Result.Loading -> {
                            _uiState.update { it.copy(isLoading = true, message = null, candidateId = null) }
                            delay(1000)
                        }

                        is Result.Success -> {
                            _uiState.update { it.copy(isLoading = false, message = null, candidateId = result.value) }
                        }

                        is Result.Failure -> {
                            _uiState.update { it.copy( isLoading = false, message = result.message, candidateId = null) }
                        }
                    }
                }
        }
    }

    fun getLocalDateTime(birthDate: String) =
        addUseCase.getDateTime(birthDate)
}

data class UiState(
    var isLoading: Boolean? = false,
    var candidateId: Long? = null,
    var message: String? = null
)