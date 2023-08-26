package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapters.GenreAdapter
import com.example.myapplication.utils.RetrofitClient

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loading = findViewById<ProgressBar>(R.id.progress_bar_genre)
        lifecycleScope.launchWhenCreated {
            loading.visibility = View.VISIBLE
            val response = try {
                RetrofitClient.instance.getGender();
            } catch (e: Exception) {
                Log.e(TAG, "Exception: $e")
                loading.visibility = View.GONE
                return@launchWhenCreated
            }
            Log.e(TAG, response.toString())
            if (response.isSuccessful && response.body() != null) {
                Toast.makeText(
                    this@MainActivity,
                    "" + response.body()?.genres?.size + " users loaded!",
                    Toast.LENGTH_SHORT
                ).show()
                val usersRecyclerView = findViewById<RecyclerView>(R.id.genre_list_view)

                usersRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                usersRecyclerView.adapter = GenreAdapter(response.body()!!.genres)

                loading.visibility = View.GONE
            } else {
                Toast.makeText(this@MainActivity, "Something went wrong!", Toast.LENGTH_SHORT)
                    .show()
                loading.visibility = View.GONE
            }
        }

    }
}