package com.example.myapplication.models

data class GenreModel(
    val genres: ArrayList<DataGenreModel>
)

data class DataGenreModel(
    val id: Int,
    val name: String
)