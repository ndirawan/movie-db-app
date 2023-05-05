package com.andi.movie_db_app.api

import com.andi.movie_db_app.models.GenreResponse
import com.andi.movie_db_app.models.MovieResponse
import com.andi.movie_db_app.models.TrailerResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("movie/popular")
    suspend fun getPopularMovie(
        @Query("api_key") key: String?,
        @Query("page") page: Int?
    ): Response<MovieResponse>

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("api_key") key: String?,
        @Query("query") query: String?,
        @Query("page") page: Int?
    ): Response<MovieResponse>

    @GET("genre/movie/list")
    suspend fun getGenres(
        @Query("api_key") key: String?
    ): GenreResponse

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideo(
        @Path("movie_id") movieId:Int?,
        @Query("api_key") key: String?
    ): TrailerResponse
}