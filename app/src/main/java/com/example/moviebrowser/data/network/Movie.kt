package com.example.moviebrowser.data.network

import com.google.gson.annotations.SerializedName

data class Movie(
    val id: Int,
    val title: String,
    val overview: String = "",

    @SerializedName("poster_path")
    val posterpath: String? = null,

    @SerializedName("vote_average")   // ⭐ Rating
    val voteAverage: Double = 0.0,

    @SerializedName("vote_count")     // 🗳️ Review count
    val voteCount: Int = 0
)
