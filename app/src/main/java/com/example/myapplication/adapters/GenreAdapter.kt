package com.example.myapplication.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.DiscoverActivity
import com.example.myapplication.R
import com.example.myapplication.models.DataGenreModel

class GenreAdapter internal constructor(private val itemList: List<DataGenreModel>) :
    RecyclerView.Adapter<GenreAdapter.ViewHolder>() {

    inner class ViewHolder(private val containerView: View) :
        RecyclerView.ViewHolder(containerView) {

        fun bind(gender: DataGenreModel) {
            containerView.findViewById<TextView>(R.id.tv_genre_Name).text = gender.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemLayout =
            LayoutInflater.from(parent.context).inflate(R.layout.genre_item, parent, false)
        return ViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = itemList[position]
        holder.itemView.setOnClickListener {
            holder.itemView.context.startActivity(
                Intent(
                    holder.itemView.context,
                    DiscoverActivity::class.java
                ).putExtra("genreId",item.id.toString())
            )
        }
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return itemList.count();
    }
}
