package com.openclassrooms.vitesse.ui.utils

import android.content.Context
import android.widget.Toast
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.openclassrooms.vitesse.R
import android.content.Intent
import com.openclassrooms.vitesse.ui.candidate.CandidateActivity

fun navigateToCandidateScreen(context: Context) {
    val intent = Intent(context, CandidateActivity::class.java)
    context.startActivity(intent)
}

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