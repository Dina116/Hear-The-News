package com.training.hearthenews.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.training.hearthenews.databinding.CategoryItemBinding
import com.training.hearthenews.databinding.NewsItemBinding
import com.training.hearthenews.models.Category
import com.training.hearthenews.models.RandomItem

class RandomItemAdapter(private val randomItems: List<RandomItem>) :
    RecyclerView.Adapter<RandomItemAdapter.ViewHolder>() {
    class ViewHolder(val binding: NewsItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(randomItem: RandomItem) {
            val random = randomItem
            binding.articleIv.setImageResource(random.imageResId)
            binding.articleText.text = random.name
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RandomItemAdapter.ViewHolder {
        val binding =
            NewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RandomItemAdapter.ViewHolder, position: Int) {
        val random = randomItems[position]
        holder.bind(random)
    }

    override fun getItemCount(): Int = randomItems.size
}