package com.training.hearthenews.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.training.hearthenews.R
import com.training.hearthenews.adapters.NewsAdapter
import com.training.hearthenews.databinding.FragmentTrendsBinding
import com.training.hearthenews.models.Article
import com.training.hearthenews.ui.MainActivity
import com.training.hearthenews.ui.NewsViewModel
import com.training.hearthenews.util.Constants
import com.training.hearthenews.util.Resource

class TrendsFragment : Fragment() {

    private lateinit var binding: FragmentTrendsBinding
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var sourcesAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrendsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newsViewModel = (activity as MainActivity).newsViewModel

        setupRecyclerView()
        observeSources()

        newsViewModel.getSource("us")
    }
    private fun setupRecyclerView() {
        sourcesAdapter = NewsAdapter(
            fragment = this@TrendsFragment,
            articles = ArrayList<Article>(),
            newsViewModel
        )
        binding.rvSources.apply {
            adapter = sourcesAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
    var isLastPage = false
    private fun observeSources() {
        newsViewModel.sourcesLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    hideProgressBar()
                    resource.data?.let { sourcesResponse ->
                        val totalPages =
                            sourcesResponse.totalResults / Constants.QUERY_PAGE_SIZE + 2
                        isLastPage = newsViewModel.headlinesPage == totalPages
                        if (isLastPage) {
                            binding.rvSources.setPadding(0, 0, 0, 0)
                        }
                        sourcesAdapter.articles.clear()
                        sourcesAdapter.articles.addAll(sourcesResponse.articles)
                        sourcesAdapter.notifyDataSetChanged()
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    resource.message?.let { message ->
                        Toast.makeText(requireContext(), "error!!", Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading -> showProgressBar()
            }
        }
    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
    }


}