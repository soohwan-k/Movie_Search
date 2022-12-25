package org.tech.town.gripcompany.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.tech.town.gripcompany.data.database.AppDatabase
import org.tech.town.gripcompany.data.model.Search
import org.tech.town.gripcompany.databinding.ItemSearchBinding

class SearchAdapter(context: Context) : RecyclerView.Adapter<SearchAdapter.SearchItemViewHolder>(), ItemTouchHelperListener {

    private var searchList = arrayListOf<Search>()
    private lateinit var listener: OnItemClickListener

    private var db = AppDatabase.getInstance(context)!!

    class SearchItemViewHolder(val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemViewHolder {
        return SearchItemViewHolder(
            ItemSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchItemViewHolder, position: Int) {
        holder.binding.apply {
            titleTextView.text = searchList[position].Title
            yearTextView.text = searchList[position].Year
            typeTextView.text = searchList[position].Type

            Glide.with(posterImageView.context)
                .load(searchList[position].Poster)
                .into(posterImageView)
        }

        holder.itemView.setOnClickListener {
            listener.onClick(it, position)
        }
    }

    fun setItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun getItemCount(): Int = searchList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList: ArrayList<Search>) {
        searchList = newList
        notifyDataSetChanged()
    }

    override fun onItemMove(from: Int, to: Int) {
        val item: Search = searchList[from]
        db.favoriteDao().deleteAll()
        searchList.removeAt(from)
        searchList.add(to, item)
        for (i in searchList){
            db.favoriteDao().insert(i)
        }

        notifyItemMoved(from, to)
    }

}
