package com.openclassrooms.vitesse.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.openclassrooms.vitesse.R
import com.openclassrooms.vitesse.databinding.ActivityDetailBinding
import com.openclassrooms.vitesse.domain.model.CandidateDescription
import com.openclassrooms.vitesse.utils.loadImage
import com.openclassrooms.vitesse.utils.navigateToCandidateScreen
import com.openclassrooms.vitesse.utils.navigateToEditScreen
import com.openclassrooms.vitesse.utils.setVisible
import com.openclassrooms.vitesse.utils.showToastMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels()
    private lateinit var candidate: CandidateDescription
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private var candidateId: Long = 0
    private var detailId: Long = 0

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
                uiState.message?.showToastMessage(this@DetailActivity)
                if (uiState.isDeleted == true) this@DetailActivity.navigateToCandidateScreen()
            }
        }
    }

    private fun setUpUI(candidate: CandidateDescription) {
        this@DetailActivity.candidate = candidate
        if (candidate.candidateId != null) candidateId = candidate.candidateId
        if (candidate.detailId != null) detailId = candidate.detailId
        val title = "%s %s".format(candidate.firstName, candidate.lastName)
        toolbar.title = title
        setFavoriteUi(candidate.isFavorite)
        setCom(candidate, title)
        candidate.photoUri?.let { binding.tvFace.loadImage(it) }
        binding.tvBirth.text = candidate.dateDescription
        binding.tvSalary.text = candidate.salaryClaimDescription
        binding.tvSalaryGbp.text = getString(R.string.either).format(candidate.salaryClaimGpb)
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
            putExtra("sms_body", getString(R.string.good_morning).format(title))
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
        "EMAIL".showToastMessage(this@DetailActivity)
        val subject = "VITESSE"
        val body = getString(R.string.email_message).format(title)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(address))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            getString(R.string.messaging_missing).showToastMessage(this@DetailActivity)
        }
    }

    private fun setToolbar() {
        toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            this@DetailActivity.navigateToCandidateScreen()
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
                        AlertDialog.Builder(this@DetailActivity).apply {
                            setTitle(getString(R.string.deletion))
                            setMessage(getString(R.string.confirm_delete))
                            setPositiveButton(getString(R.string.confirm)) { dialog, which ->
                                this@DetailActivity.navigateToEditScreen(candidateId, detailId)
                                dialog.dismiss()
                            }
                            setNegativeButton("Annuler") { dialog, which ->
                                dialog.dismiss()
                            }
                            create()
                            show()
                        }
                        true
                    }

                    R.id.fab_delete -> {
                        candidate.candidateId?.let { viewModel.deleteCandidate(it) }
                        true
                    }

                    else -> false
                }
            }
        }, this, Lifecycle.State.RESUMED)
    }
}
