package com.openclassrooms.vitesse.ui.candidate

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.openclassrooms.vitesse.R
import com.openclassrooms.vitesse.databinding.FragmentCandidateBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CandidateFragment : Fragment() {

    private var _binding: FragmentCandidateBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CandidateViewModel by viewModels()
    private lateinit var candidateAdapter: CandidateAdapter

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
        setupRecyclerView()
        setupTab()
        observeCandidate()
    }

    private fun setupTab() {
        val tabLayout = view?.findViewById<TabLayout>(R.id.tab_layout)
        tabLayout?.addTab(tabLayout.newTab().setText(R.string.candidate_all))
        tabLayout?.addTab(tabLayout.newTab().setText(R.string.candidate_favorites))
        tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
//                    0 -> observeCandidate()
//                    1 -> observeFavoritesCandidate()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

    }

    /**
     * Initializes the RecyclerView and its adapter.
     */
    private fun setupRecyclerView() {
        candidateAdapter = CandidateAdapter()
        binding.candidateRecyclerview.layoutManager = LinearLayoutManager(context)
        binding.candidateRecyclerview.adapter = candidateAdapter
    }

    /**
     * Observes the candidate UI state from the [CandidateViewModel]
     * and updates the list when changes occur.
     */
    private fun observeCandidate() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { flowState ->
                candidateAdapter.submitList(flowState.candidate)
                if (flowState.isCandidateReady == false)
                    Toast.makeText(
                        requireContext(),
                        R.string.candidate_not_ready,
                        Toast.LENGTH_SHORT
                    ).show()
//                if (flowState.isCandidateDeleted == false)
//                    Toast.makeText(requireContext(), R.string.candidate_not_deleted, Toast.LENGTH_SHORT).show()
//                if (flowState.isCandidateAdded == false)
//                    Toast.makeText(requireContext(), R.string.candidate_not_added, Toast.LENGTH_SHORT).show()

            }
        }
    }
}