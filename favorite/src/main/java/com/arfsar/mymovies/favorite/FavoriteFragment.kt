package com.arfsar.mymovies.favorite

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.arfsar.mymovies.core.domain.model.Movies
import com.arfsar.mymovies.core.ui.MoviesAdapter
import com.arfsar.mymovies.favorite.databinding.FragmentFavoriteBinding
import com.arfsar.mymovies.ui.detail.DetailActivity
import com.arfsar.mymovies.ui.home.HomeFragment.Companion.EXTRA_DATA
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val favoriteViewModel: FavoriteViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadKoinModules(favoriteModule)

        if (activity != null) {
            val moviesAdapter = MoviesAdapter()
            val mLayoutManager = GridLayoutManager(context, 2)
            with(binding.rvMovies) {
                layoutManager = mLayoutManager
                setHasFixedSize(true)
                adapter = moviesAdapter
            }

            favoriteViewModel.favoriteMovies.observe(viewLifecycleOwner) { dataMovies ->
                moviesAdapter.submitList(dataMovies)
                binding.viewEmpty.root.visibility = if (dataMovies.isNotEmpty()) View.GONE else View.VISIBLE
            }

            moviesAdapter.setOnItemClickCallback(object : MoviesAdapter.OnItemClickCallback {
                override fun onItemClicked(data: Movies) {
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra(EXTRA_DATA, data.movieId)
                    startActivity(intent)
                }

            })


        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}