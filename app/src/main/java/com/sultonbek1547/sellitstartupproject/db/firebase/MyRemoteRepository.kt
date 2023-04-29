package com.sultonbek1547.sellitstartupproject.db.firebase

import androidx.lifecycle.LiveData
import com.sultonbek1547.sellitstartupproject.models.MyProduct

class MyRemoteRepository(private val myFireBaseService: MyFireBaseService) {
    suspend fun getAllProducts(): LiveData<ArrayList<MyProduct>> =
        myFireBaseService.getALLProducts()
}