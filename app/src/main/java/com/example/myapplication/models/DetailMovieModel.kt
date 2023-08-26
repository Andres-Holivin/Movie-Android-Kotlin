package com.example.myapplication.models

data class DetailMovieModel(
    val id: Int,
    val title: String,
    val vote_average: String
)

data class ReviewMovieModel(
    val id: Int,
    val page: Int,
    val total_pages: Int,
    val total_results: Int,
    val results: List<DataReviewMovieModel>
)

data class DataReviewMovieModel(
    val id: String,
    val author: String,
    val content: String,
)

data class VideoMovieModel(
    val id: String,
    val results: List<DataVideoMovieModel>
)
data class DataVideoMovieModel(
    val id: String,
    val key: String
)
