package com.openclassrooms.vitesse.ui.add

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.openclassrooms.vitesse.R
import com.openclassrooms.vitesse.databinding.ActivityAddBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar

@AndroidEntryPoint
class AddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBinding
    private val viewModel: AddViewModel by viewModels()

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
                uiState.isLoading?.let { toLoaderUi(it) }
                uiState.message?.let { toMessageUi(it) }
            }
        }
    }

    private fun setUi() {
        binding.toolbar.title = "Ajouter un candidat"
        binding.saveButton.setOnClickListener {
            setSave()
        }
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

    private fun setSave() {
        val tvFace: ImageView = binding.tvFace

        val tvFaceUrl = binding.tvFaceUrl.text.toString()
        if (tvFaceUrl.isNotBlank()) {
            Glide.with(this)
                .load(tvFaceUrl)
                .placeholder(R.drawable.ic_avatar)
//                .error(R.drawable.error)
                .into(binding.tvFace)
        } else {
            tvFace.setImageResource(R.drawable.ic_edit)
        }

        viewModel.addCandidate(
            firstName = binding.etFirstname.text.toString(),
            lastName = binding.etLastname.text.toString(),
            phone = binding.etPhone.text.toString(),
            email = binding.etEmail.text.toString(),
            photoUri = binding.tvFaceUrl.text.toString(),
            note = binding.etNote.text.toString(),
            date = null, // binding.etDate.text.toString()
            salaryClaim = binding.etSalaryClaim.text.toString().toLongOrNull()
        )
    }

    private fun toLoaderUi(loading: Boolean) {
        binding.loading.visibility = if (loading) View.VISIBLE else View.GONE
    }


    private fun toMessageUi(message: String) {
        Toast.makeText(this@AddActivity, message, Toast.LENGTH_SHORT).show()
    }
}