@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.moviebrowser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.moviebrowser.ui.theme.MovieBrowserTheme
import com.example.moviebrowser.viewmodel.MovieViewModel
import com.example.moviebrowser.data.network.Movie
import java.util.Locale

class MainActivity : ComponentActivity() {

    private val movieViewModel: MovieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }
            val movies by movieViewModel.movies.collectAsState()
            val navController = rememberNavController()

            MovieBrowserTheme(darkTheme = isDarkTheme) {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Movie Browser") },
                            actions = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("Dark Mode")
                                    Switch(
                                        checked = isDarkTheme,
                                        onCheckedChange = { isDarkTheme = it }
                                    )
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "movie_list",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("movie_list") {
                            MovieListScreen(
                                movies = movies,
                                movieViewModel = movieViewModel,
                                navController = navController
                            )
                        }

                        composable(
                            "movie_detail/{movieId}",
                            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0
                            val movie = movies.find { it.id == movieId }
                            if (movie != null) {
                                MovieDetailScreen(movie = movie, navController = navController)
                            }
                        }
                    }

                    // Load default movies
                    LaunchedEffect(Unit) {
                        movieViewModel.searchMovies("Avengers")
                    }
                }
            }
        }
    }
}

@Composable
fun MovieListScreen(
    movies: List<Movie>,
    movieViewModel: MovieViewModel,
    navController: androidx.navigation.NavHostController
) {
    var searchQuery by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { query ->
                searchQuery = query
                if (query.isNotBlank()) {
                    movieViewModel.searchMovies(query)
                }
            },
            label = { Text("Search Movies") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(movies) { movie ->
                MovieItem(movie = movie, navController = navController)
            }
        }
    }
}

@Composable
fun MovieItem(movie: Movie, navController: androidx.navigation.NavHostController) {
    val imageUrl = movie.posterpath?.let { "https://image.tmdb.org/t/p/w200$it" }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { navController.navigate("movie_detail/${movie.id}") },
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (imageUrl != null) {
            Image(
                painter = rememberAsyncImagePainter(model = imageUrl),
                contentDescription = movie.title,
                modifier = Modifier.size(100.dp, 150.dp),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .size(100.dp, 150.dp)
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Text("No Image", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.width(16.dp))
        Text(movie.title, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun MovieDetailScreen(movie: Movie, navController: androidx.navigation.NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter("https://image.tmdb.org/t/p/w500${movie.posterpath}"),
            contentDescription = movie.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(movie.title, style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(8.dp))

        RatingBar(rating = (movie.voteAverage / 2).toFloat())
        Text(
            text = "(${String.format(Locale.getDefault(), "%.1f", movie.voteAverage)} / 10)",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "📝 Reviews: ${movie.voteCount}",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(12.dp))
        Text(movie.overview, style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.popBackStack() }) {
            Text("Back")
        }
    }
}

@Composable
fun RatingBar(rating: Float, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        for (i in 1..5) {
            if (i <= rating.toInt()) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Star",
                    tint = Color(0xFFFFD700)
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.Star, // correct outlined star
                    contentDescription = "Empty Star",
                    tint = Color(0xFFFFD700)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMovieItem() {
    MovieBrowserTheme {
        MovieItem(
            Movie(
                id = 1,
                title = "Avengers: Endgame",
                posterpath = "/or06FN3Dka5tukK1e9sl16pB3iy.jpg",
                voteAverage = 8.4,
                voteCount = 25000
            ),
            navController = rememberNavController()
        )
    }
}
