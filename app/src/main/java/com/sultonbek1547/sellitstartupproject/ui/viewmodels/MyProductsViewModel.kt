package com.sultonbek1547.sellitstartupproject.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sultonbek1547.sellitstartupproject.db.firebase.MyRemoteRepository
import com.sultonbek1547.sellitstartupproject.models.MyProduct
import kotlinx.coroutines.launch

class MyProductsViewModel(private val myRemoteRepository: MyRemoteRepository) : ViewModel() {
    val productList = MutableLiveData<ArrayList<MyProduct>>()

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            productList.value = myRemoteRepository.getAllProducts().value
        }
    }
}