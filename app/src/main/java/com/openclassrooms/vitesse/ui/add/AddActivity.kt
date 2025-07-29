package com.openclassrooms.vitesse.ui.add

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
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
                if (uiState.isValidInfo == false ) setInfoErrorNotify()
                if (uiState.isValidEmail == false ) setEmailErrorNotify()
                if (uiState.isValidInfo == true && uiState.isValidEmail == true) setSave()
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
        binding.saveButton.setOnClickListener { setVerify() }
        binding.etDate.setDateUi(this@AddActivity)
        setToolbar()
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            this@AddActivity.navigateToCandidateScreen()
        }
    }

    private fun setVerify() {
        etFirstname = binding.etFirstname.text.toString()
        etLastname = binding.etLastname.text.toString()
        etPhone = binding.etPhone.text.toString()
        etDate = binding.etDate.text.toString()
        etEmail = binding.etEmail.text.toString()
        tvEmail = binding.tvEmail
 //        val tvFaceUrl = binding.tvFaceUrl.text.toString()
//        tvFace.loadImage(tvFaceUrl)
        val etFields: List<String> = listOf(
            etFirstname,
            etLastname,
            etPhone,
            etEmail,
            etDate
        ).map { it ?: "" }
        viewModel.validateInfo(etFields)
        etEmail?.let { viewModel.validateEmail(it) }
    }

    private fun setInfoErrorNotify(){
//        "First Name, Last Name, Phone, E-mail, Birth Date is required"
        getString(R.string.mandatory_field).showToastMessage(this@AddActivity)
    }

    private fun setEmailErrorNotify(error: Boolean = true) {
        if (error) {
            tvEmail.error = getString(R.string.invalide_format)
        } else {
            tvEmail.error = null
        }
    }

    private fun setSave() {
        setEmailErrorNotify(false)
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