package com.andi.movie_db_app.listeners

import com.andi.movie_db_app.models.Movies

interface OnMovieClickListener {
    fun onMoveClick(movies: Movies, genres: String)
}