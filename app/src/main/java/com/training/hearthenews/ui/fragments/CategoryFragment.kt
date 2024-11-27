package com.training.hearthenews.ui.fragments

import android.os.Bundle
import android.view.PixelCopy
import okhttp3.Request
import androidx.fragment.app.Fragment
import android.view.View
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.training.hearthenews.R
import com.training.hearthenews.adapters.CategoriesAdapter
import com.training.hearthenews.adapters.NewsAdapter
import com.training.hearthenews.adapters.RandomItemAdapter
//import com.training.hearthenews.databinding.FragmentArticleBinding
import com.training.hearthenews.databinding.FragmentCategoryBinding
import com.training.hearthenews.databinding.NewsItemBinding
import com.training.hearthenews.models.Article
import com.training.hearthenews.models.NewsResponse
import com.training.hearthenews.models.RandomItem
import com.training.hearthenews.ui.MainActivity
import com.training.hearthenews.ui.NewsViewModel
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.IOException


class CategoryFragment : Fragment(R.layout.fragment_category) {
    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var randomAdapter:RandomItemAdapter
    private val articles: MutableList<Article> = mutableListOf()
    private val category = "Technology"
    private lateinit var  binding: NewsItemBinding
    private val random = listOf(
        RandomItem(R.drawable.animal, "Animals"),
        RandomItem(R.drawable.difficulty, "Difficulty"),
        RandomItem(R.drawable.happiness, "Happiness"),
        RandomItem(R.drawable.support, "Support"),
        RandomItem(R.drawable.help, "Help")

    )
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        categoryRecyclerView = view.findViewById(R.id.category_list_rv)
        newsViewModel = (activity as MainActivity).newsViewModel
        val layoutManager = LinearLayoutManager(context)
        categoryRecyclerView.layoutManager = layoutManager
        randomAdapter = RandomItemAdapter(random)
        categoryRecyclerView.adapter = randomAdapter
        fetchNewsForCategory(category)

    }

    private fun fetchNewsForCategory(category: String) {
        val apiKey = "API_KEY"
        val url = "https://newsapi.org/v2/top-headlines?category=$category&apiKey=$apiKey&page=1"

       val request = Request.Builder().url(url).build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val jsonResponse = response.body?.string()
                    val articlesResponse = Gson().fromJson(jsonResponse, NewsResponse::class.java)
                    activity?.runOnUiThread {
                        if (articlesResponse.articles.isEmpty()) {
                            Snackbar.make(requireView(), "No articles found.", Snackbar.LENGTH_LONG).show()
                        }
                        articles.clear()
                        articles.addAll(articlesResponse.articles)
                        newsAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                activity?.runOnUiThread {
                    Snackbar.make(requireView(), "Failed to load data. Please check your connection.", Snackbar.LENGTH_LONG).show()
                }
            }
        })
    }



}