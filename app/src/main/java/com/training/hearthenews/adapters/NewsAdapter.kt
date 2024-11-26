package com.training.hearthenews.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.snackbar.Snackbar
import com.training.hearthenews.R
import com.training.hearthenews.databinding.NewsItemBinding
import com.training.hearthenews.models.Article
import com.training.hearthenews.ui.NewsViewModel
import com.training.hearthenews.ui.fragments.ArticleFragmentArgs

class NewsAdapter(val fragment: Fragment, val  articles: MutableList<Article>,private val newsViewModel: NewsViewModel): RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(val binding: NewsItemBinding): RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NewsAdapter.ArticleViewHolder {
        val binding =
            NewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

  private var onItemClickListener:((Article)->Unit)?=null

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }
    override fun onBindViewHolder(holder: NewsAdapter.ArticleViewHolder, position: Int) {
        val article = articles[position] // Access article from the list differ.currentList[position]//
        val url = article.url
        holder.binding.articleText.text = articles[position].title
        Glide
            .with(holder.binding.articleIv.context)
            .load(articles[position].urlToImage)
            .error(R.drawable.broken_image)
            .transition(DrawableTransitionOptions.withCrossFade(1000))
            .into(holder.binding.articleIv)
        holder.binding.articleContainer.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW, url.toUri())
            Toast.makeText(this.fragment.requireContext(), "Article opened successfully in browser", Toast.LENGTH_SHORT).show()
            fragment.startActivity(i)
        }
        holder.binding.favFab.setOnClickListener {
            newsViewModel.addToFavourites(article)
            Snackbar.make(holder.itemView,"Add to favourites successfully", Snackbar.LENGTH_SHORT).show()
        }

        holder.binding.shareFab.setOnClickListener {
            ShareCompat
                .IntentBuilder(fragment.requireContext())
                .setType("text/plain")
                .setChooserTitle("Share with:")
                .setText(url)
                .startChooser()
            Toast.makeText(this.fragment.requireContext(), "Which app do you want to share with?", Toast.LENGTH_SHORT).show()

        }
    }

    override fun getItemCount(): Int=articles.size

    fun setOnItemClickListener(listener: (Article) -> Unit){
        onItemClickListener=listener

    }
    }