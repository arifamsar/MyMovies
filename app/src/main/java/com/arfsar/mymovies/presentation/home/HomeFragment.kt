package com.arfsar.mymovies.presentation.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.arfsar.mymovies.R
import com.arfsar.mymovies.core.data.Resource
import com.arfsar.mymovies.core.domain.model.Movies
import com.arfsar.mymovies.core.ui.MoviesAdapter
import com.arfsar.mymovies.databinding.FragmentHomeBinding
import com.arfsar.mymovies.databinding.FragmentSettingsBinding
import com.arfsar.mymovies.presentation.detail.DetailActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModel()

    private var _binding: FragmentHomeBinding? = null
    private var _settingBinding: FragmentSettingsBinding? = null

    private val binding get() = _binding!!
    private val settingBinding get() = _settingBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        _settingBinding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null) {
            setupRecyclerView()
            setupObservers()
            setupSearchView()
            setupSwipeRefresh()
            switchThemeSetting(settingBinding.switchTheme)
        }

    }

    private fun setupRecyclerView() {
        val moviesAdapter = MoviesAdapter()
        val mLayoutManager = GridLayoutManager(context, 2)
        with(binding.rvMovies) {
            layoutManager = mLayoutManager
            setHasFixedSize(true)
            adapter = moviesAdapter
        }

        moviesAdapter.setOnItemClickCallback(object : MoviesAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Movies) {
                getDetailMovie(data)
            }
        })
    }

    private fun getDetailMovie(data: Movies) {
        val intent = Intent(activity, DetailActivity::class.java)
        intent.putExtra(EXTRA_DATA, data.movieId)
        startActivity(intent)
    }

    private fun setupObservers() {
        homeViewModel.getMovies().observe(viewLifecycleOwner) { movies ->
            handleMovieResponse(movies)
        }
    }

    private fun handleMovieResponse(movies: Resource<List<Movies>>) {
        val moviesAdapter = binding.rvMovies.adapter as MoviesAdapter
        when (movies) {
            is Resource.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
                binding.rvMovies.visibility = View.GONE
            }
            is Resource.Success -> {
                binding.progressBar.visibility = View.GONE
                binding.rvMovies.visibility = View.VISIBLE
                moviesAdapter.submitList(movies.data)
            }
            is Resource.Error -> {
                binding.progressBar.visibility = View.GONE
                binding.viewError.root.visibility = View.VISIBLE
                binding.viewError.tvError.text = movies.message ?: getString(R.string.something_wrong)
            }
        }
    }

    private fun setupSearchView() {
        binding.apply {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { v, actionId, event ->
                    searchBar.setText(searchView.text)
                    homeViewModel.searchMovies(searchView.text.toString()).observe(viewLifecycleOwner) { movies ->
                        handleMovieResponse(movies)
                    }
                    searchView.hide()
                    false
                }
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            homeViewModel.getMovies().observe(viewLifecycleOwner) { movies ->
                handleMovieResponse(movies)
            }
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun switchThemeSetting(switch: SwitchMaterial) {
        homeViewModel.getThemeSetting().observe(viewLifecycleOwner) { state: Boolean ->
            if (state) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switch.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switch.isChecked = false
            }
        }

        switch.setOnCheckedChangeListener { _, isChecked ->
            homeViewModel.saveThemeSetting(isChecked)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _settingBinding = null
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }

}