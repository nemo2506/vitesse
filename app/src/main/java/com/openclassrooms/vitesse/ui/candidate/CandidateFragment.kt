package com.openclassrooms.vitesse.ui.candidate

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.openclassrooms.vitesse.R
import com.openclassrooms.vitesse.databinding.FragmentCandidateBinding
import kotlinx.coroutines.launch

class CandidateFragment : Fragment() {

    private var _binding: FragmentCandidateBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CandidateViewModel by viewModels()
//    private lateinit var candidateAdapter: CandidateAdapter

    /**
     * Inflates the fragment layout.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCandidateBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Called when the view has been created. Sets up UI components and observers.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setupRecyclerView()
//        setupFab()
        observeCandidate()
    }

    /**
     * Initializes the RecyclerView and its adapter.
     */
//    private fun setupRecyclerView() {
//        candidateAdapter = CandidateAdapter(this)
//        binding.candidateRecyclerview.layoutManager = LinearLayoutManager(context)
//        binding.candidateRecyclerview.adapter = candidateAdapter
//    }

    /**
     * Observes the candidate UI state from the [CandidateViewModel]
     * and updates the list when changes occur.
     */
    private fun observeCandidate() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { flowState ->
                Log.d("MARC", "observeCandidates: $flowState")
//                candidateAdapter.submitList(flowState.candidates)
//                if (flowState.isCandidateReady == false)
//                    Toast.makeText(requireContext(), R.string.exercice_not_ready, Toast.LENGTH_SHORT).show()
//                if (flowState.isCandidateDeleted == false)
//                    Toast.makeText(requireContext(), R.string.exercice_not_deleted, Toast.LENGTH_SHORT).show()
//                if (flowState.isCandidateAdded == false)
//                    Toast.makeText(requireContext(), R.string.exercice_not_added, Toast.LENGTH_SHORT).show()

            }
        }
    }
}