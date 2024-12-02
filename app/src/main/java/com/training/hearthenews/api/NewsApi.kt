package com.training.hearthenews.api

import com.training.hearthenews.models.NewsResponse
import com.training.hearthenews.util.Constants.Companion.API_KEY
import retrofit2.Response

import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun getHeadLines(
        @Query("country")
        countryCode: String = "us",
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey:String=API_KEY
    ): Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q")
        searchQuery: String,
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<NewsResponse>
    @GET("/v2/top-headlines/sources")
    suspend fun getSources(
        @Query("country") countryCode: String = "us",
        @Query("page") pageNumber: Int = 1,
        @Query("apiKey") apiKey: String = API_KEY

    ): Response<NewsResponse>///sources

//    @GET("v2/top-headlines/sources?country=us&category=business")//v2/top-headlines?country=us&category=business
//    suspend fun getBusinessNews(
//        @Query("page")
//        pageNumber: Int = 1,
//        @Query("apiKey") apiKey: String = API_KEY
//    ): Response<NewsResponse>
//
//    @GET("v2/top-headlines/sources?country=us&category=sports")
//    suspend fun getSportsNews(
//        @Query("page")
//        pageNumber: Int = 1,
//        @Query("apiKey") apiKey: String = API_KEY
//    ): Response<NewsResponse>
//
//    @GET("v2/top-headlines/sources?country=us&category=health")
//    suspend fun getHealthNews(
//        @Query("page")
//        pageNumber: Int = 1,
//        @Query("apiKey") apiKey: String = API_KEY
//    ): Response<NewsResponse>
//
//    @GET("v2/top-headlines/sources?country=us&category=technology")
//    suspend fun getTechnologyNews(
//        @Query("page")
//        pageNumber: Int = 1,
//        @Query("apiKey") apiKey: String = API_KEY
//    ): Response<NewsResponse>

    @GET("v2/top-headlines/sources")
    suspend fun getNewsByCategory(
        @Query("country") countryCode: String = "us",
        @Query("category") category: String,
        @Query("page") pageNumber: Int = 1,
        @Query("apiKey") apiKey: String = API_KEY
    ): Response<NewsResponse>

}