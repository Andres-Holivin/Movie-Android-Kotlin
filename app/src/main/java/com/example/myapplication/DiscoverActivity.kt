package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapters.DiscoverAdapter
import com.example.myapplication.models.DataDiscoverModel
import com.example.myapplication.utils.RetrofitClient


class DiscoverActivity : AppCompatActivity() {
    val TAG = "DiscoverActivity"
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerViewAdapter: DiscoverAdapter
    var listData: ArrayList<DataDiscoverModel> = ArrayList()
    private var isLoading = false
    private lateinit var loading: ProgressBar;
    private var page = 1
    private var totalPage: Int = 1
    private var genre: String? = null;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.discover_main)
        loading = findViewById<ProgressBar>(R.id.progress_bar_discover)
        genre = intent.getStringExtra("genreId")
        recyclerView = findViewById(R.id.discover_list_view)
        recyclerViewAdapter = DiscoverAdapter()
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.adapter = recyclerViewAdapter
        populateData()
        initScrollListener()
    }

    private fun initScrollListener() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                val visibleItemCount = linearLayoutManager?.childCount
                val pastVisibleItem = linearLayoutManager?.findLastVisibleItemPosition()
                val size = listData.size
                if (!isLoading && page < totalPage) {
                    if (linearLayoutManager != null && pastVisibleItem == size - 1) {
                        page++
                        populateData()
                    }
                }
            }
        })
    }

    private fun populateData() {
        isLoading = true
        loading.visibility = View.VISIBLE
        lifecycleScope.launchWhenCreated {
            val response = try {
                Log.e(TAG, "genre: $genre")
                RetrofitClient.instance.getSources(genre = genre!!, page = page);
            } catch (e: Exception) {
                Log.e(TAG, "Exception: $e")
                loading.visibility = View.GONE
                return@launchWhenCreated
            }
            Log.e(TAG, "res: $response")
            if (response.isSuccessful && response.body() != null) {
                totalPage = response.body()?.total_pages!!
                Log.d("PAGE", "totalPage: $totalPage")
                val listResponse = response.body()?.results
                if (listResponse != null) {
                    Log.d("PAGE", "listResponse != null")
                    recyclerViewAdapter.addList(listResponse)
                    Log.e(TAG, listResponse.size.toString())
                    listData.addAll(listResponse)
                }
                loading.visibility = View.GONE
                isLoading = false
            } else {
                Toast.makeText(this@DiscoverActivity, "Something went wrong!", Toast.LENGTH_SHORT)
                    .show()
                loading.visibility = View.GONE
                isLoading = false
            }
        }
    }


}