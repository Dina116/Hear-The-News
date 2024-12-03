package com.training.hearthenews.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.PixelCopy
import okhttp3.Request
import androidx.fragment.app.Fragment
import android.view.View
import android.webkit.WebViewClient
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.gson.Gson
import com.training.hearthenews.R
import com.training.hearthenews.adapters.CategoriesAdapter
import com.training.hearthenews.adapters.NewsAdapter
import com.training.hearthenews.adapters.RandomItemAdapter
//import com.training.hearthenews.databinding.FragmentArticleBinding
import com.training.hearthenews.databinding.FragmentCategoryBinding
import com.training.hearthenews.databinding.NewsItemBinding
import com.training.hearthenews.db.ArticleDataBase
import com.training.hearthenews.models.Article
import com.training.hearthenews.models.NewsResponse
import com.training.hearthenews.models.RandomItem
import com.training.hearthenews.repository.NewsRepository
import com.training.hearthenews.ui.MainActivity
import com.training.hearthenews.ui.NewsViewModel
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.IOException


class CategoryFragment : Fragment(R.layout.fragment_category) {

    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var newsRepository: NewsRepository
    private lateinit var randomAdapter:RandomItemAdapter
    private val articles: MutableList<Article> = mutableListOf()
    private val category = "Technology"
    private lateinit var  binding: NewsItemBinding
    private val args: CategoryFragmentArgs by navArgs()
    private val random = listOf(
        RandomItem(R.drawable.animal, "Animals"),
        RandomItem(R.drawable.difficulty, "Difficulty"),
        RandomItem(R.drawable.happiness, "Happiness"),
        RandomItem(R.drawable.support, "Support"),
        RandomItem(R.drawable.help, "Help")

    )
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        FirebaseApp.initializeApp(requireContext())
        super.onViewCreated(view, savedInstanceState)



        categoryRecyclerView = view.findViewById(R.id.category_list_rv)
        newsViewModel = (activity as MainActivity).newsViewModel
        val layoutManager = LinearLayoutManager(context)
        categoryRecyclerView.layoutManager = layoutManager

        newsAdapter = NewsAdapter(this@CategoryFragment, articles, newsViewModel)
        categoryRecyclerView.adapter = newsAdapter
        newsViewModel = (activity as MainActivity).newsViewModel
        newsRepository = NewsRepository(ArticleDataBase(requireContext()))

        randomAdapter = RandomItemAdapter(random)
        categoryRecyclerView.adapter = randomAdapter

            fetchNewsForCategory(category)
//        fetchNewsForCategory(args.categoryName)

    }

    private fun fetchNewsForCategory(category: String) {
        lifecycleScope.launch {
            try {
                val response= newsRepository.getNewsByCategory(category = category,"us",1)
                if (response.isSuccessful) {
                    Log.d("API Response", response.body().toString())
                    val articlesResponse = response.body()
                    articles.clear()
                    if (!articlesResponse?.articles.isNullOrEmpty()){
                        articles.addAll(articlesResponse?.articles ?: emptyList())
                        newsAdapter.notifyDataSetChanged()
                    }
                    else {
                        Log.w("Empty Articles", "No articles found for category: $category")
                        //Snackbar.make(requireView(), "No articles found for this category.", Snackbar.LENGTH_LONG).show()
                    }

                } else {
                    Log.e("API Error", "Code: ${response.code()}, Message: ${response.message()}")
                    //Snackbar.make(requireView(), "Failed to fetch news.", Snackbar.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
               // Snackbar.make(requireView(), "An error occurred: ${e.message}", Snackbar.LENGTH_LONG).show()
            }
        }
    }



}