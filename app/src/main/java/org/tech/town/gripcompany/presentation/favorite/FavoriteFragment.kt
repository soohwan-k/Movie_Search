package org.tech.town.gripcompany.presentation.favorite

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import org.tech.town.gripcompany.MainActivity
import org.tech.town.gripcompany.R
import org.tech.town.gripcompany.adapter.OnItemClickListener
import org.tech.town.gripcompany.adapter.SearchAdapter
import org.tech.town.gripcompany.data.database.AppDatabase
import org.tech.town.gripcompany.data.model.Search
import org.tech.town.gripcompany.databinding.FragmentFavoriteBinding
import org.tech.town.gripcompany.presentation.search.SearchFragment

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: SearchAdapter
    private lateinit var list: List<Search>

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
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSearchRecyclerView()
        getFavoriteMovie()
        listClick()
    }

    private fun initSearchRecyclerView() {
        adapter = SearchAdapter()
        binding.searchRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.searchRecyclerView.adapter = adapter
    }

    private fun getFavoriteMovie() {
        db = AppDatabase.getInstance(mainActivity)!!
        list = db.favoriteDao().getAll()
        adapter.setData(list)
    }

    private fun listClick() {
        adapter.setItemClickListener(object : OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                unDoFavoritePopup(position)
            }
        })
    }

    private fun unDoFavoritePopup(position: Int) {
        AlertDialog.Builder(mainActivity)
            .setTitle(getString(R.string.un_do_favorite_title))
            .setPositiveButton(getString(R.string.un_do_favorite)){_, _ ->
                db.favoriteDao().delete(list[position])
                adapter.setData(db.favoriteDao().getAll())
            }
            .setNegativeButton(getString(R.string.cancel)) {_, _ ->}
            .create()
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
