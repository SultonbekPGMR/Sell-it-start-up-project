package com.sultonbek1547.sellitstartupproject.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sultonbek1547.sellitstartupproject.R
import com.sultonbek1547.sellitstartupproject.utils.checkIsUserSignedIn

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
           if (!checkIsUserSignedIn()) {
            startActivity(Intent(this, LogInActivity::class.java))

        }
    }
}