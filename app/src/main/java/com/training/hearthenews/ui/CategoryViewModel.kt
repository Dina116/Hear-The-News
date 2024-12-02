package com.training.hearthenews.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CategoryViewModel : ViewModel() {

    private val _categoryImage = MutableLiveData<Int>()
    val categoryImage: LiveData<Int> = _categoryImage
    fun updateCategoryImage(imageResId: Int) {
        _categoryImage.value = imageResId
    }

}