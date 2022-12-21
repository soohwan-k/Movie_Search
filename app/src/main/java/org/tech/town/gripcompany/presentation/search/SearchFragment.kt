package org.tech.town.gripcompany.presentation.search

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log

import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import org.tech.town.gripcompany.Constants.Companion.API_KEY
import org.tech.town.gripcompany.adapter.SearchAdapter
import org.tech.town.gripcompany.data.repository.MovieRepository
import org.tech.town.gripcompany.databinding.FragmentSearchBinding


class SearchFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: SearchAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSearchRecyclerView()
        searchEnter()

    }

    private fun search(keyword: String) {

        val repository = MovieRepository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        viewModel.getSearchResponse(API_KEY, keyword, 1)
        viewModel.searchResponse.observe(viewLifecycleOwner, Observer {
            val responseList = it.body()?.Search.orEmpty()
            if (it.isSuccessful) {
                if (responseList.isEmpty()) {
                    binding.noSearchTextView.visibility = View.VISIBLE
                    adapter.submitList(responseList)
                } else {
                    binding.noSearchTextView.visibility = View.INVISIBLE
                    adapter.submitList(responseList)
                }
            } else {
                Log.d(TAG, "search: ${it.errorBody().toString()}")
            }


        })
    }

    private fun initSearchRecyclerView() {
        adapter = SearchAdapter()
        binding.searchRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.searchRecyclerView.adapter = adapter

    }

    private fun searchEnter() {
        binding.toolbar.searchEditText.setOnKeyListener { view, i, keyEvent ->
            if (i == KeyEvent.KEYCODE_ENTER && keyEvent.action == MotionEvent.ACTION_DOWN) {
                search(binding.toolbar.searchEditText.text.toString())
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}