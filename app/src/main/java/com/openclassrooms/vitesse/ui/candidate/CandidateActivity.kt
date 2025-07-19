package com.openclassrooms.vitesse.ui.candidate

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.openclassrooms.vitesse.R
import com.openclassrooms.vitesse.databinding.ActivityCandidateBinding
import com.openclassrooms.vitesse.ui.ConstantsApp
import com.openclassrooms.vitesse.ui.detail.DetailActivity
import com.openclassrooms.vitesse.ui.upsert.UpsertActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CandidateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCandidateBinding
    private val viewModel: CandidateViewModel by viewModels()
    private lateinit var candidateAdapter: CandidateAdapter
    private var choiceUser: Int = 0
    private lateinit var fabAdd: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCandidateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fabAdd = findViewById(R.id.fab_add)

        setupRecyclerView()
        setupTab()
        observeCandidates()
        choiceUser = viewModel.tabStarted
        viewModel.getSearch(choiceUser, "")
        userCall()
        setupFabAdd()
    }

    private fun setupRecyclerView() {
        candidateAdapter = CandidateAdapter { candidate ->
            val candidateId = candidate.id
            toDetail(candidateId)
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

    private fun observeCandidates() {
        lifecycleScope.launch {
            viewModel.uiState.collect { flowState ->
                val candidate = flowState.candidate
                candidateAdapter.submitList(candidate)
            }
        }
    }

    private fun userCall() {
        viewModel.getSearch(choiceUser, binding.tvSearchEdit.text.toString())
        binding.tvSearchEdit.doOnTextChanged { text, _, _, _ ->
            viewModel.getSearch(choiceUser, text.toString())
        }
    }

    private fun setupFabAdd() {
        fabAdd.setOnClickListener {
            val intent = Intent(this, UpsertActivity::class.java)
            startActivity(intent)
        }
    }

    private fun toDetail(candidateId: Long) {
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra(ConstantsApp.CANDIDATE_ID, candidateId)
        }
        startActivity(intent)
    }
}
