package org.tech.town.gripcompany.presentation.search


import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.tech.town.gripcompany.Constants.Companion.API_KEY
import org.tech.town.gripcompany.adapter.SearchAdapter
import org.tech.town.gripcompany.data.model.Search
import org.tech.town.gripcompany.data.repository.MovieRepository
import org.tech.town.gripcompany.databinding.FragmentSearchBinding


class SearchFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: SearchAdapter
    private var responseList = arrayListOf<Search>()
    private var pagingList = arrayListOf<Search>()
    private lateinit var searchWord: String
    private var totalResults: Int = 0
    private var totalPage = 0
    private var page = 1

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
        paging()

    }

    private fun search(keyword: String, page: Int) {
        initViewModel()
        viewModel.getSearchResponse(API_KEY, keyword, page)
        viewModel.searchResponse.observe(viewLifecycleOwner) {
            responseList.clear()
            responseList = it.body()?.Search as ArrayList<Search>
            pagingList += responseList

            if (page == 1) {
                if (it.isSuccessful) {
                    if (responseList.isEmpty()) {
                        binding.noSearchTextView.visibility = View.VISIBLE
                        adapter.setData(responseList)
                    } else {
                        binding.noSearchTextView.visibility = View.INVISIBLE
                        adapter.setData(responseList)
                        Log.d("TAGTAG", "page=1 paging $pagingList")
                        Log.d("TAGTAG", "page=1 response $responseList")
                    }
                } else {
                    Log.d("TAGTAG", "search: ${it.errorBody().toString()}")
                }
            } else {
                adapter.setData(pagingList)
                Log.d("TAGTAG", "page!=1 paging $pagingList")
                Log.d("TAGTAG", "page!=1 response $responseList")

            }

        }


    }





    private fun paging() {
        binding.searchRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //리사이클러뷰 아이템 위치 찾기
                val recyclerViewPosition =
                    (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                val totalCount = recyclerView.adapter?.itemCount?.minus(1)

                //페이징 처리
                if (recyclerViewPosition == totalCount) {
                    page++
                    Log.d("TAGTAG", "onScrolled: paging $page")
                    search(searchWord, page)

                }
            }
        })
    }

    private fun initSearchRecyclerView() {
        adapter = SearchAdapter()
        binding.searchRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.searchRecyclerView.adapter = adapter
    }

    private fun initViewModel() {
        val repository = MovieRepository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
    }

    private fun searchEnter() {
        binding.toolbar.searchEditText.setOnKeyListener { view, i, keyEvent ->
            if (i == KeyEvent.KEYCODE_ENTER && keyEvent.action == MotionEvent.ACTION_DOWN) {
                pagingList.clear()
                page = 1
                searchWord = binding.toolbar.searchEditText.text.toString()
                search(binding.toolbar.searchEditText.text.toString(), page)
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