package com.openclassrooms.vitesse.ui.add

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.openclassrooms.vitesse.R
import com.openclassrooms.vitesse.databinding.ActivityAddBinding
import com.openclassrooms.vitesse.ui.utils.loadImage
import com.openclassrooms.vitesse.ui.utils.navigateToCandidateScreen
import com.openclassrooms.vitesse.ui.utils.navigateToDetailScreen
import com.openclassrooms.vitesse.ui.utils.setVisible
import com.openclassrooms.vitesse.ui.utils.showToastMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar

@AndroidEntryPoint
class AddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBinding
    private val viewModel: AddViewModel by viewModels()
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolbar = binding.toolbar
        setUi()
        setDateUi()
        setToolbar()
        observeAdd()
    }

    private fun observeAdd() {
        lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                uiState.isLoading?.let { binding.loading.setVisible(it) }
                uiState.message?.let { showToastMessage(this@AddActivity ,it) }
                uiState.candidateId?.let { navigateToDetailScreen(this@AddActivity, it) }
            }
        }
    }

    private fun setUi() {
        toolbar.title = "Ajouter un candidat"
        binding.saveButton.setOnClickListener {
            setSave()
        }
    }

    private fun setDateUi(){
        val etDate = binding.etDate
        etDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val datePicker = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate =
                    "%02d/%02d/%04d".format(selectedDay, selectedMonth + 1, selectedYear)
                etDate.setText(formattedDate)
            }, year, month, day)
            datePicker.show()
        }
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

        val tvFace: ImageView = binding.tvFace
        val tvFaceUrl = binding.tvFaceUrl.text.toString()
        if (tvFaceUrl.isNotBlank()) {
            tvFace.loadImage(tvFaceUrl)
        } else {
            tvFace.setImageResource(R.drawable.ic_edit)
        }

        val birthDate = viewModel.getLocalDateTime(binding.etDate.text.toString())

        viewModel.addCandidate(
            firstName = binding.etFirstname.text.toString(),
            lastName = binding.etLastname.text.toString(),
            phone = binding.etPhone.text.toString(),
            email = binding.etEmail.text.toString(),
            photoUri = binding.tvFaceUrl.text.toString(),
            note = binding.etNote.text.toString(),
            date = birthDate,
            salaryClaim = binding.etSalaryClaim.text.toString().toLongOrNull()
        )
    }
}