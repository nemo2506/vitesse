package com.openclassrooms.vitesse.ui.edit

import androidx.lifecycle.ViewModel
import com.openclassrooms.vitesse.domain.usecase.AddUseCase
import com.openclassrooms.vitesse.domain.usecase.CandidateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(
    private val candidateUseCase: CandidateUseCase,
    private val addUseCase: AddUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

}

data class UiState(
    var isLoading: Boolean? = false,
    var candidateId: Long? = null,
    var message: String? = null
)