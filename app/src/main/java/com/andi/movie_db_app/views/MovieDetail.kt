package com.andi.movie_db_app.views

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.andi.movie_db_app.BuildConfig
import com.andi.movie_db_app.R
import com.andi.movie_db_app.databinding.ActivityMovieDetailBinding
import com.andi.movie_db_app.models.Movies
import com.andi.movie_db_app.repositories.MoveiRepository
import com.bumptech.glide.Glide
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.launch

class MovieDetail : AppCompatActivity() {
    private var _binding: ActivityMovieDetailBinding? = null
    private val binding get() = _binding!!
    private val repo: MoveiRepository = MoveiRepository()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.parcelable<Movies>(movie)?.let {
            intent.getStringExtra(genres)?.let { genres -> setupData(it, genres) } }
        binding.btn.setOnClickListener {
            lifecycleScope.launch {
            trailerYoutube()
        } }
    }

    private fun setupData(movie: Movies, genres: String) {
        with(movie) {
            binding.apply {
                Glide.with(this@MovieDetail).load("${BuildConfig.PHOTO_BASE_URL}${posterPath}").into(posterDetail)
                titleDetail.text = title
                releaseDateDetail.text = releaseDate
                ratingText.text = voteAverage.toString()
                ratingBar.rating = voteAverage?.div(2) ?: 0f
                genreDetail.text = genres.dropLast(2)
                overview.text = movie.overview
            }
        }
    }
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
        Build.VERSION.SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
    }

    companion object {
        const val movie = "movie"
        const val genres = "genres"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private suspend fun trailerYoutube(){
        val popupView = LayoutInflater.from(this).inflate(R.layout.activity_movie_trailer, null)
        var trailer = popupView.findViewById<YouTubePlayerView>(R.id.youtube_player_view)

        val movieId = intent.getIntExtra("movieId", 0)
        val videoId = repo.getMovieVideo(movieId).results[0].key

        trailer.enableAutomaticInitialization = false
        trailer.initialize(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)
                if (videoId != null) {
                    youTubePlayer.loadVideo(videoId, 0f)
                }
            }
        })
        val alertDialog = AlertDialog.Builder(this).setView(popupView).create()
        alertDialog.show()
    }
}