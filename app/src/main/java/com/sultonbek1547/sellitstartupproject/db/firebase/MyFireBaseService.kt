package com.sultonbek1547.sellitstartupproject.db.firebase

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.sultonbek1547.sellitstartupproject.db.MyConstants
import com.sultonbek1547.sellitstartupproject.models.MyProduct
import com.sultonbek1547.sellitstartupproject.models.User
import com.sultonbek1547.sellitstartupproject.utils.MySharedPreference
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class MyFireBaseService {
    private val productsReference = Firebase.firestore.collection("products")
    private val usersReference = Firebase.firestore.collection("users")
    private val imagesReference = FirebaseStorage.getInstance().getReference("productImages")

    fun postProduct(product: MyProduct) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val docRef = productsReference.add(product).await()
            val productId = docRef.id
            // Update the product object with the generated ID
            val updatedProduct = product.copy(productId = productId)
            // Update the document in Firestore with the updated product object
            productsReference.document(productId).set(updatedProduct).await()
        } catch (e: Exception) {
            Log.e("FirebaseException", "postProduct: $e")
        }

    }


    suspend fun postImageToStorage(id: String, uri: Uri): String =
        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            val imageUrlDeferred = CompletableDeferred<String>()
            try {
                val task = imagesReference.child(id).putFile(uri)
                task.addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata?.reference?.downloadUrl?.addOnSuccessListener {
                        // link of image is ready
                        val imageUrl = it.toString()
                        imageUrlDeferred.complete(imageUrl)
                    }
                }
                task.await()
            } catch (e: Exception) {
                Log.e("FirebaseException", "getProductsAsync: $e")
            }
            imageUrlDeferred.await()
        }

    suspend fun getALLProducts(): LiveData<ArrayList<MyProduct>> =
        withContext(Dispatchers.IO) {
            try {
                val querySnapshot = productsReference.get().await()
                val listOfProducts = MutableLiveData<ArrayList<MyProduct>>(ArrayList())
                for (document in querySnapshot.documents) {
                    document.toObject(MyProduct::class.java)?.let {
                        listOfProducts.value!!.add(it)
                    }
                }
                return@withContext listOfProducts
            } catch (e: Exception) {
                Log.e("FirebaseException", "getProductsAsync: $e")
                return@withContext MutableLiveData<ArrayList<MyProduct>>()
            }
        }

    fun updateUser(user: User) = CoroutineScope(Dispatchers.IO).launch {
        user.uid?.let { usersReference.document(it).set(user) }
    }


    fun getUsersFromFirebaseAsList() =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot = usersReference.get().await()
                MyConstants.userList = ArrayList<User>()
                for (document in querySnapshot.documents) {
                    document.toObject(User::class.java)?.let {
                        MyConstants.userList.add(it)
                    }
                }
            } catch (e: Exception) {
                Log.e("FirebaseException", "getProductsAsync: $e")
            }

        }


    fun deleteProduct(product: MyProduct, user: User) = CoroutineScope(Dispatchers.IO).launch {
        if (MyConstants.likedProductsList!!.contains(product)) {
            MyConstants.likedProductsList!!.remove(
                product
            )
        }
        user.likedProducts = Gson().toJson(MyConstants.likedProductsList)
        updateUser(user)
        MySharedPreference.user = user
        productsReference.document(product.productId).delete().await()
    }
}



