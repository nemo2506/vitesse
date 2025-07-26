package com.openclassrooms.vitesse.ui.utils

import android.content.Context
import android.widget.Toast
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.openclassrooms.vitesse.R
import com.openclassrooms.vitesse.ui.ConstantsApp
import com.openclassrooms.vitesse.ui.candidate.CandidateActivity
import com.openclassrooms.vitesse.ui.detail.DetailActivity
import com.openclassrooms.vitesse.ui.edit.EditActivity
import android.content.Intent
import com.openclassrooms.vitesse.ui.add.AddActivity
import android.view.View
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import java.util.Calendar
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.DatePickerDialog
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.TextView
import androidx.activity.ComponentActivity

fun showToastMessage(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun ImageView.loadImage(url: String?) {
    Glide.with(this.context)
        .load(url)
        .placeholder(R.drawable.ic_avatar)
        .error(R.drawable.ic_edit)
        .into(this)
}

fun navigateToDetailScreen(context: Context, candidateId: Long) {
    val intent = Intent(context, DetailActivity::class.java).apply {
        putExtra(ConstantsApp.CANDIDATE_ID, candidateId)
    }
    context.startActivity(intent)
}

fun navigateToAddScreen(context: Context) {
    val intent = Intent(context, AddActivity::class.java)
    context.startActivity(intent)
}

fun navigateToCandidateScreen(context: Context) {
    val intent = Intent(context, CandidateActivity::class.java)
    context.startActivity(intent)
}

fun navigateToEditScreen(context: Context, candidateId: Long) {
    val intent = Intent(context, EditActivity::class.java).apply {
        putExtra(ConstantsApp.CANDIDATE_ID, candidateId)
    }
    context.startActivity(intent)
}

fun View.setVisible(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}

fun LocalDateTime.toLocalDateString(): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return this.format(formatter)
}

class MediaPickerHelper(
    private val activity: ComponentActivity,
    private val tvFace: ImageView,
    private val onImagePicked: ((Uri) -> Unit)? = null
) {
    private val requestPermissionLauncher =
        activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                pickMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            } else {
                showToastMessage(activity, "Permission refusée")
            }
        }

    private val pickMediaLauncher =
        activity.registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                onImagePicked?.invoke(uri)
                tvFace.loadImage(uri.toString())
            } else {
                showToastMessage(activity, "Aucune image sélectionnée")
            }
        }

    fun setup() {
        tvFace.setOnClickListener {
            when {
                ContextCompat.checkSelfPermission(
                    activity,
                    READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    pickMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }

                activity.shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE) -> {
                    showToastMessage(
                        activity,
                        "Cette permission est nécessaire pour sélectionner une image."
                    )
                    requestPermissionLauncher.launch(READ_EXTERNAL_STORAGE)
                }

                else -> {
                    requestPermissionLauncher.launch(READ_EXTERNAL_STORAGE)
                }
            }
        }
    }

}

fun setDateUi(context: Context, etDate: TextView) {
    etDate.setOnClickListener {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePicker = DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate =
                "%02d/%02d/%04d".format(selectedDay, selectedMonth + 1, selectedYear)
            etDate.setText(formattedDate)
        }, year, month, day)
        datePicker.show()
    }
}