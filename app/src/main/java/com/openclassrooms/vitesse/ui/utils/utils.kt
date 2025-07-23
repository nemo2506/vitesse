package com.openclassrooms.vitesse.ui.utils

import android.content.Context
import android.widget.Toast
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.openclassrooms.vitesse.R
import android.content.Intent
import com.openclassrooms.vitesse.ui.ConstantsApp
import com.openclassrooms.vitesse.ui.candidate.CandidateActivity
import com.openclassrooms.vitesse.ui.detail.DetailActivity
import com.openclassrooms.vitesse.ui.edit.EditActivity
import android.net.Uri
import com.openclassrooms.vitesse.ui.add.AddActivity

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

object ImageUtils {
    fun setPicture(imageView: ImageView, uri: Uri) {
        val imageUri = Uri.parse(uri.toString())
        imageView.setImageURI(imageUri)
    }
}