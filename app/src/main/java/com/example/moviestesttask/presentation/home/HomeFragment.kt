package com.example.moviestesttask.presentation.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviestesttask.R
import com.example.moviestesttask.databinding.FragmentHomeBinding
import com.example.moviestesttask.presentation.adapters.MoviesAdapter
import com.example.moviestesttask.presentation.viewmodels.MovieViewModel
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

    private val binding: FragmentHomeBinding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentHomeBinding.inflate(layoutInflater)
    }
    private val vm: MovieViewModel by hiltNavGraphViewModels(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setupRecycler()


    }
    private fun FragmentHomeBinding.setupRecycler() {

        val manager = LinearLayoutManager(root.context)
        val adapter = MoviesAdapter()
        homeRec.adapter = adapter
        homeRec.layoutManager = manager
        bindList(adapter)
    }

    private fun FragmentHomeBinding.bindList(adapter: MoviesAdapter) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    vm.pagingDataFlow.collect { data ->
                        adapter.submitData(data)
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "HomeFragment"
    }
}


