package com.openclassrooms.vitesse.ui.add

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.openclassrooms.vitesse.domain.usecase.CandidateUseCase
import com.openclassrooms.vitesse.ui.candidate.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val candidateUseCase: CandidateUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

//    private fun addCandidate(
//        // TODO()
//    )
}