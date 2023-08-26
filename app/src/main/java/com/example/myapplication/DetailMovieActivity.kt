package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapters.ReviewMovieAdapter
import com.example.myapplication.models.DataReviewMovieModel
import com.example.myapplication.models.DetailMovieModel
import com.example.myapplication.utils.RetrofitClient


private const val TAG = "DetailMoview"

class DetailMovieActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private var isLoading = false
    private lateinit var loading: ProgressBar;
    private var page = 1
    private var totalPage: Int = 1
    var listData: ArrayList<DataReviewMovieModel> = ArrayList()
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerViewAdapter: ReviewMovieAdapter
    private var discover: Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_movie_main)
        discover =
            if (intent.getStringExtra("discoverId") != null) intent.getStringExtra("discoverId")!!
                .toInt() else 0
        loading = findViewById<ProgressBar>(R.id.progress_bar_detail_movie)
        loading.visibility = View.GONE
        Log.e(TAG, "Run")
        initAdapter()
        getMovieData(discover)
        initScrollListener(discover)
        initFavorite(discover)

    }

    private fun initText(data: DetailMovieModel) {
        var title = findViewById<TextView>(R.id.tv_review_movie_title)
        var rating = findViewById<TextView>(R.id.tv_review_movie_rating)
        title.text = data.title
        rating.text = "Rating: ${data.vote_average}"
    }

    private fun initFavorite(id: Int) {
        var cb = findViewById<CheckBox>(R.id.cb_detail_movie)
        val sp = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        cb.isChecked = sp.getInt(id.toString(), 0) == 1
        cb.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            val edit = sp.edit()
            edit.putInt(id.toString(), (if (isChecked) 1 else 0))
            edit.commit()
            edit.apply()
        })


    }

    private fun initAdapter() {
        recyclerView = findViewById(R.id.detail_movie_list_view)
        recyclerViewAdapter = ReviewMovieAdapter()
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.adapter = recyclerViewAdapter
    }

    private fun initScrollListener(id: Int) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                val visibleItemCount = linearLayoutManager?.childCount
                val pastVisibleItem = linearLayoutManager?.findLastVisibleItemPosition()
                val size = listData.size
                Log.d(TAG, "$isLoading $page $totalPage")
                if (!isLoading && page < totalPage) {
                    Log.d(TAG, "$pastVisibleItem $size")
                    if (linearLayoutManager != null && pastVisibleItem == size - 1) {
                        page++
                        loadMoreReviewMovie(id)
                    }
                }
            }
        })
    }

    private fun getMovieData(id: Int) {
        lifecycleScope.launchWhenCreated {
            try {
                val detailMovie = RetrofitClient.instance.getDetailMovie(id)
                if (detailMovie.isSuccessful && detailMovie.body() != null) {
                    initText(detailMovie.body()!!)
                    val videoMovie = RetrofitClient.instance.getVideoMovie(id)
                    val reviewMovie = RetrofitClient.instance.getReviewMovie(id, page)
                    Log.e(TAG,videoMovie.toString())
                    Log.e(TAG,reviewMovie.toString())
                    if (videoMovie.isSuccessful && reviewMovie.isSuccessful) {
                        val reviewData = reviewMovie.body()
                        val videoData = videoMovie.body()
                        if (reviewData != null && videoData != null) {
                            loadYoutube(videoData.results[0].key)
                            listData.addAll(reviewData.results)
                            recyclerViewAdapter.addList(reviewData.results)
                        }
                    }
                    loading.visibility = View.GONE
                    isLoading = false
                } else {
                    Toast.makeText(
                        this@DetailMovieActivity,
                        "Something went wrong!",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    loading.visibility = View.GONE
                    isLoading = false
                }
            } catch (e: Exception) {
                Log.e(TAG, "Get API Exception: $e")
                loading.visibility = View.GONE
                return@launchWhenCreated
            }
        }
    }

    private fun loadMoreReviewMovie(id: Int) {
        isLoading = true
        loading.visibility = View.VISIBLE
        lifecycleScope.launchWhenCreated {
            val response = try {
                RetrofitClient.instance.getReviewMovie(id, page = page);
            } catch (e: Exception) {
                Log.e(TAG, "Exception: $e")
                loading.visibility = View.GONE
                return@launchWhenCreated
            }
            if (response.isSuccessful && response.body() != null) {
                totalPage = response.body()?.total_pages!!
                val listResponse = response.body()?.results
                if (listResponse != null) {
                    recyclerViewAdapter.addList(listResponse)
                    listData.addAll(listResponse)
                }
                loading.visibility = View.GONE
                isLoading = false
            } else {
                Toast.makeText(
                    this@DetailMovieActivity,
                    "Something went wrong!",
                    Toast.LENGTH_SHORT
                )
                    .show()
                loading.visibility = View.GONE
                isLoading = false
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun loadYoutube(youtubeCode: String) {
        webView = findViewById(R.id.webView)
        val youTubeUrl = "https://www.youtube.com/embed/$youtubeCode"
        val frameVideo =
            "<html><body><iframe width=\"auto\" height=\"auto\" " + "src='" + youTubeUrl + "' frameborder=\"0\" allowfullscreen>" + "</iframe></body></html>"
        val regexYoutUbe: String = "^(http(s)?:\\/\\/)?((w){3}.)?youtu(be|.be)?(\\.com)?\\/.+"
        if (youTubeUrl.matches(regexYoutUbe.toRegex())) {
            webView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    return false
                }
            }
            val webSettings = webView.settings;
            webSettings.setJavaScriptEnabled(true);
            webView.loadData(frameVideo, "text/html", "utf-8");
        }
    }
}