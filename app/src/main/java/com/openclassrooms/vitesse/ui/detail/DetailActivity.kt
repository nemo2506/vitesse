package com.openclassrooms.vitesse.ui.detail

import android.content.Intent
import android.icu.text.CaseMap.Title
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.openclassrooms.vitesse.R
import com.openclassrooms.vitesse.data.entity.CandidateTotal
import com.openclassrooms.vitesse.databinding.ActivityDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels()
    private lateinit var current: CandidateTotal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbar()
        setMenu()
        setupComMenu()
        observeCandidate()
    }

    private fun observeCandidate() {
        lifecycleScope.launch {
            viewModel.uiState.collect {
                it.candidate?.let { it1 -> setUpUI(it1) }
            }
        }
    }

    private fun setUpUI(candidate: CandidateTotal) {
        current = candidate
        val title = "%s %s".format(candidate.firstName, candidate.lastName)
        binding.toolbar.title = title
        setFavoriteUi(candidate.isFavorite)
        setFab(candidate, title)
        setFace(candidate.photoUri, binding.tvFace)
        binding.tvBirth.text = viewModel.setBirth(candidate.date)
        binding.tvSalary.text = viewModel.setSalary(candidate.salaryClaim)
        binding.tvSalaryGbp.text = viewModel.setSalaryGbp(candidate.salaryClaim)
        binding.tvNotes.text = candidate.note
    }

    private fun setFavoriteUi(fav: Boolean) {
        val icon = if (fav) R.drawable.ic_star_active else R.drawable.ic_star
        binding.toolbar.post {
            binding.toolbar.menu.findItem(R.id.fab_favorite)?.icon =
                ContextCompat.getDrawable(this, icon)
        }
    }

    private fun setFab(candidate: CandidateTotal, title: String) {
        binding.btnCall.setOnClickListener {
            setCall(candidate.phone)
        }
        binding.btnSms.setOnClickListener {
            setSms(candidate.phone, title)
        }
        binding.btnEmail.setOnClickListener {
            setEmail(candidate.email, title)
        }
    }

    private fun setFace(imageUrl: String, ivFace: ImageView) {
        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.ic_avatar)
            .error(R.drawable.ic_avatar)
            .into(ivFace)
    }

    private fun setSms(phone: String, title: String) {
        val message = "Bonjour , $title"
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("smsto:$phone")
            putExtra("sms_body", message)
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun setCall(phone: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phone")
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }


    private fun setEmail(address: String, title: String) {
        val subject = "Sujet de l’email"
        val body = "Bonjour $title,\nVoici un message pré-rempli."

        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$address")
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun setToolbar() {
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setMenu() {
        val menuHost: MenuHost = this
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.toolbar_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.fab_favorite -> {
                        viewModel.updateFavorite(current.id, current.isFavorite)
                        true
                    }

                    R.id.fab_edit -> {
                        Toast.makeText(this@DetailActivity, "Éditer", Toast.LENGTH_SHORT).show()
                        true
                    }

                    R.id.fab_delete -> {
                        Toast.makeText(this@DetailActivity, "Supprimer", Toast.LENGTH_SHORT).show()
                        true
                    }

                    else -> false
                }
            }
        }, this, Lifecycle.State.RESUMED)
    }

    private fun setupComMenu() {
        binding.btnCall.setOnClickListener {
            Toast.makeText(this, "Call", Toast.LENGTH_SHORT).show()
        }
        binding.btnSms.setOnClickListener {
            Toast.makeText(this, "SMS", Toast.LENGTH_SHORT).show()
        }
        binding.btnEmail.setOnClickListener {
            Toast.makeText(this, "EMAIL", Toast.LENGTH_SHORT).show()
        }
    }
}
