//package com.training.hearthenews.adapters
//
//import android.content.Intent
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import android.widget.Toast
//import androidx.core.app.ShareCompat
//import androidx.core.net.toUri
//import androidx.recyclerview.widget.AsyncListDiffer
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
//import com.google.android.material.snackbar.Snackbar
//import com.training.hearthenews.R
//import com.training.hearthenews.databinding.NewsItemBinding
//import com.training.hearthenews.models.Source
//
//class SourcesAdapter : RecyclerView.Adapter<SourcesAdapter.SourcesViewHolder>() {
//
//    inner class SourcesViewHolder(private val binding: NewsItemBinding) :
//        RecyclerView.ViewHolder(binding.root)
//
//    private val differCallback = object : DiffUtil.ItemCallback<Source>() {
//        override fun areItemsTheSame(oldItem: Source, newItem: Source): Boolean {
//            return oldItem.id == newItem.id
//        }
//
//        override fun areContentsTheSame(oldItem: Source, newItem: Source): Boolean {
//            return oldItem == newItem
//        }
//    }
//
//    private val differ = AsyncListDiffer(this, differCallback)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SourcesViewHolder {
//        val binding = NewsItemBinding.inflate(
//            LayoutInflater.from(parent.context),
//            parent,
//            false
//        )
//        return SourcesViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: SourcesViewHolder, position: Int) {
//        val source = differ.currentList[position]
//        val article = articles[position] // Access article from the list differ.currentList[position]//
//        val url = article.url
//        holder.binding.articleText.text = articles[position].title
//        Glide
//            .with(holder.binding.articleIv.context)
//            .load(articles[position].urlToImage)
//            .error(R.drawable.broken_image)
//            .transition(DrawableTransitionOptions.withCrossFade(1000))
//            .into(holder.binding.articleIv)
//        holder.binding.articleContainer.setOnClickListener {
//            val i = Intent(Intent.ACTION_VIEW, url.toUri())
//            Toast.makeText(this.fragment.requireContext(), "Article opened successfully in browser", Toast.LENGTH_SHORT).show()
//            fragment.startActivity(i)
//        }
//        holder.binding.favFab.setOnClickListener {
//            newsViewModel.addToFavourites(article)
//            Snackbar.make(holder.itemView,"Add to favourites successfully", Snackbar.LENGTH_SHORT).show()
//        }
//
//        holder.binding.shareFab.setOnClickListener {
//            ShareCompat
//                .IntentBuilder(fragment.requireContext())
//                .setType("text/plain")
//                .setChooserTitle("Share with:")
//                .setText(url)
//                .startChooser()
//            Toast.makeText(this.fragment.requireContext(), "Which app do you want to share with?", Toast.LENGTH_SHORT).show()
//
//        }
//    }
//
//    override fun getItemCount(): Int = differ.currentList.size
//
//    fun submitList(list: List<Source>) {
//        differ.submitList(list)
//    }
//
//
//}
