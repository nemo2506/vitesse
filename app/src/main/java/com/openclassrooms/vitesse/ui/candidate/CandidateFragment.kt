package com.openclassrooms.vitesse.ui.candidate

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.openclassrooms.vitesse.R
import com.openclassrooms.vitesse.databinding.FragmentCandidateBinding
import com.openclassrooms.vitesse.ui.ConstantsApp
import com.openclassrooms.vitesse.ui.detail.DetailFragment
import com.openclassrooms.vitesse.ui.upsert.UpsertFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CandidateFragment : Fragment() {

    private var _binding: FragmentCandidateBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CandidateViewModel by viewModels()
    private lateinit var candidateAdapter: CandidateAdapter
    private var choiceUser: Int = 0
    private lateinit var fabAdd: View

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
        fabAdd = view.findViewById<FloatingActionButton>(R.id.fab_add)
        setupRecyclerView()
        setupTab()
        observerCandidate()
        choiceUser = viewModel.tabStarted
        viewModel.getSearch(choiceUser, "")
        userCall()
        upsertCandidate()
    }

    /**
     * Initializes the RecyclerView and its adapter.
     */
    private fun setupRecyclerView() {
        candidateAdapter = CandidateAdapter { candidate ->
            val candidateId = candidate.id
            Log.d("MARC", "setupRecyclerView: $candidateId")
            if (candidateId != null) {
                toDetail(candidateId)
            }

        }

        binding.candidateRecyclerview.layoutManager = LinearLayoutManager(context)
        binding.candidateRecyclerview.adapter = candidateAdapter
    }

    private fun setupTab() {

        val tabLayout = view?.findViewById<TabLayout>(R.id.tab_layout)
        tabLayout?.addTab(tabLayout.newTab().setText(R.string.candidate_all))
        tabLayout?.addTab(tabLayout.newTab().setText(R.string.candidate_favorites))

        tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                choiceUser = tab.position
                userCall()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        tabLayout?.getTabAt(viewModel.tabStarted)?.select()

    }

    private fun observerCandidate() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { flowState ->
                candidateAdapter.submitList(flowState.candidate)
            }
        }
    }

    private fun userCall() {
        viewModel.getSearch(choiceUser, binding.tvSearchEdit.text.toString())
        binding.tvSearchEdit.doOnTextChanged { text, _, _, _ ->
            viewModel.getSearch(choiceUser, text.toString())
        }
    }

    private val upsertCandidate = {
        fabAdd.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, UpsertFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    /**
     * Navigates to [DetailFragment] and passes the logged-in user's ID via Intent.
     *
     * @param currentId The EditText containing the user identifier.
     */
    private fun toDetail(currentId: Long) {
        val intent = Intent(requireContext(), DetailFragment::class.java).apply {
            putExtra(ConstantsApp.CANDIDATE_ID, currentId)
        }
        startActivity(intent)
    }
}