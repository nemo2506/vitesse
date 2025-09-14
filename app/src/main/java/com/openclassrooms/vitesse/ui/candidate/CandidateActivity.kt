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
    var currentTab: Int = 0

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
        userCom()
        setAdd()
    }

    private fun observeCandidate() {
        lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                uiState.isLoading?.let { binding.loading.setVisible(it) }
                uiState.candidate.let { candidateAdapter.submitList(it) }
//                uiState.message?.showToastMessage(this@CandidateActivity)
            }
        }
    }

    private fun setupRecyclerView() {
        candidateAdapter = CandidateAdapter { candidate ->
            candidate.id?.let { this@CandidateActivity.navigateToDetailScreen(it) }
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
                currentTab = tab.position
                userCom()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        tabLayout.getTabAt(currentTab)?.select()
    }

    private fun userCom() {
        viewModel.upSearch(currentTab, binding.tvSearchEdit.text.toString())
        binding.tvSearchEdit.doOnTextChanged { text, _, _, _ ->
            viewModel.upSearch(currentTab, text.toString())
        }
    }

    private fun setAdd() {
        binding.fabAdd.setOnClickListener {
            this@CandidateActivity.navigateToAddScreen()
        }
    }
}
