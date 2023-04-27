package com.sultonbek1547.sellitstartupproject.utils

import android.app.Activity
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging

class MainUtils {

}

private val auth =  FirebaseAuth.getInstance()
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

}fun Fragment.showToast(toastMessage: String) {
    Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()

}