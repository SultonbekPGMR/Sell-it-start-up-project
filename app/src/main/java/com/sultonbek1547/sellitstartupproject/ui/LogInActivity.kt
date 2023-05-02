package com.sultonbek1547.sellitstartupproject.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.sultonbek1547.sellitstartupproject.R
import com.sultonbek1547.sellitstartupproject.databinding.ActivityLogInBinding
import com.sultonbek1547.sellitstartupproject.db.MyConstants
import com.sultonbek1547.sellitstartupproject.db.firebase.MyFireBaseService
import com.sultonbek1547.sellitstartupproject.models.MyProduct
import com.sultonbek1547.sellitstartupproject.models.User
import com.sultonbek1547.sellitstartupproject.utils.MySharedPreference
import com.sultonbek1547.sellitstartupproject.utils.showToast
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "LogInActivity"

class LogInActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLogInBinding.inflate(layoutInflater) }
    private val auth = FirebaseAuth.getInstance()
    private val usersReference = Firebase.firestore.collection("users")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        MySharedPreference.init(this)
        MyFireBaseService().getUsersFromFirebaseAsList()

        binding.apply {
            btnContinueWithGoogle.setOnClickListener {
                continueWithGoogle()
            }

            btnLogIn.setOnClickListener {
                val email = edtEmail.text.toString()
                val password = edtPassword.text.toString()
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    MyConstants.userList
                        .forEach {
                        if (it.email.equals(email) && it.password.equals(password)) {
                            // email and password are correct
                            MySharedPreference.isUserAuthenticated = true
                            MySharedPreference.user = it
                            startActivity(Intent(this@LogInActivity, MainActivity::class.java))
                            return@setOnClickListener
                        }
                    }
                    showToast("email or password is incorrect")
                } else {
                    if (email.isEmpty()) edtEmail.error = "fill out"
                    if (password.isEmpty()) edtPassword.error = "fill out"
                }


            }
        }

    }


    private fun continueWithGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail().build()
        val googleSigningClient = GoogleSignIn.getClient(this, gso)

        startActivityForResult(googleSigningClient.signInIntent, 1)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == 1) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                Log.d(TAG, "onActivityResult: ${account.displayName}")
                fireBaseAuthWithGoogle(account.idToken)

            } catch (e: Exception) {
                Log.d(TAG, "onActivityResult: FAILURE ${e.toString()}")
            }

        }
    }

    private fun fireBaseAuthWithGoogle(idToken: String?) {
        CoroutineScope(IO).launch {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential).addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d(TAG, "fireBaseAuthWithGoogle: Sign in with credential SUCCESSFUL")
                    binding.edtEmail.setText(auth.currentUser?.email)
                    binding.textInputLayoutPassword.hint = "password for account"
                    binding.btnSignUp.visibility = View.VISIBLE
                    binding.btnLogIn.visibility = View.GONE
                    binding.btnContinueWithGoogle.visibility = View.GONE
                    var password = ""
                    binding.btnSignUp.setOnClickListener {
                        password = binding.edtPassword.text.toString()
                        if (password.isNotEmpty()) {
                            saveNewUserToFirebase()
                        }
                    }

                } else {
                    Log.d(TAG, "fireBaseAuthWithGoogle: Sign in with credential FAILED")
                }
            }

        }
    }

    private fun saveNewUserToFirebase() {
        val authUser = auth.currentUser
        val user = User(
            auth.uid,
            authUser?.displayName,
            authUser?.photoUrl.toString(),
            auth.currentUser?.email,
            binding.edtPassword.text.toString(),
            SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
            "true",
            MySharedPreference.deviceToken!!,
            Gson().toJson(ArrayList<String>())
        )
        usersReference.document(auth.uid!!).set(user).addOnSuccessListener {
            MySharedPreference.isUserAuthenticated = true
            MySharedPreference.user = user
            startActivity(Intent(this@LogInActivity, MainActivity::class.java))
        }
    }

}