package com.openclassrooms.vitesse.ui.candidate

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
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
//        holder.tvFace.text = "Duration: ${candidate.duration}"
        holder.tvFirstName.text = String.format("%s", candidate.firstName.toString())
        holder.tvLastName.text = String.format("%s", candidate.lastName)
    }

    inner class CandidateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvFace: TextView
        var tvFirstName: TextView
        var tvLastName: TextView
        var tvNote: TextView

        init {
            tvFace = itemView.findViewById(R.id.tv_face)
            tvFirstName = itemView.findViewById(R.id.tv_firstname)
            tvLastName = itemView.findViewById(R.id.tv_lastname)
            tvNote = itemView.findViewById(R.id.tv_note)
        }
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