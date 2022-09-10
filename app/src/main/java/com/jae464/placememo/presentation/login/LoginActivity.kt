package com.jae464.placememo.presentation.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jae464.placememo.MainActivity
import com.jae464.placememo.R
import com.jae464.placememo.databinding.ActivityLoginBinding
import com.jae464.placememo.presentation.base.BaseActivity

class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {
    private lateinit var client: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private val fireStore = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        setGoogleLogin()
        initListener()
    }

    private fun setGoogleLogin() {
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("599832472009-9n76h9tik9oh4v670hk4tquv75hg56jk.apps.googleusercontent.com")
            .requestEmail().build()
        client = GoogleSignIn.getClient(this, options)

    }

    private fun initListener() {
        binding.loginButton.setOnClickListener {
            startActivityForResult(client.signInIntent, 1)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            var account: GoogleSignInAccount? = null
            try {
                account = task.getResult(ApiException::class.java)
                Log.d("LoginActivity", account.toString())
                firebaseAuthWithGoogle(account!!.idToken)
                Toast.makeText(this, "sucess google login", Toast.LENGTH_SHORT).show()
//                goToMain()
            } catch (e: ApiException) {
                Toast.makeText(this, "Failed Google Login", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this
            ) { task ->
                if (task.isSuccessful) {
                    // 인증에 성공한 후, 현재 로그인된 유저의 정보를 가져올 수 있습니다.
                    val email = auth.currentUser?.email
                    Log.d("LoginActivity", email.toString())
                    val user = hashMapOf(
                        "uid" to auth.currentUser?.uid,
                        "email" to auth.currentUser?.email
                    )
                    // 사용자 조회 후 없으면 저장
                    fireStore.collection("users")
                        .document(auth.currentUser?.uid ?: "")
                        .get()
                        .addOnSuccessListener {
                            Log.d("LoginActivity", it.data.toString())
                            if (it.data == null) {
                                fireStore.collection("users")
                                    .document(auth.currentUser?.uid.toString())
                                    .set(user)
                                    .addOnSuccessListener {
                                        goToMain()
                                    }
                                    .addOnFailureListener {  }
                            }
                            else {
                                Log.d("LoginActivity","이미 존재하는 사용자입니다.")
                                goToMain()
                            }
                        }
                        .addOnFailureListener {
                            Log.e("LoginActivity", "사용자 조회에 실패했습니다.")
                            goToMain()
                        }
                }
            }
    }

    private fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}