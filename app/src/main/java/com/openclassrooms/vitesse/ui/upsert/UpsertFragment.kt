package com.openclassrooms.vitesse.ui.upsert

import android.content.res.Configuration
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import com.openclassrooms.vitesse.R
import com.openclassrooms.vitesse.databinding.FragmentUpsertBinding
import com.openclassrooms.vitesse.domain.model.Candidate
import dagger.hilt.android.AndroidEntryPoint
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController


/**
 * Interface used to allow deletion of an [Candidate] from the UI.
 */
interface DeleteCandidateInterface {
    /**
     * Callback to request deletion of an candidate.
     *
     * @param candidate The candidate to delete.
     */
    fun deleteCandidate(candidate: Candidate?)
}

@AndroidEntryPoint
class UpsertFragment : Fragment() {

    private var _binding: FragmentUpsertBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UpsertViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setMenu(view)
        setComMenu()
        themeControl()
    }

    private fun themeControl() {
        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> {
                Toast.makeText(requireContext(), "UI_MODE_NIGHT_YES", Toast.LENGTH_SHORT).show()
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                Toast.makeText(requireContext(), "UI_MODE_NIGHT_NO", Toast.LENGTH_SHORT).show()
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                Toast.makeText(requireContext(), "UI_MODE_NIGHT_UNDEFINED", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setComMenu() {
        val btnCall = binding.btnCall
        val btnSms = binding.btnSms
        val btnEmail = binding.btnEmail
        btnCall.setOnClickListener {
            Toast.makeText(requireContext(), "CAll", Toast.LENGTH_SHORT).show()
        }
        btnSms.setOnClickListener {
            Toast.makeText(requireContext(), "SMS", Toast.LENGTH_SHORT).show()
        }
        btnEmail.setOnClickListener {
            Toast.makeText(requireContext(), "EMAIL", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setMenu(view: View) {

        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.toolbar_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.fab_favorite -> {
                        Toast.makeText(requireContext(), "Favori", Toast.LENGTH_SHORT).show()
                        true
                    }

                    R.id.fab_edit -> {
                        Toast.makeText(requireContext(), "Éditer", Toast.LENGTH_SHORT).show()
                        true
                    }

                    R.id.fab_delete -> {
                        Toast.makeText(requireContext(), "Supprimer", Toast.LENGTH_SHORT).show()
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpsertBinding.inflate(inflater, container, false)
        return binding.root
    }

}