package com.openclassrooms.vitesse.ui.add

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.openclassrooms.vitesse.domain.usecase.CandidateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val candidateUseCase: CandidateUseCase,
    appState: SavedStateHandle
) : ViewModel() {

}