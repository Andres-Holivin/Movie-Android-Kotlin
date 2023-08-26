package com.example.myapplication.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.DetailMovieActivity
import com.example.myapplication.R
import com.example.myapplication.models.DataDiscoverModel
import com.example.myapplication.models.DataGenreModel

class DiscoverAdapter internal constructor() :
    RecyclerView.Adapter<DiscoverAdapter.ViewHolder>() {
    private var listItem = ArrayList<DataDiscoverModel>()

    inner class ViewHolder(private val containerView: View) :
        RecyclerView.ViewHolder(containerView) {

        fun bind(data: DataDiscoverModel, position: Int) {
            containerView.findViewById<TextView>(R.id.tv_discover_title).text = data.original_title
            containerView.findViewById<TextView>(R.id.tv_discover_overview).text = data.overview
            containerView.findViewById<TextView>(R.id.tv_discover_release).text = data.release_date.toString()
        }
    }

    override fun getItemCount(): Int = listItem.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemLayout =
            LayoutInflater.from(parent.context).inflate(R.layout.discover_item, parent, false)
        return ViewHolder(itemLayout)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = listItem[position];
        holder.itemView.setOnClickListener {
            holder.itemView.context.startActivity(
                Intent(
                    holder.itemView.context,
                    DetailMovieActivity::class.java
                ).putExtra("discoverId",item.id.toString())
            )
        }
        holder.bind(item, position)
    }

    fun addList(items: List<DataDiscoverModel>) {
        listItem.addAll(items)
        notifyDataSetChanged()
    }

    fun getList(): ArrayList<DataDiscoverModel> {
        return listItem;
    }

    fun clear() {
        listItem.clear()
        notifyDataSetChanged()
    }
}