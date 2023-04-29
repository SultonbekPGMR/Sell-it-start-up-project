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

fun Fragment.uploadLikedProductIdToList(id: String) {
    MySharedPreference.user = MySharedPreference.user?.apply {
        val type = object : TypeToken<ArrayList<String>>() {}.type
        val list = Gson().fromJson<ArrayList<String>>(likedProductIds, type)
        list.add(id)
        likedProductIds = Gson().toJson(list)
    }
    MyFireBaseService().updateUser(MySharedPreference.user!!)
    loadLikedProductsList()
}

fun Fragment.removeLikedProductIdFromList(id: String) {
    MySharedPreference.user = MySharedPreference.user?.apply {
        val type = object : TypeToken<ArrayList<String>>() {}.type
        val list = Gson().fromJson<ArrayList<String>>(likedProductIds, type)
        list.remove(id)
        likedProductIds = Gson().toJson(list)
    }
    MyFireBaseService().updateUser(MySharedPreference.user!!)
    loadLikedProductsList()
}


fun Fragment.loadLikedProductsList() {
    MySharedPreference.init(requireContext())
    val type = object : TypeToken<ArrayList<String>>() {}.type
    MyConstants.likedProductIdsList =
        Gson().fromJson<ArrayList<String>>(MySharedPreference.user?.likedProductIds, type)
}