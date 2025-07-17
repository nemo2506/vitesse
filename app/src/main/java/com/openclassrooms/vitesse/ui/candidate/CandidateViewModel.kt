package com.openclassrooms.vitesse.ui.candidate

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.openclassrooms.vitesse.domain.model.Candidate
import com.openclassrooms.vitesse.domain.usecase.GetCandidateUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CandidateViewModel @Inject constructor(
    private val getCandidateUseCase: GetCandidateUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    val tabStarted: Int = 0

    init {
        observeCandidate(tabStarted)
    }

    private fun observeCandidate(fav: Int) {
        val key = _uiState.value.searchKey?.trim()?.lowercase().orEmpty()
        Log.d("MARC", "observeCandidate/favorite: $fav")

        viewModelScope.launch {
            launch {
                getCandidateUseCase.execute(fav)
                    .catch {
                        _uiState.update { it.copy(isCandidateReady = false, candidate = null) }
                    }
                    .collect { updated ->
                        _uiState.update {
                            it.copy(
                                candidate = updated,
                                isCandidateReady = updated.isNotEmpty()
                            )
                        }
                    }
            }
        }
    }

    fun getTab(position: Int) {
        observeCandidate(position)
    }

    fun searchInsert(key: String?) {
        val cleanKey = key?.trim()?.lowercase()
        _uiState.update { it.copy(searchKey = cleanKey) }
    }
}

/**
 * Data class that represents the UI state for the candidate screen.
 *
 * @param candidate List of candidate to display.
 */
data class UiState(
    var candidate: List<Candidate>? = null,
    var isCandidateReady: Boolean? = null,
    var isFavoriteReady: Boolean? = null,
    var searchKey: String? = null
)