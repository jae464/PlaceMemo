package com.jae464.placememo.presentation.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
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
import com.jae464.placememo.domain.model.login.User
import com.jae464.placememo.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {
    private lateinit var client: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        setGoogleLogin()
        initListener()
        initObserver()
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

        binding.dialogButton.setOnClickListener {
            SettingNicknameDialog().show(supportFragmentManager, "test")
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
                    viewModel.getUserInfo(auth.currentUser?.uid.toString())
                }
            }
    }

    private fun initObserver() {
        viewModel.user.observe(this) {
            Log.d("LoginActivity", it.toString())
            if (it == null) {
                println("파이어스토어에 등록되지 않은 유저입니다.")
                // todo FireStore에 유저 정보 등록하기
                val currentUser = FirebaseAuth.getInstance().currentUser
                if (currentUser == null) {
                    Log.e("LoginActivity", "현재 유저 가져오기 실패")
                    return@observe
                }
                val user = User(currentUser.uid, currentUser.email ?: "")
                viewModel.setUserInfo(user)
                Log.d("LoginActivity", "ViewModel보다 먼저 시작되는지 확인")
                goToMain()
            }
            else {
                goToMain()
            }
        }
    }

    private fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}