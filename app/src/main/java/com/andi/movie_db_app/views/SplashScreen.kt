package com.andi.movie_db_app.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.andi.movie_db_app.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {
    private var _binding: ActivitySplashScreenBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(mainLooper).postDelayed({
            startActivity(Intent(this,MovieList::class.java))
            finish()
        }, 1000)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}