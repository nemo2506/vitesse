package com.openclassrooms.vitesse.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.Locale
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
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import java.util.Calendar
import android.app.DatePickerDialog
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.TextView
import androidx.activity.ComponentActivity
import java.text.NumberFormat
import android.Manifest
import android.util.Log

fun Long.toFrDescription(): String? {
    if (this == 0L) return null
    val formatter = NumberFormat.getCurrencyInstance(Locale.FRANCE).apply {
        minimumFractionDigits = 0
        maximumFractionDigits = 0
    }
    return formatter.format(this)
}

fun Long.toGbpDescription(currency: Double): String? {
    if (this == 0L) return null
    val converted = this * currency
    val formatter = NumberFormat.getCurrencyInstance(Locale.UK)
    return formatter.format(converted)
}

fun LocalDateTime.calculateAge(): Int {
    val birthLocalDate: LocalDate = this.toLocalDate()
    val today = LocalDate.now()
    return Period.between(birthLocalDate, today).years
}

fun LocalDateTime?.toDateDescription(): String? {
    if (this == null) return null
    val localeLang = Locale.getDefault().language
    val formatter: DateTimeFormatter
    val message: String
    if (localeLang == "fr") {
        formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        message = "%s (%d ans)"
    } else {
        formatter = DateTimeFormatter.ofPattern("MM/d/yyyy")
        message = "%s (%d years old)"
    }
    val age = this.calculateAge()
    val date = this.format(formatter)
    return message.format(date, age)
}

fun String.toDate(): LocalDateTime? {
    val localeLang = Locale.getDefault().language
    val formatter = if (localeLang == "fr") {
        DateTimeFormatter.ofPattern("dd/MM/yyyy")
    } else {
        DateTimeFormatter.ofPattern("MM/d/yyyy")
    }
    return try {
        LocalDate.parse(this, formatter).atStartOfDay()
    } catch (e: Exception) {
        null
    }
}

fun Long?.toEmpty(): String {
    if (this == null) return ""
    return this.toString()
}

fun String?.toZeroOrLong(): Long {
    if (this.isNullOrBlank()) return 0L
    return this.toLong()
}

fun String.showToastMessage(context: Context) {
    Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
}

fun ImageView.loadImage(url: String?) {
    Glide.with(this.context)
        .load(url)
        .placeholder(R.drawable.ic_avatar)
        .error(R.drawable.ic_edit)
        .into(this)
}

fun Context.navigateToDetailScreen(candidateId: Long) {
    val intent = Intent(this, DetailActivity::class.java).apply {
        putExtra(ConstantsApp.CANDIDATE_ID, candidateId)
    }
    this.startActivity(intent)
}

fun Context.navigateToAddScreen() {
    val intent = Intent(this, AddActivity::class.java)
    this.startActivity(intent)
}

fun Context.navigateToCandidateScreen() {
    val intent = Intent(this, CandidateActivity::class.java)
    this.startActivity(intent)
}

fun Context.navigateToEditScreen(candidateId: Long, detailId: Long) {
    val intent = Intent(this, EditActivity::class.java).apply {
        putExtra(ConstantsApp.CANDIDATE_ID, candidateId)
        putExtra(ConstantsApp.DETAIL_ID, detailId)
    }
    this.startActivity(intent)
}

fun View.setVisible(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}

fun LocalDateTime.toLocalDateString(): String {
    val localeLang = Locale.getDefault().language
    val formatter = if (localeLang == "fr") {
        DateTimeFormatter.ofPattern("dd/MM/yyyy")
    } else {
        DateTimeFormatter.ofPattern("MM/d/yyyy")
    }
    return this.format(formatter)
}

fun TextView.setDateUi(context: Context) {
    this.setOnClickListener {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePicker = DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
            val localeLang = context.resources.configuration.locales.get(0).language
            Log.d("MARC", "setDateUi: localeLang/$localeLang")
            val formattedDate = if (localeLang == "fr") {
                "%02d/%02d/%04d".format(selectedDay, selectedMonth + 1, selectedYear)
            } else {
                "%02d/%d/%04d".format(selectedMonth + 1, selectedDay, selectedYear)
            }
            this.text = formattedDate
        }, year, month, day)
        datePicker.show()
    }
}

fun String.capitalizeFirstLetter(): String {
    return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}

fun Long?.isPositive(): Boolean = this != null && this > 0


fun String.isValidEmail(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

class MediaPickerHelper(
    private val activity: ComponentActivity,
    private val tvFace: ImageView,
    private val onImagePicked: ((Uri) -> Unit)? = null
) {
    private var currentPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    private val requestPermissionLauncher =
        activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                pickMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            } else {
                activity.getString(R.string.permission_denied).showToastMessage(activity)
            }
        }

    private val pickMediaLauncher =
        activity.registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                onImagePicked?.invoke(uri)
                tvFace.loadImage(uri.toString())
            } else {
                activity.getString(R.string.no_image_selected).showToastMessage(activity)
            }
        }

    fun setup(context: Context) {
        currentPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        tvFace.setOnClickListener {
            when {
                ContextCompat.checkSelfPermission(
                    activity,
                    currentPermission
                ) == PackageManager.PERMISSION_GRANTED -> {
                    pickMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }

                activity.shouldShowRequestPermissionRationale(currentPermission) -> {
                    context.getString(R.string.authorization_required).showToastMessage(activity)
                    requestPermissionLauncher.launch(currentPermission)
                }

                else -> {
                    requestPermissionLauncher.launch(currentPermission)
                }
            }
        }
    }
}