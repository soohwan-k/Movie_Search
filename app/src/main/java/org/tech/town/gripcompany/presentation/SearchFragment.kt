package org.tech.town.gripcompany.presentation

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import org.tech.town.gripcompany.R
import org.tech.town.gripcompany.adapter.SearchAdapter
import org.tech.town.gripcompany.data.api.MovieApi
import org.tech.town.gripcompany.data.model.Search
import org.tech.town.gripcompany.data.model.SearchResponse
import org.tech.town.gripcompany.data.retrofit.RetrofitClient
import org.tech.town.gripcompany.databinding.FragmentSearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: SearchAdapter

    private val movieApi =
        RetrofitClient.getClient("https://www.omdbapi.com")?.create(MovieApi::class.java)

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
        binding.toolbar.searchEditText.setOnKeyListener { view, i, keyEvent ->
            if (i == KeyEvent.KEYCODE_ENTER && keyEvent.action == MotionEvent.ACTION_DOWN) {
                search(binding.toolbar.searchEditText.text.toString())
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }


    }

    private fun search(keyword: String) {
        movieApi?.getSearchResponses(getString(R.string.api_key), keyword)
            ?.enqueue(object : Callback<SearchResponse> {
                override fun onResponse(
                    call: Call<SearchResponse>,
                    response: Response<SearchResponse>
                ) {
                    if (response.isSuccessful.not()) {
                        return
                    }
                    val responseList = response.body()?.Search.orEmpty()

                    if (responseList.isEmpty()) {
                        binding.noSearchTextView.visibility = View.VISIBLE
                        adapter.submitList(responseList)
                    } else {
                        binding.noSearchTextView.visibility = View.INVISIBLE
                        adapter.submitList(responseList)
                    }

                }

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {

                }

            })
    }

    private fun initSearchRecyclerView() {
        adapter = SearchAdapter()
        binding.searchRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.searchRecyclerView.adapter = adapter
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}