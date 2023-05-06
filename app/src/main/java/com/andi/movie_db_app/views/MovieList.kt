package com.andi.movie_db_app.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.andi.movie_db_app.adapters.MovieListAdapter
import com.andi.movie_db_app.api.RequestState
import com.andi.movie_db_app.databinding.ActivityMovieListBinding
import com.andi.movie_db_app.listeners.OnMovieClickListener
import com.andi.movie_db_app.models.Movies
import com.andi.movie_db_app.viewmodels.MovieViewModel

class MovieList : AppCompatActivity() {
    private var _binding: ActivityMovieListBinding? = null
    private val binding get() = _binding!!
    private var adapter: MovieListAdapter? = null
    private var layoutManager: LayoutManager? = null
    private val viewModel: MovieViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMovieListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestThenObserveAnychangeGenres()
        viewModel.getPopularMovie()
        observeAnychangePopularMovie()
        setupRecyclerView()


        adapter?.onMivieClickListener(object: OnMovieClickListener{
            override fun onMoveClick(movies: Movies, genres: String) {
                val intent = Intent(this@MovieList, MovieDetail::class.java)
                intent.putExtra(MovieDetail.movie, movies)
                intent.putExtra(MovieDetail.genres, genres)
                intent.putExtra("movieId", movies.id)
                startActivity(intent)
            }

        })

        binding.searchButton.setOnClickListener {
            val query = binding.search.text.toString()
            when {
                query.isEmpty() -> binding.search.error = "Please insert a keyword!"
                else -> {
                    val intent = Intent(this, SearchMovie::class.java)
                    intent.putExtra(SearchMovie.query, query)
                    startActivity(intent)
                }
            }
        }
    }


    private fun observeAnychangePopularMovie(genreId:Int? =null) {
        viewModel.popularResponse.observe(this) {
            if (it != null) {
                when (it) {
                    is RequestState.Loading -> showLoading()
                    is RequestState.Success -> {
                        hideLoading()
                        if(genreId == null){
                            it.data?.results?.let { data -> adapter?.differ?.submitList(data.toList()) }
                        }else{
                            it.data?.results?.filter { it.genreIds?.contains(genreId) ?: false }?.let { data -> adapter?.differ?.submitList(data.toList()) }
                        }
                    }
                    is RequestState.Error -> {
                        hideLoading()
                        Toast.makeText(this,it.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun requestThenObserveAnychangeGenres() {
        val genreMap = HashMap<String?, Int?>()
        val genreList = ArrayList<String?>()
        viewModel.getGenres().observe(this) {
            if (it != null) {
                when (it) {
                    is RequestState.Loading -> {}
                    is RequestState.Success -> {
                        it.data.genres?.let { data -> adapter?.setGenres(data) }
                        genreList.add("Genre")
                        for (i in it.data.genres!!){
                            genreMap.put(i.name, i.id)
                            genreList.add(i.name)
                        }
                        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, genreList)
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                        val spinner = binding.spGenre
                        spinner.adapter = adapter

                        spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
                                observeAnychangePopularMovie(genreMap.get(genreList[position]))
                            }
                            override fun onNothingSelected(adapterView: AdapterView<*>) {
                                observeAnychangePopularMovie()
                            }
                        })
                    }
                    is RequestState.Error -> Toast.makeText(this,it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = MovieListAdapter()
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.apply {
            movieList.adapter = adapter
            movieList.layoutManager = layoutManager
            movieList.addOnScrollListener(scrollListener)
        }
    }

    private val scrollListener = object: OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (!recyclerView.canScrollVertically(1)) {
                viewModel.getPopularMovie()
            }
        }
    }

    private fun showLoading() {
        binding.loading.show()
    }
    private fun hideLoading() {
        binding.loading.hide()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}