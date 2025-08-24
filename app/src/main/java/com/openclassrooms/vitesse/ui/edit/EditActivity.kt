package com.openclassrooms.vitesse.ui.edit

import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
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
    private lateinit var etFirstname: TextInputEditText
    private lateinit var etLastName: TextInputEditText
    private lateinit var etPhone: TextInputEditText
    private lateinit var tvEmail: TextInputLayout
    private lateinit var etEmail: TextInputEditText
    private lateinit var etDate: TextInputEditText
    private lateinit var etSalaryClaim: TextInputEditText
    private lateinit var etNote: TextInputEditText
    private var currentUri: String? = null
    private var currentFirstname: String? = null
    private var currentLastname: String? = null
    private var currentPhone: String? = null
    private var currentEmail: String? = null
    private var currentDate: String? = null
    private var currentSalaryClaim: String? = null
    private var currentNote: String? = null

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
        etFirstname = binding.etFirstname
        etLastName = binding.etLastname
        etPhone = binding.etPhone
        tvEmail = binding.tvEmail
        etEmail = binding.etEmail
        etDate = binding.etDate
        etSalaryClaim = binding.etSalaryClaim
        etNote = binding.etNote
        mediaPickerHelper = MediaPickerHelper(this, tvFace) { uri -> currentUri = uri.toString() }
        mediaPickerHelper.setup()

        binding.saveButton.setOnClickListener { setVerify() }
        binding.etDate.setDateUi(this@EditActivity)
        setToolbar()
    }

    private fun setCandidate(candidate: CandidateDetail) {
        candidateId = candidate.candidateId!!
        detailId = candidate.detailId!!

        if (currentUri == null) {
            currentUri = candidate.photoUri
            tvFace.loadImage(candidate.photoUri)
        } else if (currentUri != candidate.photoUri) {
            tvFace.loadImage(currentUri)
        } else {
            tvFace.loadImage(candidate.photoUri)
        }

        if (currentFirstname != candidate.firstName)
            etFirstname.setText(candidate.firstName)

        if (currentLastname != candidate.lastName)
            etLastName.setText(candidate.lastName)

        if (currentPhone != candidate.phone)
            etPhone.setText(candidate.phone)

        if (currentEmail != candidate.email)
            etEmail.setText(candidate.email)

        if (currentDate != candidate.date?.toLocalDateString())
            etDate.setText(candidate.date?.toLocalDateString())

        if (currentSalaryClaim != candidate.salaryClaim)
            etSalaryClaim.setText(candidate.salaryClaim)

        if (currentNote != candidate.note)
            etNote.setText(candidate.note)

        currentFirstname = candidate.firstName
        currentLastname = candidate.lastName
        currentPhone = candidate.phone
        currentEmail = candidate.email
        currentDate = candidate.date?.toLocalDateString()
        currentEmail = candidate.email
        currentSalaryClaim = candidate.salaryClaim
        currentNote = candidate.note
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
        tvEmail.error = when (state) {
            EmailState.MandatoryField -> getString(R.string.mandatory_field)
            EmailState.InvalidFormat -> getString(R.string.invalide_format)
            EmailState.Valid -> null
        }
    }

    private fun setVerify() {
        viewModel.checkFirstName(etFirstname.text.toString())
        viewModel.checkLastName(etLastName.text.toString())
        viewModel.checkPhone(etPhone.text.toString())
        viewModel.checkDate(etDate.text.toString())
        viewModel.validateEmail(etEmail.text.toString())
        viewModel.isCandidateReadyToSave()
    }

    private fun candidateSave() {
        viewModel.modifyCandidate(
            candidateId = candidateId,
            detailId = detailId,

            firstName = etFirstname.text.toString(),
            lastName = etLastName.text.toString(),
            phone = etPhone.text.toString(),
            email = etEmail.text.toString(),
            date = etDate.text.toString(),

            photoUri = currentUri,
            note = etNote.text.toString(),
            salaryClaim = etSalaryClaim.text.toString()
        )
    }
}