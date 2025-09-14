package com.openclassrooms.vitesse.ui.candidate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.vitesse.domain.model.Candidate
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.combine
import com.openclassrooms.vitesse.domain.usecase.CandidateUseCase
import com.openclassrooms.vitesse.domain.usecase.Result
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

@HiltViewModel
class CandidateViewModel @Inject constructor(
    private val candidateUseCase: CandidateUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    private val filterState = MutableStateFlow(Pair(0, "")) // Pair(fav, term)

    init {
        observeCandidate()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeCandidate() {
        viewModelScope.launch {
            filterState.flatMapLatest { (fav, term) ->
                candidateUseCase.getCandidate(term).map { result ->
                    when (result) {
                        is Result.Success -> Result.Success(
                            if (fav == 1) result.value.filter { it.isFavorite }
                            else result.value
                        )
                        else -> result
                    }
                }
            }.collect { result ->
                when (result) {
                    is Result.Loading -> _uiState.update {
                        it.copy(isLoading = true, candidate = emptyList(), message = null)
                    }
                    is Result.Success -> _uiState.update {
                        it.copy(isLoading = false, candidate = result.value, message = null)
                    }
                    is Result.Failure -> _uiState.update {
                        it.copy(isLoading = false, candidate = emptyList(), message = result.message)
                    }
                }
            }
        }
    }

    fun upSearch(fav: Int? = null, term: String? = null) {
        val (currentFav, currentTerm) = filterState.value
        filterState.value = Pair(fav ?: currentFav, term ?: currentTerm)
    }
}

/**
 * Data class that represents the UI state for the candidate screen.
 *
 * @param candidate List of candidate to display.
 */
data class UiState(
    var candidate: List<Candidate> = emptyList(),
    var isLoading: Boolean? = null,
    var message: String? = null
)