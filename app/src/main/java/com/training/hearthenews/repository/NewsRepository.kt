package com.training.hearthenews.repository

import android.content.Context
import android.net.ConnectivityManager
import com.training.hearthenews.api.RetrofitInstance
import com.training.hearthenews.db.ArticleDataBase
import com.training.hearthenews.models.Article
import com.training.hearthenews.models.NewsResponse
import retrofit2.Response

class NewsRepository(val db: ArticleDataBase) {

    suspend fun getHeadLines(countryCode: String, pageNumber: Int): Response<NewsResponse> {
        return RetrofitInstance.api.getHeadLines(countryCode, pageNumber)

    }

    suspend fun searchNews(searchQuery: String, pageNumber: Int): Response<NewsResponse> {
        return  RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

    }

//    suspend fun getBusinessNews(pageNumber: Int) {
//        RetrofitInstance.api.getBusinessNews(pageNumber)
//
//    }
//
//    suspend fun getSportsNews(pageNumber: Int) {
//        RetrofitInstance.api.getSportsNewss(pageNumber)
//
//    }
//
//    suspend fun getHealthNews(pageNumber: Int) {
//        RetrofitInstance.api.getHealthNewss(pageNumber)
//
//    }
//
//    suspend fun getTechnologyNews(pageNumber: Int) {
//        RetrofitInstance.api.getTechnologyNewss(pageNumber)
//
//    }
suspend fun getBusinessNews(pageNumber: Int): Response<NewsResponse> {
    return RetrofitInstance.api.getBusinessNews(pageNumber)
}

    suspend fun getSportsNews(pageNumber: Int): Response<NewsResponse> {
        return RetrofitInstance.api.getSportsNews(pageNumber)  // تم تصحيح الاسم هنا
    }

    suspend fun getHealthNews(pageNumber: Int): Response<NewsResponse> {
        return RetrofitInstance.api.getHealthNews(pageNumber)  // تم تصحيح الاسم هنا
    }

    suspend fun getTechnologyNews(pageNumber: Int): Response<NewsResponse> {
        return RetrofitInstance.api.getTechnologyNews(pageNumber)  // تم تصحيح الاسم هنا
    }

    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    fun getFavouriteNews() = db.getArticleDao().getAllArticles()
    suspend fun deleteNews(article: Article) = db.getArticleDao().deleteArticle(article)


}