package com.jae464.placememo

import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.ktx.Firebase
import com.jae464.placememo.databinding.ActivityLoginBinding
import com.jae464.placememo.presentation.base.BaseActivity

class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {
    private lateinit var client: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        setOneTapLogin()
        setGoogleLogin()
        initListener()
    }

    private fun setOneTapLogin() {
        oneTapClient = Identity.getSignInClient(this)
        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                .setSupported(true)
                .build())
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId("599832472009-9n76h9tik9oh4v670hk4tquv75hg56jk.apps.googleusercontent.com")
                    .setFilterByAuthorizedAccounts(true)
                    .build())
            .setAutoSelectEnabled(true)
            .build()
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
//            oneTapClient.beginSignIn(signInRequest)
//                .addOnSuccessListener(this) { result ->
//                    try {
//                        startIntentSenderForResult(
//                            result.pendingIntent.intentSender, 2,
//                            null, 0, 0, 0, null)
//
//                    } catch (e: IntentSender.SendIntentException) {
//                        Log.e("LoginActivity", "원탭 로그인을 시작할 수 없습니다.")
//                    }
//                }
//                .addOnFailureListener(this) { e->
//                    Log.d("LoginActivity", e.localizedMessage)
//                }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            var account: GoogleSignInAccount? = null
            try {
                Toast.makeText(this, "sucess google login", Toast.LENGTH_SHORT).show()
                account = task.getResult(ApiException::class.java)
                Log.d("LoginActivity", account.toString())
                firebaseAuthWithGoogle(account!!.idToken)
            } catch (e: ApiException) {
                Toast.makeText(this, "Failed Google Login", Toast.LENGTH_SHORT).show()
            }
        }
        when (requestCode) {
            2 -> {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(data)
                    val idToken = credential.googleIdToken
                    val username = credential.id
                    val password = credential.password
                } catch (e: ApiException) {
                    Log.e("LoginActivity", e.message.toString())
                }
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
                    println(email)
                }
            }
    }
}