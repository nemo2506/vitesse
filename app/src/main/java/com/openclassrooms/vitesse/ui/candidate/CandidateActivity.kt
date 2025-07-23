package com.openclassrooms.vitesse.ui.candidate

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.openclassrooms.vitesse.R
import com.openclassrooms.vitesse.databinding.ActivityCandidateBinding
import com.openclassrooms.vitesse.ui.utils.navigateToAddScreen
import com.openclassrooms.vitesse.ui.utils.navigateToDetailScreen
import com.openclassrooms.vitesse.ui.utils.setVisible
import com.openclassrooms.vitesse.ui.utils.showToastMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CandidateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCandidateBinding
    private val viewModel: CandidateViewModel by viewModels()
    private lateinit var candidateAdapter: CandidateAdapter
    private var choiceUser: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCandidateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        choiceUser = viewModel.tabStarted
        setupRecyclerView()
        setupTab()
        observeCandidate()
        userCall()
        setAdd()
    }

    private fun observeCandidate() {
        viewModel.getSearch(choiceUser, "")
        lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                uiState.isLoading?.let { binding.loading.setVisible(it) }
                uiState.candidate.let { candidateAdapter.submitList(it) }
                uiState.message?.let {
                    Log.d("MARC", "observeCandidate/message: $it")
                    showToastMessage(this@CandidateActivity, it) }
            }
        }
    }

    private fun setupRecyclerView() {
        candidateAdapter = CandidateAdapter { candidate ->
            navigateToDetailScreen(this@CandidateActivity, candidate.id)
        }
        binding.candidateRecyclerview.layoutManager = LinearLayoutManager(this)
        binding.candidateRecyclerview.adapter = candidateAdapter
    }

    private fun setupTab() {
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        tabLayout.addTab(tabLayout.newTab().setText(R.string.candidate_all))
        tabLayout.addTab(tabLayout.newTab().setText(R.string.candidate_favorites))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                choiceUser = tab.position
                userCall()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        tabLayout.getTabAt(viewModel.tabStarted)?.select()
    }

    private fun userCall() {
        viewModel.getSearch(choiceUser, binding.tvSearchEdit.text.toString())
        binding.tvSearchEdit.doOnTextChanged { text, _, _, _ ->
            viewModel.getSearch(choiceUser, text.toString())
        }
    }

    private fun setAdd() {
        val fabAdd = binding.fabAdd
        fabAdd.setOnClickListener {
            navigateToAddScreen(this@CandidateActivity)
        }
    }
}
