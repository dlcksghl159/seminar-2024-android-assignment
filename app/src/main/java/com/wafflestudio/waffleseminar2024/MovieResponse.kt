package com.example.assignment0

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("movies")  // 필요한 경우 여기도 매핑해줍니다. 하지만 JSON 구조에 따라 생략 가능
    val movies: List<Movie>
)
