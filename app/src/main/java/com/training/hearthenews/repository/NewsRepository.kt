package com.training.hearthenews.repository


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
    suspend fun getNewsByCategory(category: String, countryCode: String, pageNumber: Int): Response<NewsResponse> {
        return RetrofitInstance.api.getNewsByCategory(category = category, countryCode = countryCode,pageNumber = pageNumber)
    }

//suspend fun getBusinessNews(pageNumber: Int): Response<NewsResponse> {
//    return RetrofitInstance.api.getBusinessNews(pageNumber)
//}
//
//    suspend fun getSportsNews(pageNumber: Int): Response<NewsResponse> {
//        return RetrofitInstance.api.getSportsNews(pageNumber)
//    }
//
//    suspend fun getHealthNews(pageNumber: Int): Response<NewsResponse> {
//        return RetrofitInstance.api.getHealthNews(pageNumber)
//    }
//
//    suspend fun getTechnologyNews(pageNumber: Int): Response<NewsResponse> {
//        return RetrofitInstance.api.getTechnologyNews(pageNumber)
//    }

    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    fun getFavouriteNews() = db.getArticleDao().getAllArticles()
    suspend fun deleteNews(article: Article) = db.getArticleDao().deleteArticle(article)


}