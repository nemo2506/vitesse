package com.openclassrooms.vitesse.ui.add

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.openclassrooms.vitesse.R
import com.openclassrooms.vitesse.databinding.ActivityAddBinding
import com.openclassrooms.vitesse.ui.utils.MediaPickerHelper
import com.openclassrooms.vitesse.ui.utils.loadImage
import com.openclassrooms.vitesse.ui.utils.navigateToCandidateScreen
import com.openclassrooms.vitesse.ui.utils.navigateToDetailScreen
import com.openclassrooms.vitesse.ui.utils.setVisible
import com.openclassrooms.vitesse.ui.utils.showToastMessage
import com.openclassrooms.vitesse.ui.utils.setDateUi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBinding
    private val viewModel: AddViewModel by viewModels()
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var mediaPickerHelper: MediaPickerHelper
    private lateinit var tvFace: ImageView
    private var currentUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUi()
        observeAdd()
    }

    private fun observeAdd() {
        lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                uiState.isLoading?.let { binding.loading.setVisible(it) }
                uiState.message?.let { showToastMessage(this@AddActivity, it) }
                uiState.candidateId?.let { navigateToDetailScreen(this@AddActivity, it) }
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
        setDateUi(this@AddActivity, binding.etDate)
        setToolbar()
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            navigateToCandidateScreen(this)
        }
    }

    private fun setSave() {
        val etLastname = binding.etLastname
        val etEmail = binding.etEmail

        if (etLastname.text?.toString().isNullOrBlank()) {
            etLastname.error = "Ce champ est obligatoire"
        } else {
            etLastname.error = null // pour retirer l’erreur
        }
        if (etEmail.text?.toString().isNullOrBlank()) {
            etEmail.error = "Ce champ est obligatoire"
        } else {
            etEmail.error = null // pour retirer l’erreur
        }

        val tvFaceUrl = binding.tvFaceUrl.text.toString()
        tvFace.loadImage(tvFaceUrl)

        viewModel.addCandidate(
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
}