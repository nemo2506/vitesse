package com.openclassrooms.vitesse.ui.edit

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.openclassrooms.vitesse.databinding.ActivityAddBinding
import com.openclassrooms.vitesse.domain.model.CandidateDetail
import com.openclassrooms.vitesse.utils.MediaPickerHelper
import com.openclassrooms.vitesse.utils.setVisible
import com.openclassrooms.vitesse.utils.showToastMessage
import com.openclassrooms.vitesse.utils.loadImage
import com.openclassrooms.vitesse.utils.navigateToCandidateScreen
import com.openclassrooms.vitesse.utils.navigateToDetailScreen
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
    private lateinit var mediaPickerHelper: MediaPickerHelper
    private lateinit var tvFace: ImageView
    private var currentUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUi()
        observeEdit()
    }

    private fun observeEdit() {
        lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                uiState.isLoading?.let { binding.loading.setVisible(it) }
                uiState.candidate?.let { setUpUI(it) }
                uiState.candidateId?.let { navigateToDetailScreen(this@EditActivity, it) }
                uiState.message?.let { showToastMessage(this@EditActivity, it) }
            }
        }
    }

    private fun setUi() {
        toolbar = binding.toolbar
        toolbar.title = "Ajouter un candidat"
        tvFace = binding.tvFace
        mediaPickerHelper = MediaPickerHelper(this, tvFace) { uri -> currentUri = uri }
        mediaPickerHelper.setup()
        binding.saveButton.setOnClickListener { setSave() }
        setDateUi(this@EditActivity, binding.etDate)
        setToolbar()
    }

    private fun setSave() {
//        val etLastname = binding.etLastname
//        val etEmail = binding.etEmail
//
//        if (etLastname.text?.toString().isNullOrBlank()) {
//            etLastname.error = "Ce champ est obligatoire"
//        } else {
//            etLastname.error = null // pour retirer l’erreur
//        }
//        if (etEmail.text?.toString().isNullOrBlank()) {
//            etEmail.error = "Ce champ est obligatoire"
//        } else {
//            etEmail.error = null // pour retirer l’erreur
//        }

        val tvFaceUrl = binding.tvFaceUrl.text.toString()
        tvFace.loadImage(tvFaceUrl)

        viewModel.upsertCandidate(
            firstName = binding.etFirstname.text.toString(),
            lastName = binding.etLastname.text.toString(),
            phone = binding.etPhone.text.toString(),
            email = binding.etEmail.text.toString(),
            photoUri = currentUri.toString(),
            note = binding.etNote.text.toString(),
            date = binding.etDate.text.toString(),
            salaryClaim = binding.etSalaryClaim.text.toString()
        )
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