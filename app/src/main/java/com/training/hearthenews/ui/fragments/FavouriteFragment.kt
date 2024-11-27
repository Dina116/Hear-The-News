package com.training.hearthenews.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.training.hearthenews.R
import com.training.hearthenews.adapters.NewsAdapter
import com.training.hearthenews.databinding.FragmentFavouriteBinding
import com.training.hearthenews.models.Article
import com.training.hearthenews.ui.MainActivity
import com.training.hearthenews.ui.NewsViewModel


class FavouriteFragment : Fragment(R.layout.fragment_favourite) {
    lateinit var newsViewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    lateinit var binding: FragmentFavouriteBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavouriteBinding.bind(view)

        newsViewModel = (activity as MainActivity).newsViewModel
        setUpFavouriteRecycler()

        newsAdapter.setOnItemClickListener {
            val action = FavouriteFragmentDirections.actionFavouriteFragmentToArticleFragment()
            findNavController().navigate(action)
        }
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.articles[position]
                newsViewModel.deleteArticle(article)
                Snackbar.make(view, "Removed From Favourite Successfully", Snackbar.LENGTH_LONG)
                    .apply {
                        setAction("Undo"){
                            newsViewModel.addToFavourites(article)
                        }
                        show()

                    }

            }

        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.recyclerFavourites)
        }
        newsViewModel.getFavouriteNews().observe(viewLifecycleOwner, Observer {
            newsAdapter.articles.clear()
            newsAdapter.articles.addAll(it)
            newsAdapter.notifyDataSetChanged()
        })



    }

    private fun setUpFavouriteRecycler() {
        newsAdapter =
            NewsAdapter(fragment = this@FavouriteFragment, articles = ArrayList<Article>(), newsViewModel)
        binding.recyclerFavourites.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }


}