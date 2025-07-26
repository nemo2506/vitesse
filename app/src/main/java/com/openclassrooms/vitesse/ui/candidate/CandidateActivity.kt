package com.openclassrooms.vitesse.ui.candidate

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.openclassrooms.vitesse.R
import com.openclassrooms.vitesse.databinding.ActivityCandidateBinding
import com.openclassrooms.vitesse.utils.navigateToAddScreen
import com.openclassrooms.vitesse.utils.navigateToDetailScreen
import com.openclassrooms.vitesse.utils.setVisible
import com.openclassrooms.vitesse.utils.showToastMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CandidateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCandidateBinding
    private val viewModel: CandidateViewModel by viewModels()
    private lateinit var candidateAdapter: CandidateAdapter
    private var choiceTab: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCandidateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUi()
        setupRecyclerView()
        observeCandidate()
    }

    private fun setUi() {
        setupTab()
        userCall()
        setAdd()
    }

    private fun observeCandidate() {
        viewModel.getSearch(choiceTab, "")
        lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                uiState.isLoading?.let { binding.loading.setVisible(it) }
                uiState.candidate.let { candidateAdapter.submitList(it) }
                uiState.message?.showToastMessage(this@CandidateActivity)
            }
        }
    }

    private fun setupRecyclerView() {
        candidateAdapter = CandidateAdapter { candidate ->
            candidate.id?.let { navigateToDetailScreen(this@CandidateActivity, it ) }
        }
        binding.candidateRecyclerview.layoutManager = LinearLayoutManager(this)
        binding.candidateRecyclerview.adapter = candidateAdapter
    }

    private fun setupTab() {
        choiceTab = viewModel.tabStarted
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        tabLayout.addTab(tabLayout.newTab().setText(R.string.candidate_all))
        tabLayout.addTab(tabLayout.newTab().setText(R.string.candidate_favorites))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                choiceTab = tab.position
                userCall()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        tabLayout.getTabAt(viewModel.tabStarted)?.select()
    }

    private fun userCall() {
        viewModel.getSearch(choiceTab, binding.tvSearchEdit.text.toString())
        binding.tvSearchEdit.doOnTextChanged { text, _, _, _ ->
            viewModel.getSearch(choiceTab, text.toString())
        }
    }

    private fun setAdd() {
        val fabAdd = binding.fabAdd
        fabAdd.setOnClickListener {
            navigateToAddScreen(this@CandidateActivity)
        }
    }
}
