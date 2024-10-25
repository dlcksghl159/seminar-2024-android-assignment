package com.example.assignment0

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.wafflestudio.waffleseminar2024.R

class MovieAdapter(private val movieList: List<Movie>) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val movieImage: ImageView = itemView.findViewById(R.id.movie_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movieList[position]

        // 포스터 URL 생성
        val posterUrl = "https://image.tmdb.org/t/p/w185${movie.posterPath}"

        // Coil로 이미지 로드
        holder.movieImage.load(posterUrl) {
            placeholder(R.drawable.placeholder_image)
            error(R.drawable.error_image)
        }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }
}
