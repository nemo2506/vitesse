package com.openclassrooms.vitesse.ui.upsert

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import com.openclassrooms.vitesse.R
import com.openclassrooms.vitesse.databinding.ActivityUpsertBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpsertActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpsertBinding
    private val viewModel: UpsertViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpsertBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()
        setupMenu()
        setupComMenu()
    }

    private fun setupToolbar() {
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

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
                        Toast.makeText(this@UpsertActivity, "Favori", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.fab_edit -> {
                        Toast.makeText(this@UpsertActivity, "Éditer", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.fab_delete -> {
                        Toast.makeText(this@UpsertActivity, "Supprimer", Toast.LENGTH_SHORT).show()
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
