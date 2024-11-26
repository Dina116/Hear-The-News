package com.training.hearthenews.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.training.hearthenews.models.Article
import com.training.hearthenews.models.NewsResponse
import com.training.hearthenews.repository.NewsRepository
import com.training.hearthenews.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException


class NewsViewModel(app: Application, val newsRepository: NewsRepository): AndroidViewModel(app) {
    val headlines:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var headlinesPage = 1
    var headlinesResponse: NewsResponse? = null

    val searchNews:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchPage = 1
    var searchResponse: NewsResponse? = null
    var newSearchQuery:String? = null
    var oldSearchQuery:String? = null
    init {
        getHeadLines("us")
    }

    fun getHeadLines(countryCode: String) = viewModelScope.launch {
        headLinesInternet(countryCode)

    }
    fun searchNews(searchQuery: String) = viewModelScope.launch {
        searchNewsInternet(searchQuery)
    }
    fun getNewsByCategory(category: String) = viewModelScope.launch {
        when (category) {
            "Technology" -> fetchTechnologyNews()
            "Sports" -> fetchSportsNews()
            "Health" -> fetchHealthNews()
            "Business" -> fetchBusinessNews()
            else -> fetchHeadLines("us")
        }
    }
    private suspend fun fetchTechnologyNews() {
        val response = newsRepository.getTechnologyNews(headlinesPage)
        if(response.isSuccessful && response.body()?.articles.isNullOrEmpty()){
            headlines.postValue(handleHeadLineResponse(response))
            Toast.makeText(getApplication(), "Response is successful but articles list is empty", Toast.LENGTH_SHORT).show()

        }

    }

    private suspend fun fetchSportsNews() {
        val response = newsRepository.getSportsNews(headlinesPage)
        if(response.isSuccessful ){
            headlines.postValue(handleHeadLineResponse(response))
            Toast.makeText(getApplication(), "Response is successful but articles list is empty", Toast.LENGTH_SHORT).show()
        }
//        else if(response.isSuccessful && response.body()?.articles.isNullOrEmpty()){
//
//        }

    }

    private suspend fun fetchHealthNews() {
        val response = newsRepository.getHealthNews(headlinesPage)
        if(response.isSuccessful && response.body()?.articles.isNullOrEmpty()){
            Toast.makeText(getApplication(), "Response is successful but articles list is empty", Toast.LENGTH_SHORT).show()
        }
//        else if(response.isSuccessful ){
//            headlines.postValue(handleHeadLineResponse(response))
//        }

    }

    private suspend fun fetchBusinessNews() {
        val response = newsRepository.getBusinessNews(headlinesPage)
        if(response.isSuccessful && response.body()?.articles.isNullOrEmpty()){
           // headlines.postValue(handleHeadLineResponse(response))
            Toast.makeText(getApplication(), "Response is successful but articles list is empty", Toast.LENGTH_SHORT).show()
        }
    }

    private suspend fun fetchHeadLines(countryCode: String) {
        val response = newsRepository.getHeadLines(countryCode, headlinesPage)
        //headlines.postValue(handleHeadLineResponse(response))
    }

    private fun handleHeadLineResponse(response: Response<NewsResponse>):Resource<NewsResponse> {

        if(response.isSuccessful){
            response.body()?.let { resultResponse->
                headlinesPage++
                if(headlinesResponse==null){
                    headlinesResponse=resultResponse
                }else{
                    val oldArticles=headlinesResponse?.articles
                    val newArticles=resultResponse.articles
                    if(!oldArticles.isNullOrEmpty() && !newArticles.isNullOrEmpty()){
                        oldArticles?.addAll(newArticles)
                    }
                    else{
                        Log.e("NewsViewModel", "Error: oldArticles or newArticles is null or empty")
                    }

                }
                return Resource.Success(headlinesResponse?:resultResponse)



            }

        }
        return Resource.Error(response.message())

    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>):Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse->
                searchPage++
                if(searchResponse==null || newSearchQuery != oldSearchQuery){
                    searchPage=1
                    oldSearchQuery=newSearchQuery
                    searchResponse=resultResponse

                }else{
                    val oldArticles=searchResponse?.articles
                    val newArticles=resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(searchResponse?:resultResponse)



            }

        }
        return Resource.Error(response.message())
    }

    fun addToFavourites(article: Article)=viewModelScope.launch{
        newsRepository.upsert(article)

    }
    fun getFavouriteNews()=newsRepository.getFavouriteNews()

    fun deleteArticle(article: Article)=viewModelScope.launch{
        newsRepository.deleteNews(article)
    }

    fun internetConnection(context: Context): Boolean {
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).apply {
            return getNetworkCapabilities(activeNetwork)?.run {
                when {
                    hasTransport(android.net.NetworkCapabilities.TRANSPORT_CELLULAR) ->true
                    hasTransport(android.net.NetworkCapabilities.TRANSPORT_WIFI) ->true
                    hasTransport(android.net.NetworkCapabilities.TRANSPORT_ETHERNET) ->true
                    else ->false

                }

            }?:false
        }
    }

    private suspend fun headLinesInternet(countryCode: String){
        headlines.postValue(Resource.Loading())
        try {
            if(internetConnection(this.getApplication())){
                val response=newsRepository.getHeadLines(countryCode,headlinesPage)
                headlines.postValue(handleHeadLineResponse(response))
            }else{
                headlines.postValue(Resource.Error("No internet connection"))
                Toast.makeText(getApplication(), "No internet connection", Toast.LENGTH_SHORT).show()
            }
        }catch (t:Throwable){
            when(t){
                is IOException -> headlines.postValue(Resource.Error("Network Failure"))
                else -> headlines.postValue(Resource.Error("Conversion Error"))
            }

        }

    }

    private suspend fun searchNewsInternet(searchQuery: String){
        newSearchQuery=searchQuery
        searchNews.postValue(Resource.Loading())
        try {
            if(internetConnection(this.getApplication())){
                val response=newsRepository.searchNews(searchQuery,searchPage)
                searchNews.postValue(handleSearchNewsResponse(response))
            }else{
                searchNews.postValue(Resource.Error("No internet connection"))
                Toast.makeText(getApplication(), "No internet connection", Toast.LENGTH_SHORT).show()

        }}catch (t:Throwable){
            when(t){
                is IOException->searchNews.postValue(Resource.Error("Network Failure"))
                else->searchNews.postValue(Resource.Error("Conversion Error"))

            }
        }


    }



}