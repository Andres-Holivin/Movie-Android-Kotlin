package com.example.myapplication.models

import java.util.Date

data class DiscoverModel(
    val page: Int,
    val total_pages: Int,
    val total_results: Int,
    val results:List<DataDiscoverModel>
)
data class DataDiscoverModel(
    val id: Int,
    val overview: String,
    val original_title: String,
    val poster_path: String,
    val release_date: Date
)