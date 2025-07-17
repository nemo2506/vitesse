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

class CandidateAdapter() :
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
        holder.tvFirstName.text = String.format("%s", candidate.firstName)
        holder.tvLastName.text = String.format("%s", candidate.lastName)
        holder.tvNote.text = String.format("%s", candidate.note)
    }

    inner class CandidateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvFace: ImageView = itemView.findViewById(R.id.tv_face)
        var tvFirstName: TextView = itemView.findViewById(R.id.tv_firstname)
        var tvLastName: TextView = itemView.findViewById(R.id.tv_lastname)
        var tvNote: TextView = itemView.findViewById(R.id.tv_note)
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Candidate> =
            object : DiffUtil.ItemCallback<Candidate>() {
                override fun areItemsTheSame(oldItem: Candidate, newItem: Candidate): Boolean {
                    return oldItem === newItem
                }

                override fun areContentsTheSame(oldItem: Candidate, newItem: Candidate): Boolean {
                    return oldItem == newItem
                }
            }
    }
}