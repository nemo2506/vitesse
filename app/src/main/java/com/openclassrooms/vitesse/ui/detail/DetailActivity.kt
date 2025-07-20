package com.openclassrooms.vitesse.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
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
import com.openclassrooms.vitesse.databinding.ActivityDetailBinding
import com.openclassrooms.vitesse.domain.model.CandidateDetail
import com.openclassrooms.vitesse.ui.candidate.CandidateActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels()
    private lateinit var candidate: CandidateDetail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbar()
        setMenu()
        setupComMenu()
        observeDetail()
    }

    private fun observeDetail() {
        lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                uiState.isLoading?.let { toLoaderUi(it) }
                uiState.candidate?.let { setUpUI(it) }
                uiState.message?.let {
                    Log.d("MARC", "observeDetail: $it")
                    toMessageUi(it)
                }
                if (uiState.isDeleted == true) toCandidateScreen()
            }
        }
    }

    private fun toLoaderUi(loading: Boolean) {
        binding.loading.visibility = if (loading) View.VISIBLE else View.GONE
    }

    private fun toCandidateScreen() {
        val intent = Intent(this, CandidateActivity::class.java)
        startActivity(intent)
    }

    private fun setUpUI(candidate: CandidateDetail) {
        this@DetailActivity.candidate = candidate
        val title = "%s %s".format(candidate.firstName, candidate.lastName)
        binding.toolbar.title = title
        setFavoriteUi(candidate.isFavorite)
        setFab(candidate, title)
        setFace(candidate.photoUri, binding.tvFace)
        binding.tvBirth.text = candidate.dateDescription
        binding.tvSalary.text = candidate.salaryClaimDescription
        binding.tvSalaryGbp.text = candidate.salaryClaimGpb
        binding.tvNotes.text = candidate.note

    }

    private fun toMessageUi(message: String) {
        Toast.makeText(this@DetailActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setFavoriteUi(fav: Boolean) {
        val icon = if (fav) R.drawable.ic_star_active else R.drawable.ic_star
        binding.toolbar.post {
            binding.toolbar.menu.findItem(R.id.fab_favorite)?.icon =
                ContextCompat.getDrawable(this, icon)
        }
    }

    private fun setFab(candidate: CandidateDetail, title: String) {
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
                        candidate.candidateId?.let {
                            viewModel.updateFavorite(
                                it,
                                candidate.isFavorite
                            )
                        }
                        true
                    }

                    R.id.fab_edit -> {
                        Toast.makeText(
                            this@DetailActivity,
                            "MODIFIER EN COURS ...",
                            Toast.LENGTH_SHORT
                        ).show()
                        true
                    }

                    R.id.fab_delete -> {
                        try {
                            candidate.candidateId?.let { viewModel.deleteCandidate(it) }
                        } catch (e: Exception) {
                            Log.d("MARC", "deleteCandidate: $e")
                        }
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
