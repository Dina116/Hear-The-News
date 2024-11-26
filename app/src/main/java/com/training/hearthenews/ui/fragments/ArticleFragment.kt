package com.training.hearthenews.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.training.hearthenews.R
import com.training.hearthenews.databinding.FragmentArticleBinding
import com.training.hearthenews.ui.MainActivity
import com.training.hearthenews.ui.NewsViewModel


class ArticleFragment : Fragment(R.layout.fragment_article) {

    lateinit var newsViewModel: NewsViewModel
    val args:ArticleFragmentArgs by navArgs()
    val article = args.article
    lateinit var binding: FragmentArticleBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val myArg=args.myArg
        binding= FragmentArticleBinding.bind(view)
        newsViewModel=(activity as MainActivity).newsViewModel
        val article=args.article
        if(article!=null){
            binding.webView.apply {
                webViewClient= WebViewClient()
                article.url?.let {
                    loadUrl(it)
                }
        }
            binding.fab.setOnClickListener {
                newsViewModel.addToFavourites(article)
                Snackbar.make(view,"Add to favourites successfully",Snackbar.LENGTH_SHORT).show()

            }
            newsViewModel.addToFavourites(article)
        }else{
            Snackbar.make(view, "Article data is missing", Snackbar.LENGTH_SHORT).show()
        }



    }

}