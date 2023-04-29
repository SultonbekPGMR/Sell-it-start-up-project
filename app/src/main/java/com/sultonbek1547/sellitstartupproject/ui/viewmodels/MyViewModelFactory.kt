package com.sultonbek1547.sellitstartupproject.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sultonbek1547.sellitstartupproject.db.firebase.MyRemoteRepository

class MyViewModelFactory(private val repository: MyRemoteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyProductsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MyProductsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}