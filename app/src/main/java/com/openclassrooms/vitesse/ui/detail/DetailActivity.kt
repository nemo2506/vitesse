package com.openclassrooms.vitesse.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.openclassrooms.vitesse.R
import com.openclassrooms.vitesse.databinding.ActivityDetailBinding
import com.openclassrooms.vitesse.domain.model.CandidateDescription
import com.openclassrooms.vitesse.ui.utils.loadImage
import com.openclassrooms.vitesse.ui.utils.navigateToCandidateScreen
import com.openclassrooms.vitesse.ui.utils.navigateToEditScreen
import com.openclassrooms.vitesse.ui.utils.setVisible
import com.openclassrooms.vitesse.ui.utils.showToastMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels()
    private lateinit var candidate: CandidateDescription
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbar()
        setMenu()
        observeDetail()
    }

    private fun observeDetail() {
        lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                uiState.isLoading?.let { binding.loading.setVisible(it) }
                uiState.candidate?.let { setUpUI(it) }
                uiState.message?.let {
                    showToastMessage(this@DetailActivity, it)
                }
                if (uiState.isDeleted == true) navigateToCandidateScreen(this@DetailActivity)
            }
        }
    }

    private fun setUpUI(candidate: CandidateDescription) {
        this@DetailActivity.candidate = candidate
        val title = "%s %s".format(candidate.firstName, candidate.lastName)
        toolbar.title = title
        setFavoriteUi(candidate.isFavorite)
        setCom(candidate, title)
        candidate.photoUri?.let { binding.tvFace.loadImage(it) }
        binding.tvBirth.text = candidate.dateDescription
        binding.tvSalary.text = candidate.salaryClaimDescription
        binding.tvSalaryGbp.text = candidate.salaryClaimGpb
        binding.tvNotes.text = candidate.note

    }

    private fun setFavoriteUi(fav: Boolean) {
        val icon = if (fav) R.drawable.ic_star_active else R.drawable.ic_star
        toolbar.post {
            toolbar.menu.findItem(R.id.fab_favorite)?.icon =
                ContextCompat.getDrawable(this, icon)
        }
    }

    private fun setCom(candidate: CandidateDescription, title: String) {
        binding.btnCall.setOnClickListener {
            candidate.phone?.let { it1 -> setCall(it1) }
        }
        binding.btnSms.setOnClickListener {
            candidate.phone?.let { it1 -> setSms(it1, title) }
        }
        binding.btnEmail.setOnClickListener {
            candidate.email?.let { it1 -> setEmail(it1, title) }
        }
    }

    private fun setSms(phone: String, title: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("smsto:$phone")
            putExtra("sms_body", "Bonjour , $title")
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
        showToastMessage(this@DetailActivity, "EMAIL")
        Log.d("MARC", "setEmail: $address/$title")
        val subject = "VITESSE"
        val body = "Bonjour $title,\nVoici un message pré-rempli."

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(address))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            showToastMessage(this@DetailActivity, "MESSAGERIE ABSENTE")
        }
    }

    private fun setToolbar() {
        toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            navigateToCandidateScreen(this)
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
                        viewModel.candidateId?.let { navigateToEditScreen(this@DetailActivity, it) }
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
}
