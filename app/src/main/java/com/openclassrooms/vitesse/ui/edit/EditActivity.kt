package com.openclassrooms.vitesse.ui.edit

import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.openclassrooms.vitesse.R
import com.openclassrooms.vitesse.databinding.ActivityAddBinding
import com.openclassrooms.vitesse.domain.model.CandidateDetail
import com.openclassrooms.vitesse.utils.MediaPickerHelper
import com.openclassrooms.vitesse.utils.setVisible
import com.openclassrooms.vitesse.utils.showToastMessage
import com.openclassrooms.vitesse.utils.loadImage
import com.openclassrooms.vitesse.utils.navigateToCandidateScreen
import com.openclassrooms.vitesse.utils.setDateUi
import com.openclassrooms.vitesse.utils.toLocalDateString
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBinding
    private val viewModel: EditViewModel by viewModels()
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var candidate: CandidateDetail
    private var candidateId: Long = 0L
    private var detailId: Long = 0L
    private lateinit var mediaPickerHelper: MediaPickerHelper
    private lateinit var tvFace: ImageView
    private var currentUri: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setGlobalUi()
        observeEdit()
    }

    private fun observeEdit() {
        lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                uiState.isLoading?.let { binding.loading.setVisible(it) }
                uiState.candidate?.let { setUp(it) }
                uiState.isUpdated?.let { this@EditActivity.navigateToCandidateScreen() }
                uiState.message?.showToastMessage(this@EditActivity)
            }
        }
    }

    private fun setGlobalUi() {
        toolbar = binding.toolbar
        toolbar.title = getString(R.string.edit_candidate)
        tvFace = binding.tvFace
        mediaPickerHelper = MediaPickerHelper(this, tvFace) { uri -> currentUri = uri.toString() }
        mediaPickerHelper.setup(this@EditActivity)
        binding.saveButton.setOnClickListener { setSave() }
        binding.etDate.setDateUi(this@EditActivity)
        setToolbar()
    }

    private fun setUp(candidate: CandidateDetail) {
        candidateId = candidate.candidateId!!
        detailId = candidate.detailId!!
        currentUri = candidate.photoUri.toString()
        this@EditActivity.candidate = candidate
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
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setSave() {
        val tvFaceUrl = binding.tvFaceUrl.text.toString()
        tvFace.loadImage(tvFaceUrl)
        viewModel.modifyCandidate(
            candidateId = candidateId,
            detailId = detailId,
            firstName = binding.etFirstname.text.toString(),
            lastName = binding.etLastname.text.toString(),
            phone = binding.etPhone.text.toString(),
            email = binding.etEmail.text.toString(),
            photoUri = currentUri,
            note = binding.etNote.text.toString(),
            date = binding.etDate.text.toString(),
            salaryClaim = binding.etSalaryClaim.text.toString()
        )
    }
}