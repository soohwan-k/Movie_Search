package org.tech.town.gripcompany.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.tech.town.gripcompany.data.model.Search
import org.tech.town.gripcompany.databinding.ItemSearchBinding


class SearchAdapter : RecyclerView.Adapter<SearchAdapter.SearchItemViewHolder>() {

    private var searchList = listOf<Search>()
    private lateinit var listener: OnItemClickListener




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
        holder.binding.titleTextView.text = searchList[position].Title
        holder.binding.yearTextView.text = searchList[position].Year
        holder.binding.typeTextView.text = searchList[position].Type
        Glide
            .with(holder.binding.posterImageView.context)
            .load(searchList[position].Poster)
            .into(holder.binding.posterImageView)

        holder.itemView.setOnClickListener {
            listener.onClick(it, position)
        }


    }

    fun setItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun getItemCount(): Int {
        return searchList.size
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList: List<Search>) {
        searchList = newList
        notifyDataSetChanged()
    }


}