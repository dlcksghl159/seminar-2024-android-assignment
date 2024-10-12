package com.wafflestudio.waffleseminar2024

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class GenreAdapter(private val genres: List<Genre>, private val onClick: (Genre) -> Unit) : RecyclerView.Adapter<GenreAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val button = LayoutInflater.from(parent.context).inflate(R.layout.item_genre, parent, false) as Button
        return ViewHolder(button)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val genre = genres[position]
        holder.button.text = genre.name
        holder.button.setCompoundDrawablesWithIntrinsicBounds( 0, 0,genre.iconResId, 0)
        holder.button.setOnClickListener { onClick(genre) }
    }

    override fun getItemCount(): Int = genres.size

    class ViewHolder(val button: Button) : RecyclerView.ViewHolder(button)
}
