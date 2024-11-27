package com.training.hearthenews.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.training.hearthenews.R
import com.training.hearthenews.models.Category
import com.training.hearthenews.models.RandomItem
import kotlin.random.Random

class CategoryListAdapter(
    private val categoryList: List<Category>,
    private val itemClickListener: (Category) -> Unit
) : RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder>() {
    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryNameTextView: TextView = itemView.findViewById(R.id.category_name_tv)

        fun bind(category: Category) {
            categoryNameTextView.text = category.name
            itemView.setOnClickListener {
                itemClickListener(category)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.news_item, parent, false)
        return CategoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val currentCategory = categoryList[position]
        holder.bind(currentCategory)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }
    fun updateData(newCategoryList: List<Category>) {
        (categoryList as ArrayList).clear()
        categoryList.addAll(newCategoryList)
        notifyDataSetChanged()
    }


}
