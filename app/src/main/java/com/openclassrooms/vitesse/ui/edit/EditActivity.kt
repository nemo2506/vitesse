package com.openclassrooms.vitesse.ui.edit

import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputLayout
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
    private var candidateId: Long = 0L
    private var detailId: Long = 0L
    private lateinit var mediaPickerHelper: MediaPickerHelper
    private lateinit var tvFace: ImageView
    private var currentUri: String? = null
    private var etFirstname: String? = null
    private var etLastname: String? = null
    private var etPhone: String? = null
    private var etEmail: String? = null
    private var etDate: String? = null
    private lateinit var tvEmail: TextInputLayout

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
                uiState.candidate?.let { setCandidate(it) }
                uiState.isFirstNameCheck?.let { setInfoErrorNotify(binding.tvFirstname, it) }
                uiState.isLastNameCheck?.let { setInfoErrorNotify(binding.tvLastname, it) }
                uiState.isPhoneCheck?.let { setInfoErrorNotify(binding.tvPhone, it) }
                uiState.isDateCheck?.let { setInfoErrorNotify(binding.tvDate, it) }
                setEmailNotify(uiState.isValidEmail)
                if (uiState.isCandidateFull == true) candidateSave()
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
        mediaPickerHelper.setup()
        binding.saveButton.setOnClickListener { setVerify() }
        binding.etDate.setDateUi(this@EditActivity)
        setToolbar()
    }

    private fun setCandidate(candidate: CandidateDetail) {

        candidateId = candidate.candidateId!!
        detailId = candidate.detailId!!
        currentUri = candidate.photoUri.toString()
        candidate.photoUri?.let { binding.tvFace.loadImage(it) }

        if (etFirstname == null)
            binding.etFirstname.setText(candidate.firstName)
        if (etLastname == null)
            binding.etLastname.setText(candidate.lastName)
        if (etPhone == null)
            binding.etPhone.setText(candidate.phone)
        if (etEmail == null)
            binding.etEmail.setText(candidate.email)
        if (etDate == null)
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

    private fun setInfoErrorNotify(
        tv: TextInputLayout,
        valid: Boolean,
        message: String = getString(R.string.mandatory_field)
    ) {
        if (valid) tv.error = null
        else tv.error = message
    }

    private fun setEmailNotify(state: EmailState) {
        binding.tvEmail.error = when (state) {
            EmailState.MandatoryField -> getString(R.string.mandatory_field)
            EmailState.InvalidFormat -> getString(R.string.invalide_format)
            EmailState.Valid -> null
        }
    }

    private fun setVerify() {
        etFirstname = binding.etFirstname.text.toString()
        etLastname = binding.etLastname.text.toString()
        etPhone = binding.etPhone.text.toString()
        etDate = binding.etDate.text.toString()
        etEmail = binding.etEmail.text.toString()
        tvEmail = binding.tvEmail
        tvFace.loadImage(binding.tvFaceUrl.text.toString())

        viewModel.checkFirstName(etFirstname)
        viewModel.checkLastName(etLastname)
        viewModel.checkPhone(etPhone)
        viewModel.checkDate(etDate)
        etEmail?.let { viewModel.validateEmail(it) }
        viewModel.isCandidateReadyToSave()
    }

    private fun candidateSave() {
        viewModel.modifyCandidate(
            candidateId = candidateId,
            detailId = detailId,

            firstName = etFirstname,
            lastName = etLastname,
            phone = etPhone,
            email = etEmail,
            date = etDate,

            photoUri = currentUri,
            note = binding.etNote.text.toString(),
            salaryClaim = binding.etSalaryClaim.text.toString()
        )
    }
}