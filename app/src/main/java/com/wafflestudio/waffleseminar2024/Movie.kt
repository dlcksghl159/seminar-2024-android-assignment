package com.example.assignment0

import com.google.gson.annotations.SerializedName

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,

    @SerializedName("genre_ids")  // JSON의 "genre_ids" 필드를 코틀린의 genreIds로 매핑
    val genreIds: List<Int>?,

    @SerializedName("poster_path")  // JSON의 "poster_path" 필드를 코틀린의 posterPath로 매핑
    val posterPath: String,

    @SerializedName("backdrop_path")  // JSON의 "backdrop_path" 필드를 코틀린의 backdropPath로 매핑
    val backdropPath: String
)
