package com.training.hearthenews.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.launch
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.training.hearthenews.R
import com.training.hearthenews.adapters.CategoriesAdapter
import com.training.hearthenews.adapters.NewsAdapter
import com.training.hearthenews.api.NewsApi
import com.training.hearthenews.databinding.CategoryItemBinding
import com.training.hearthenews.databinding.FragmentCategoryBinding
import com.training.hearthenews.databinding.FragmentHeadLinesBinding
import com.training.hearthenews.models.Article
import com.training.hearthenews.models.Category
import com.training.hearthenews.models.NewsResponse
import com.training.hearthenews.ui.CategoryViewModel
import com.training.hearthenews.ui.MainActivity
import com.training.hearthenews.ui.NewsViewModel
import com.training.hearthenews.util.Constants
import com.training.hearthenews.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class HeadLinesFragment : Fragment(R.layout.fragment_head_lines) {
    lateinit var newsViewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    lateinit var retryButton: Button
    lateinit var errorText: TextView
    lateinit var itemHeadlinesError: CardView
    lateinit var categoriesAdapter: CategoriesAdapter
    lateinit var binding: FragmentHeadLinesBinding
     lateinit var categoryFragment:CategoryFragment
     lateinit var bindingCategory:FragmentCategoryBinding
     lateinit var bindinglist:CategoryItemBinding

    var selectedCategory: String? = null

    private val categories = listOf(
        Category(R.drawable.tech, "Technology"),
        Category(R.drawable.sports, "Sports"),
        Category(R.drawable.business, "Business"),
        Category(R.drawable.health, "Health")
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        FirebaseApp.initializeApp(requireContext())
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHeadLinesBinding.bind(view)
        categoriesAdapter = CategoriesAdapter(
            fragment = this, categories,
            onItemClick = { category ->
                selectedCategory = category.name
                val categoryName = selectedCategory ?:""

                Toast.makeText(activity, "Go to news of category you select", Toast.LENGTH_SHORT).show()
                val action = HeadLinesFragmentDirections.actionHeadLinesFragmentToCategoryFragment(categoryName)
                findNavController().navigate(action)
                loadNewsCategories(selectedCategory)
            },
        )
        binding.categoryRv.adapter = categoriesAdapter


        val inflater =
            requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        itemHeadlinesError = view.findViewById(R.id.itemHeadlinesError)
        //
        val view: View = inflater.inflate(R.layout.item_error, null)



        retryButton = view.findViewById(R.id.retryButton)
        errorText = view.findViewById(R.id.errorText)

        newsViewModel = (activity as MainActivity).newsViewModel
        categoryFragment = CategoryFragment()
        bindingCategory = FragmentCategoryBinding.inflate(layoutInflater)
        bindinglist = CategoryItemBinding.inflate(layoutInflater)

        setUpHeadLinesRecycler()

        newsAdapter.setOnItemClickListener { article ->

            val action =
                HeadLinesFragmentDirections.actionHeadLinesFragmentToCategoryFragment(categoryName = bindinglist.categoryNameTv.text.toString())
            findNavController().navigate(action)
        }
        newsViewModel.headlines.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success<*> -> {
                    hideProgressBar()
                    hideErrorMessage()
                    response.data?.let { newsResponse ->
                        val totalPages = newsResponse.totalResults / Constants.QUERY_PAGE_SIZE + 2
                        isLastPage = newsViewModel.headlinesPage == totalPages
                        if (isLastPage) {
                            binding.recyclerHeadlines.setPadding(0, 0, 0, 0)
                        }
                        newsAdapter.articles.clear()
                        newsAdapter.articles.addAll(newsResponse.articles)
                        newsAdapter.notifyDataSetChanged()
                    }

                }

                is Resource.Error<*> -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity, "Error: $message", Toast.LENGTH_SHORT).show()
                        showErrorMessage(message)
                    }

                }

                is Resource.Loading<*> -> {
                    showProgressBar()
                }

                else -> {}

            }

        })
        retryButton.setOnClickListener {
            newsViewModel.getHeadLines("us")
        }
//        val categories = loadCategories()
//        showCategories(categories)
//        loadNewsCategories(selectedCategory)

    }

    var isError = false
    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideErrorMessage() {
        itemHeadlinesError.visibility = View.INVISIBLE
        isError = false

    }

    private fun showErrorMessage(message: String) {
        itemHeadlinesError.visibility = View.VISIBLE
        errorText.text = message
        isError = true

    }

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNoErrors = !isError
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate =
                isNoErrors && isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                newsViewModel.getHeadLines("us")
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private fun setUpHeadLinesRecycler() {
        newsAdapter =
            NewsAdapter(fragment = this@HeadLinesFragment, articles = ArrayList<Article>(), newsViewModel)
        binding.recyclerHeadlines.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(scrollListener)
        }
    }
    private fun loadNewsCategories(category: String?) {
        category?.let {
            newsAdapter =
                NewsAdapter(fragment =  categoryFragment, articles = ArrayList<Article>(), newsViewModel)
            bindingCategory.categoryListRv.apply {
                adapter = newsAdapter
                layoutManager = LinearLayoutManager(activity)
                addOnScrollListener(scrollListener)
            }
            newsViewModel.getNewsByCategory(it)
        } ?: run {
            newsViewModel.getHeadLines("us")  // في حالة عدم تحديد فئة
        }
    }


    private fun showNews(articles: MutableList<Article>) {
        val newsAdapter = NewsAdapter(this, articles, newsViewModel)
        binding.recyclerHeadlines.adapter = newsAdapter
    }

//private fun showCategories(categories: List<Category>) {
//    categoriesAdapter = CategoriesAdapter(fragment = this, categories = categories, onItemClick = { category ->
//        selectedCategory = category.name
//        loadNewsCategories(selectedCategory)
//    })
//
//    binding.categoryRv.apply {
//        adapter = categoriesAdapter
//    }
//}
//private fun loadCategories(): List<Category> {
//    return listOf(
//        Category(R.drawable.tech,"Technology"),
//        Category( R.drawable.sports,"Sports"),
//        Category( R.drawable.business,"Business"),
//        Category( R.drawable.health,"Health")
//    )
//}
    override fun onResume() {
        super.onResume()
        binding.paginationProgressBar.isVisible = true
        newsViewModel.getHeadLines("us")
    }

    private fun newsCallback(newsType: String): Callback<NewsResponse> {
        return object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                val news = response.body()
                val articles = news?.articles!!
                articles.removeAll { it.title == "[Removed]" }
                showNews(articles)
                // binding.textView1.text = newsType
                // binding.swipeRefresh.isRefreshing = false
                binding.paginationProgressBar.isVisible = false
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                Log.d("trace", "Error: ${t.message}")
            }
        }
    }


}

