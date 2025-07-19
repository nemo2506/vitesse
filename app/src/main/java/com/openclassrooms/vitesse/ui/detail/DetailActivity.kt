package com.openclassrooms.vitesse.ui.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()
        setupMenu()
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
        val id = candidate.id
        val firstName = candidate.firstName
        val lastName = candidate.lastName
        binding.toolbar.title = "$firstName $lastName"

        // Modifier un autre texte, par exemple la date de naissance
        val tvBirth = findViewById<TextView>(R.id.tv_birth)
        val tvSalary = findViewById<TextView>(R.id.tv_salary)
        val tvSalaryGbp = findViewById<TextView>(R.id.tv_salary_gbp)
        val tvNotes = findViewById<TextView>(R.id.tv_notes)

        val btnCall = findViewById<ImageButton>(R.id.btn_call)
        val btnSms = findViewById<ImageButton>(R.id.btn_sms)
        val btnEmail = findViewById<ImageButton>(R.id.btn_email)
//        btnCall.setOnClickListener(setCall(phone))
//        btnSms.setOnClickListener(setSms(phone))
//        btnEmail.setOnClickListener(setEmail(email))
        setFace(candidate.photoUri, findViewById<ImageView>(R.id.tv_face))
        tvBirth.text = viewModel.setBirth(candidate.date)
        tvSalary.text = viewModel.setSalary(candidate.salaryClaim)
        tvSalaryGbp.text = viewModel.setSalaryGbp(candidate.salaryClaim)
        tvNotes.text = candidate.note

    }

    private fun setFace(imageUrl: String, ivFace: ImageView) {
        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.ic_avatar)
            .error(R.drawable.ic_avatar)
            .into(ivFace)
    }

    //    private fun setSms(phone: String): View.OnClickListener? {
//        Log.d("MARC", "setSms: INIT")
//    }
//
//    private fun setCall(phone: String): View.OnClickListener? {
//        Log.d("MARC", "setCall: INIT")
//    }
//
//    private fun setEmail(email: String): View.OnClickListener? {
//
//    }
    private fun setupToolbar() {
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.title = title
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupMenu() {
        val menuHost: MenuHost = this
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.toolbar_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.fab_favorite -> {
                        Toast.makeText(this@DetailActivity, "Favori", Toast.LENGTH_SHORT).show()
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
