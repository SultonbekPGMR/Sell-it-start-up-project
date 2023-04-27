package com.sultonbek1547.sellitstartupproject.db.firebase

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.sultonbek1547.sellitstartupproject.models.MyProduct
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class MyFireBaseService {
    private val productsReference = Firebase.firestore.collection("products")
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


    fun getProductsAsync(): Deferred<ArrayList<MyProduct>> = CoroutineScope(Dispatchers.IO).async {
        getALLProducts()
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

    private suspend fun getALLProducts(): ArrayList<MyProduct> =
        withContext(Dispatchers.IO) {
            try {
                val querySnapshot = productsReference.get().await()
                val listOfProducts = ArrayList<MyProduct>()
                for (document in querySnapshot.documents) {
                    document.toObject(MyProduct::class.java)?.let {
                        listOfProducts.add(it)
                    }
                }
                return@withContext listOfProducts
            } catch (e: Exception) {
                Log.e("FirebaseException", "getProductsAsync: $e")
                return@withContext ArrayList<MyProduct>()
            }
        }

}
