package com.training.hearthenews.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.training.hearthenews.databinding.CategoryItemBinding
import com.training.hearthenews.models.Category
import com.training.hearthenews.ui.CategoryViewModel

class CategoriesAdapter(
    val fragment: Fragment,
    private val categories: List<Category>,
    //private val listener: OnCategoryClickListener
  private val onItemClick: (Category) -> Unit
) :
    RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {
    inner class CategoryViewHolder(val binding: CategoryItemBinding) :
       RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category) {
            val category = categories[position]
            binding.categoryIv.setImageResource(category.categoryImgId)
            binding.categoryNameTv.text = categories[position].name

//            itemView.setOnClickListener {
//                onItemClick(category)
//            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoriesAdapter.CategoryViewHolder {
        val binding =
            CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.bind(category)
        holder.itemView.setOnClickListener {
           onItemClick(category) // عند الضغط على الـ Category
        }
    }

    override fun getItemCount(): Int = categories.size

    interface OnCategoryClickListener {
        fun onCategoryClick(category: Category)
    }


}