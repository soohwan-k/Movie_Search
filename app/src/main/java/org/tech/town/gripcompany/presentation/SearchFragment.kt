package org.tech.town.gripcompany.presentation

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import org.tech.town.gripcompany.adapter.SearchAdapter
import org.tech.town.gripcompany.data.api.MovieApi
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
        // 코드 작성
        //RetrofitManager.instance.searchMovies("92e32667", "Iron man")
        initSearchRecyclerView()

        movieApi?.getSearchResponses("92e32667", "Iron man")
            ?.enqueue(object : Callback<SearchResponse> {
                override fun onResponse(
                    call: Call<SearchResponse>,
                    response: Response<SearchResponse>
                ) {
                    if (response.isSuccessful.not()){
                        return
                    }
                    response.body()?.let{
                        Log.d(TAG, "onResponse: called ${it.toString()}")
                        it.Search.forEach{ search ->
                            Log.d(TAG, "onResponse: ${search.toString()}")
                        }
                        adapter.submitList(it.Search)
                    }

                }

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                    TODO("Not yet implemented")
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