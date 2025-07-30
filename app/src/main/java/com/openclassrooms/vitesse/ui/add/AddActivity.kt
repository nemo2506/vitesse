package com.openclassrooms.vitesse.ui.add

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputLayout
import com.openclassrooms.vitesse.R
import com.openclassrooms.vitesse.databinding.ActivityAddBinding
import com.openclassrooms.vitesse.utils.MediaPickerHelper
import com.openclassrooms.vitesse.utils.navigateToCandidateScreen
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
    private var etFirstname: String? = null
    private var etLastname: String? = null
    private var etPhone: String? = null
    private var etEmail: String? = null
    private lateinit var tvEmail: TextInputLayout
    private var etDate: String? = null

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
                uiState.isFirstNameCheck?.let { setInfoErrorNotify(binding.tvFirstname, it) }
                uiState.isLastNameCheck?.let { setInfoErrorNotify(binding.tvLastname, it) }
                uiState.isPhoneCheck?.let { setInfoErrorNotify(binding.tvPhone, it) }
                uiState.isDateCheck?.let { setInfoErrorNotify(binding.tvDate, it) }
                setEmailNotify(uiState.isValidEmail)
                if( uiState.isCandidateFull == true ) candidateSave()
                uiState.isUpdated?.let { this@AddActivity.navigateToCandidateScreen() }
            }
        }
    }

    private fun setGlobalUi() {
        toolbar = binding.toolbar
        toolbar.title = getString(R.string.add_candidate)
        tvFace = binding.tvFace
        mediaPickerHelper = MediaPickerHelper(this@AddActivity, tvFace) { uri -> currentUri = uri }
        mediaPickerHelper.setup()
        binding.saveButton.setOnClickListener { setValAndVerify() }
        binding.etDate.setDateUi(this@AddActivity)
        setToolbar()
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

    private fun setValAndVerify() {
        etFirstname = binding.etFirstname.text.toString()
        etLastname = binding.etLastname.text.toString()
        etPhone = binding.etPhone.text.toString()
        etDate = binding.etDate.text.toString()
        etEmail = binding.etEmail.text.toString()
        tvEmail = binding.tvEmail
        viewModel.checkFirstName(etFirstname)
        viewModel.checkLastName(etLastname)
        viewModel.checkPhone(etPhone)
        viewModel.checkDate(etDate)
        etEmail?.let { viewModel.validateEmail(it) }
        viewModel.isCandidateReadyToSave()
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

    private fun candidateSave() {
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