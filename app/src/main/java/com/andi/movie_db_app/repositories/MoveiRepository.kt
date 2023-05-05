package com.andi.movie_db_app.repositories

import com.andi.movie_db_app.BuildConfig
import com.andi.movie_db_app.api.ApiConfig
import com.andi.movie_db_app.models.MovieResponse
import retrofit2.Response

class MoveiRepository {
    private val client = ApiConfig.getApiService()

    suspend fun getPopularMovie(page: Int) = client.getPopularMovie(BuildConfig.API_KEY, page)
    suspend fun searchMovie(query: String, page: Int) = client.searchMovie(BuildConfig.API_KEY, query, page)
    suspend fun getGenres() = client.getGenres(BuildConfig.API_KEY)
    suspend fun getMovieVideo(movieId: Int) = client.getMovieVideo(movieId, BuildConfig.API_KEY)
}