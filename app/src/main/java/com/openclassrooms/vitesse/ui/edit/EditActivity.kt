package com.openclassrooms.vitesse.ui.edit

import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.openclassrooms.vitesse.databinding.ActivityAddBinding
import com.openclassrooms.vitesse.domain.model.CandidateDetail
import com.openclassrooms.vitesse.ui.utils.setVisible
import com.openclassrooms.vitesse.ui.utils.showToastMessage
import com.openclassrooms.vitesse.ui.utils.loadImage
import com.openclassrooms.vitesse.ui.utils.navigateToCandidateScreen
import com.openclassrooms.vitesse.ui.utils.toLocalDateString
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBinding
    private val viewModel: EditViewModel by viewModels()
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var candidate: CandidateDetail
    private lateinit var pickMediaLauncher: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private var currentUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbar()
//        setMenu()
        observeEdit()
    }

    private fun observeEdit() {
        lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                uiState.isLoading?.let { binding.loading.setVisible(it) }
                uiState.candidate?.let { setUpUI(it) }
                uiState.message?.let { showToastMessage(this@EditActivity, it) }
            }
        }
    }

    private fun setUpUI(candidate: CandidateDetail) {
        this@EditActivity.candidate = candidate
        toolbar.title = "Modifier un candidat"
        candidate.photoUri?.let { binding.tvFace.loadImage(it) }
        binding.etFirstname.setText(candidate.firstName)
        binding.etLastname.setText(candidate.lastName)
        binding.etPhone.setText(candidate.phone)
        binding.etEmail.setText(candidate.email)
        binding.etDate.setText(candidate.date?.toLocalDateString())
        binding.etSalaryClaim.setText(candidate.salaryClaim.toString())
        binding.etNote.setText(candidate.note)
    }

    private fun setToolbar() {
        toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            navigateToCandidateScreen(this)
        }
    }
}