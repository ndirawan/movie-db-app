package com.andi.movie_db_app.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.andi.movie_db_app.databinding.ActivityMovieTrailerBinding

class MovieTrailer : AppCompatActivity() {
    private lateinit var binding : ActivityMovieTrailerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieTrailerBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}