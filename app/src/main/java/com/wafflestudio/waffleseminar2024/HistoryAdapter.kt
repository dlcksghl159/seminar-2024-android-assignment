package com.wafflestudio.waffleseminar2024

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HistoryAdapter(
    private val historyList: List<String>,
    private val itemClickListener: (String) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val historyItemText: TextView = itemView.findViewById(R.id.history_item_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.history_item, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.historyItemText.text = historyList[position]
        holder.itemView.setOnClickListener {
            itemClickListener(historyList[position])
        }
    }

    override fun getItemCount(): Int = historyList.size
}
