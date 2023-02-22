package com.jae464.presentation.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.activity.viewModels
import androidx.fragment.app.DialogFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.jae464.domain.model.login.User
import com.jae464.presentation.MainActivity
import com.jae464.presentation.base.BaseActivity
import com.jae464.presentation.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import com.jae464.presentation.R

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login),
SettingNicknameDialog.NoticeDialogListener {
    private lateinit var client: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private val viewModel: LoginViewModel by viewModels()

    private val googleLoginLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
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
            googleLoginLauncher.launch(client.signInIntent)
        }

        binding.dialogButton.setOnClickListener {
            SettingNicknameDialog().show(supportFragmentManager, "test")
        }

    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
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
                val currentUser = FirebaseAuth.getInstance().currentUser
                if(currentUser == null) {
                    Log.e("LoginActivity", "현재 유저 가져오기 실패")
                    return@observe
                }
                showNicknameDialog()
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

    private fun showNicknameDialog() {
        val dialog = SettingNicknameDialog()
        dialog.show(supportFragmentManager, "SettingNicknameDialogFragment")
    }

    override fun onDialogPositiveClick(dialog: DialogFragment, nickname: String) {
        println("onDialogPositiveClick")
        println(nickname)
        // TODO Firestore 에 유저 정보 업로드하기
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return
        if (currentUser.email == null) return
        val user =
            User(currentUser.uid, currentUser.email!!, nickname)
        viewModel.setUserInfo(user)
        goToMain()

    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        TODO("Not yet implemented")
    }
}