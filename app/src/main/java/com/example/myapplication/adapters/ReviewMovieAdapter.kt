package com.example.myapplication.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.DetailMovieActivity
import com.example.myapplication.R
import com.example.myapplication.models.DataReviewMovieModel

class ReviewMovieAdapter internal constructor() :
    RecyclerView.Adapter<ReviewMovieAdapter.ViewHolder>() {
    private var listItem = ArrayList<DataReviewMovieModel>()

    inner class ViewHolder(private val containerView: View) :
        RecyclerView.ViewHolder(containerView) {
        fun bind(data: DataReviewMovieModel, position: Int) {
            containerView.findViewById<TextView>(R.id.tv_review_movie_author).text = data.author
            containerView.findViewById<TextView>(R.id.tv_review_movie_content).text = data.content
        }
    }

    override fun getItemCount(): Int {
        return listItem.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemLayout =
            LayoutInflater.from(parent.context).inflate(R.layout.review_movie_item, parent, false)
        return ViewHolder(itemLayout)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = listItem[position];
        holder.bind(item, position)
    }

    fun addList(items: List<DataReviewMovieModel>) {
        listItem.addAll(items)
        notifyDataSetChanged()
    }

    fun getList(): ArrayList<DataReviewMovieModel> {
        return listItem;
    }

    fun clear() {
        listItem.clear()
        notifyDataSetChanged()
    }
}