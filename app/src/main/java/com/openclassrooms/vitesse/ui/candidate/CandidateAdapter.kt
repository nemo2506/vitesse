package com.openclassrooms.vitesse.ui.candidate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.vitesse.R
import com.openclassrooms.vitesse.domain.model.CandidateSummary
import com.openclassrooms.vitesse.ui.utils.capitalizeFirstLetter

class CandidateAdapter(
    private val toDetailScreen: (CandidateSummary) -> Unit
) :
    ListAdapter<CandidateSummary, CandidateAdapter.CandidateViewHolder>(
        DIFF_CALLBACK
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CandidateViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_candidate, parent, false)
        return CandidateViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CandidateViewHolder, position: Int) {
        val candidate = getItem(position)

        Glide.with(holder.itemView.context)
            .load(candidate.photoUri)
            .into(holder.tvFace)
        holder.tvFirstName.text = candidate.firstName?.let { String.format("%s", it.capitalizeFirstLetter()) }
        holder.tvLastName.text = String.format("%s", candidate.lastName.uppercase())
        holder.tvNote.text = String.format("%s", candidate.note)
        holder.itemView.setOnClickListener {
            toDetailScreen(candidate)
        }
    }

    inner class CandidateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvFace: ImageView = itemView.findViewById(R.id.tv_face)
        val tvFirstName: TextView = itemView.findViewById(R.id.tv_firstname)
        val tvLastName: TextView = itemView.findViewById(R.id.tv_lastname)
        val tvNote: TextView = itemView.findViewById(R.id.tv_note)
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<CandidateSummary> =
            object : DiffUtil.ItemCallback<CandidateSummary>() {
                override fun areItemsTheSame(
                    oldItem: CandidateSummary,
                    newItem: CandidateSummary
                ): Boolean {
                    // Comparez les IDs uniques des candidats
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: CandidateSummary,
                    newItem: CandidateSummary
                ): Boolean {
                    // Comparez les contenus complets (data class a un equals correctement défini)
                    return oldItem == newItem
                }
            }
    }
}