package com.example.myapplication.services

import com.example.myapplication.models.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

const val token: String =
    "";

interface ApiService {
    @Headers(
        "Authorization: Bearer $token",
        "accept: application/json"
    )
    @GET("genre/movie/list")
    suspend fun getGender(): Response<GenreModel>

    @Headers(
        "Authorization: Bearer $token",
        "accept: application/json"
    )
    @GET("discover/movie")
    suspend fun getSources(
        @Query("with_genres") genre: String,
        @Query("page") page: Int,
    ): Response<DiscoverModel>

    @Headers(
        "Authorization: Bearer $token",
        "accept: application/json"
    )
    @GET("movie/{id}")
    suspend fun getDetailMovie(
        @Path("id") id:Int
    ): Response<DetailMovieModel>

    @Headers(
        "Authorization: Bearer $token",
        "accept: application/json"
    )
    @GET("movie/{id}/videos")
    suspend fun getVideoMovie(
        @Path("id") id:Int
    ): Response<VideoMovieModel>

    @Headers(
        "Authorization: Bearer $token",
        "accept: application/json"
    )
    @GET("movie/{id}/reviews")
    suspend fun getReviewMovie(
        @Path("id") id:Int,
        @Query("page") page: Int,
    ): Response<ReviewMovieModel>
}
