package com.openclassrooms.vitesse.ui.add

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.openclassrooms.vitesse.R
import com.openclassrooms.vitesse.databinding.ActivityAddBinding
import com.openclassrooms.vitesse.utils.MediaPickerHelper
import com.openclassrooms.vitesse.utils.loadImage
import com.openclassrooms.vitesse.utils.navigateToCandidateScreen
import com.openclassrooms.vitesse.utils.navigateToDetailScreen
import com.openclassrooms.vitesse.utils.setVisible
import com.openclassrooms.vitesse.utils.showToastMessage
import com.openclassrooms.vitesse.utils.setDateUi
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
        setGlobalUi()
        observeAdd()
    }

    private fun observeAdd() {
        lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                uiState.isLoading?.let { binding.loading.setVisible(it) }
                uiState.message?.showToastMessage(this@AddActivity)
                uiState.isUpdated?.let { this@AddActivity.navigateToCandidateScreen() }
            }
        }
    }

    private fun setGlobalUi() {
        toolbar = binding.toolbar
        toolbar.title = getString(R.string.add_candidate)
        tvFace = binding.tvFace
        mediaPickerHelper = MediaPickerHelper(this@AddActivity, tvFace) { uri -> currentUri = uri }
        mediaPickerHelper.setup(this@AddActivity)
        binding.saveButton.setOnClickListener { setSave() }
        binding.etDate.setDateUi(this@AddActivity)
        setToolbar()
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            this@AddActivity.navigateToCandidateScreen()
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun setSave() {
        val tvFaceUrl = binding.tvFaceUrl.text.toString()
        tvFace.loadImage(tvFaceUrl)

        val etFirstname = binding.etFirstname.text.toString()
        val etLastname = binding.etLastname.text.toString()
        val etPhone = binding.etPhone.text.toString()
        val tvEmail = binding.tvEmail
        val etEmail = binding.etEmail.text.toString()
        val etDate = binding.etDate.text.toString()

        if (etEmail.isBlank()) {
            tvEmail.error = "L'email est requis"
            return
        } else if (!isValidEmail(etEmail)) {
            tvEmail.error = "Email invalide"
            return
        } else {
            binding.tvEmail.error = null
        }

        val etFields = listOf(
            etFirstname,
            etLastname,
            etPhone,
            etEmail,
            etDate
        )

        viewModel.addCandidate(
            firstName = etFirstname,
            lastName = etLastname,
            phone = etPhone,
            email = etEmail,
            date = etDate,
            photoUri = currentUri.toString(),
            note = binding.etNote.text.toString(),
            salaryClaim = binding.etSalaryClaim.text.toString()
        )
    }
}