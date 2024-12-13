package com.example.glusity.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.glusity.R
import com.example.glusity.model.ObesityResult

class HistoryAdapter : ListAdapter<ObesityResult, HistoryAdapter.HistoryViewHolder>(ObesityResultDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_obesity_result, parent, false)
        return HistoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val obesityResult = getItem(position)
        holder.bind(obesityResult)
    }

    inner class HistoryViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        fun bind(obesityResult: ObesityResult) {
            itemView.findViewById<android.widget.TextView>(R.id.categoryText).text = obesityResult.category
            itemView.findViewById<android.widget.TextView>(R.id.recommendationText).text = obesityResult.recommendation
        }
    }
}

class ObesityResultDiffCallback : androidx.recyclerview.widget.DiffUtil.ItemCallback<ObesityResult>() {
    override fun areItemsTheSame(oldItem: ObesityResult, newItem: ObesityResult): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ObesityResult, newItem: ObesityResult): Boolean {
        return oldItem == newItem
    }
}