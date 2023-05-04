package com.sultonbek1547.sellitstartupproject.utils

import android.app.Activity
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sultonbek1547.sellitstartupproject.db.MyConstants
import com.sultonbek1547.sellitstartupproject.db.firebase.MyFireBaseService
import com.sultonbek1547.sellitstartupproject.models.MyProduct
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainUtils {

}

private val auth = FirebaseAuth.getInstance()
fun Activity.checkIsUserSignedIn(): Boolean {

    MySharedPreference.init(this)
    if (MySharedPreference.deviceToken!!.isEmpty()) {
        MySharedPreference.init(this)
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            MySharedPreference.deviceToken = task.result
        })
    }// checking for token is taken or not


    return MySharedPreference.isUserAuthenticated!!
}

fun Activity.showToast(toastMessage: String) {
    Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()

}

fun Fragment.showToast(toastMessage: String) {
    Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
}

fun Fragment.uploadLikedProductToList(product: MyProduct) =
    CoroutineScope(Dispatchers.Default).launch {
        MySharedPreference.user = MySharedPreference.user?.apply {
            val gson = Gson()
            val type = object : TypeToken<ArrayList<MyProduct>>() {}.type
            val list: ArrayList<MyProduct> =
                gson.fromJson<ArrayList<MyProduct>>(likedProducts, type) ?: ArrayList()
            list.add(product)
            likedProducts = Gson().toJson(list)
        }
        MyFireBaseService().updateUser(MySharedPreference.user!!)
        loadLikedProductsList()
    }


fun Fragment.removeLikedProductFromList(product: MyProduct) =
    CoroutineScope(Dispatchers.Default).launch {
        MySharedPreference.user = MySharedPreference.user?.apply {
            val gson = Gson()
            val type = object : TypeToken<ArrayList<MyProduct>>() {}.type
            val list: ArrayList<MyProduct> =
                gson.fromJson<ArrayList<MyProduct>>(likedProducts, type) ?: ArrayList()
            list.remove(product)
            likedProducts = Gson().toJson(list)
        }
       MyFireBaseService().updateUser(MySharedPreference.user!!)
        loadLikedProductsList()
    }


fun Fragment.loadLikedProductsList() =
    CoroutineScope(Dispatchers.IO).launch {
        MySharedPreference.init(requireContext())
        val type = object : TypeToken<ArrayList<MyProduct>>() {}.type
        MyConstants.likedProductsList =
            Gson().fromJson<ArrayList<MyProduct>>(MySharedPreference.user?.likedProducts, type)
    }
