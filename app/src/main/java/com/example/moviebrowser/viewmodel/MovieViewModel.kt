package com.example.moviebrowser.viewmodel


import com.example.moviebrowser.BuildConfig
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviebrowser.data.network.Movie
import com.example.moviebrowser.data.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies

    fun searchMovies(query: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.searchMovies(
                    apiKey = BuildConfig.TMDB_API_KEY,
                    query = query
                )
                _movies.value = response.results
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
