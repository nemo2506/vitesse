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
import com.openclassrooms.vitesse.domain.model.Candidate

class CandidateAdapter(
    private val toDetailScreen: (Candidate) -> Unit
) :
    ListAdapter<Candidate, CandidateAdapter.CandidateViewHolder>(
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
        holder.tvFirstName.text = candidate.firstName
        holder.tvLastName.text = candidate.lastName
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
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Candidate> =
            object : DiffUtil.ItemCallback<Candidate>() {
                override fun areItemsTheSame(
                    oldItem: Candidate,
                    newItem: Candidate
                ): Boolean {
                    // Comparez les IDs uniques des candidats
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: Candidate,
                    newItem: Candidate
                ): Boolean {
                    // Comparez les contenus complets (data class a un equals correctement défini)
                    return oldItem == newItem
                }
            }
    }
}