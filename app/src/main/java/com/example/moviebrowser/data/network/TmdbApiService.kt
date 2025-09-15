package com.example.moviebrowser.data.network

import retrofit2.http.GET
import retrofit2.http.Query

// Retrofit API interface
interface TmdbApiService {
    @GET("search/movie")
    suspend fun searchMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String
    ): MovieResponse
}
