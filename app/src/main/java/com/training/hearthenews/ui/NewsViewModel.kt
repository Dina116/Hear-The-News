package com.training.hearthenews.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
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
    val newsLiveData: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val sourcesLiveData: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()


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
    fun getSource(countryCode: String) = viewModelScope.launch {
        getSources(countryCode)
    }
    fun getNewsByCategory(category: String) = viewModelScope.launch {
        fetchNews(category, "us")
    }

    private suspend fun fetchNews(category: String, countryCode: String) {
        newsLiveData.postValue(Resource.Loading())
        try {
            if (isInternetAvailable()) {
                val response = newsRepository.getNewsByCategory(category, "us", headlinesPage)
                newsLiveData.postValue(handleHeadLineResponse(response))
                Log.d("NewsViewModel", "Response: ${response.body()}")
            } else {
                showNoInternetError()
            }
        } catch (t: Throwable) {
            handleError(t)
        }
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
    private suspend fun getSources(countryCode: String){
        sourcesLiveData.postValue(Resource.Loading())
        try {
            if(internetConnection(this.getApplication())){
                val response=newsRepository.getSources(countryCode,headlinesPage)
                sourcesLiveData.postValue(handleHeadLineResponse(response))
            }else{
                sourcesLiveData.postValue(Resource.Error("No internet connection"))
                Toast.makeText(getApplication(), "No internet connection", Toast.LENGTH_SHORT).show()
            }

        }catch (t:Throwable){
            when(t){
                is IOException -> sourcesLiveData.postValue(Resource.Error("Network Failure"))
                else -> sourcesLiveData.postValue(Resource.Error("Conversion Error"))
            }
        }


    }
    private fun handleSourcesResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        return if (response.isSuccessful) {
            response.body()?.let { result ->
                Resource.Success(result)
            } ?: Resource.Error("Empty response")
        } else {
            Resource.Error(response.message())
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
    private fun isInternetAvailable(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    }
    private fun showNoInternetError() {
        newsLiveData.postValue(Resource.Error("No internet connection"))
        Toast.makeText(getApplication(), "No internet connection", Toast.LENGTH_SHORT).show()
    }
    private fun handleError(t: Throwable) {
        when (t) {
            is IOException -> newsLiveData.postValue(Resource.Error("Network Failure"))
            else -> newsLiveData.postValue(Resource.Error("Conversion Error"))
        }
    }



}