package com.openclassrooms.vitesse.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationBarView
import com.openclassrooms.vitesse.R
import com.openclassrooms.vitesse.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var selectedMenuItemId: Int = R.id.nav_user_data

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.bottomNavigation.setOnItemSelectedListener(navListener)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, UserDataFragment()).commit()
        } else {
            savedInstanceState.getInt(SELECTED_MENU_ITEM, R.id.nav_user_data)
                .also { selectedMenuItemId = it }
            binding.bottomNavigation.selectedItemId = selectedMenuItemId
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SELECTED_MENU_ITEM, selectedMenuItemId)
    }

    private val navListener = NavigationBarView.OnItemSelectedListener { item ->
        getFragmentById(item.itemId)?.let { selectedFragment ->
            selectedMenuItemId = item.itemId
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, selectedFragment).commit()
            true
        } ?: false
    }

    private fun getFragmentById(menuId: Int): Fragment? {
        return when (menuId) {
            R.id.nav_user_data -> UserDataFragment()
            else -> null
        }
    }

    companion object {
        private const val SELECTED_MENU_ITEM = "selected_menu_item"
    }
}