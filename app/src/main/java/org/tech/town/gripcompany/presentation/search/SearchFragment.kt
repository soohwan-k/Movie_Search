package org.tech.town.gripcompany.presentation.search

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.tech.town.gripcompany.Constants.Companion.API_KEY
import org.tech.town.gripcompany.MainActivity
import org.tech.town.gripcompany.R
import org.tech.town.gripcompany.adapter.OnItemClickListener
import org.tech.town.gripcompany.adapter.SearchAdapter
import org.tech.town.gripcompany.data.database.AppDatabase
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
    private var page = 1

    private lateinit var db: AppDatabase

    private lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity
    }

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

        db = AppDatabase.getInstance(mainActivity)!!

        initSearchRecyclerView()
        initViewModel()
        searchEnter()
        paging()
        listClick()
    }

    private fun search(keyword: String, page: Int) {
        viewModel.getSearchResponse(API_KEY, keyword, page)
        viewModel.searchResponse.observe(viewLifecycleOwner) {
            responseList.clear()
            responseList = (it.body()?.Search ?: arrayListOf<Search>()) as ArrayList<Search>

            pagingList += responseList

            if (page == 1) {
                if (it.isSuccessful) {
                    adapter.setData(pagingList)
                    if (adapter.itemCount == 0) {
                        binding.noSearchTextView.visibility = View.VISIBLE
                    } else {
                        binding.noSearchTextView.visibility = View.INVISIBLE
                    }
                } else {
                    Log.d(TAG, "search: ${it.errorBody().toString()}")
                }
            } else {
                adapter.setData(pagingList)
            }
        }
    }

    private fun paging() {
        binding.searchRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val recyclerViewPosition =
                    (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                val totalCount = recyclerView.adapter?.itemCount?.minus(1)

                if (recyclerViewPosition == totalCount) {
                    page++
                    search(searchWord, page)
                }
            }
        })
    }

    private fun initSearchRecyclerView() {
        adapter = SearchAdapter(mainActivity)
        binding.searchRecyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.searchRecyclerView.adapter = adapter
    }

    private fun initViewModel() {
        val repository = MovieRepository()
        val viewModelFactory = MainViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
    }

    private fun searchEnter() {
        binding.toolbar.searchEditText.apply {
            setOnKeyListener { _, i, keyEvent ->
                if (i == KeyEvent.KEYCODE_ENTER && keyEvent.action == MotionEvent.ACTION_DOWN) {
                    pagingList.clear()
                    page = 1
                    searchWord = text.toString()
                    search(text.toString(), page)
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }
        }
    }

    private fun listClick() {
        adapter.setItemClickListener(object : OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                checkFavorite()
                if (pagingList[position].isFavorite) {
                    unDoFavoritePopup(position)
                } else {
                    doFavoritePopup(position)
                }
            }
        })
    }

    private fun doFavoritePopup(position: Int) {
        AlertDialog.Builder(mainActivity)
            .setTitle(getString(R.string.do_favorite_title))
            .setPositiveButton(getString(R.string.do_favorite)) { _, _ ->
                pagingList[position].isFavorite = true
                db.favoriteDao().insert(pagingList[position])
            }
            .setNegativeButton(getString(R.string.cancel)) { _, _ -> }
            .create()
            .show()
    }

    private fun unDoFavoritePopup(position: Int) {
        AlertDialog.Builder(mainActivity)
            .setTitle(getString(R.string.un_do_favorite_title))
            .setPositiveButton(getString(R.string.un_do_favorite)) { _, _ ->
                pagingList[position].isFavorite = false
                db.favoriteDao().delete(pagingList[position])
            }
            .setNegativeButton(getString(R.string.cancel)) { _, _ -> }
            .create()
            .show()
    }

    private fun checkFavorite() {
        val list = db.favoriteDao().getAll()
        for (i in 0 until pagingList.size) {
            for (j in list.indices) {
                if (pagingList[i].imdbID == list[j].imdbID) {
                    pagingList[i].isFavorite = true
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
