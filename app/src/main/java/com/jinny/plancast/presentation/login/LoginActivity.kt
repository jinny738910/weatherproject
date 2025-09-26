package com.jinny.plancast.presentation.login

import LoginScreen
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast

import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.biometric.BiometricPrompt
import androidx.compose.material3.MaterialTheme
import androidx.core.content.ContextCompat

import androidx.lifecycle.lifecycleScope
import com.jinny.plancast.presentation.BaseActivity
import com.jinny.plancast.presentation.login.SignUpState.Success
import com.jinny.plancast.presentation.todo.list.ListActivity
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.Executor


class LoginActivity : BaseActivity<LoginViewModel>() {

    override val viewModel: LoginViewModel by viewModel()

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    private val listLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            // 결과가 돌아왔을 때 이 람다 블록이 실행됩니다.
            if (result.resultCode == Activity.RESULT_OK) {
                // 성공적인 결과를 처리합니다.
                val data: Intent? = result.data
                val message = data?.getStringExtra("result_key")
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                viewModel.fetchData()
            }
        }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                LoginScreen(
                    onLoginClick = { email, password ->
                        viewModel.loginWithFirebase(email, password)
                    },
                    onSignUpClick = { email, password ->
                        viewModel.signupWithFirebase(email, password)
                    },
                    onBiometricClick = { email, password ->
                        // 저장된 사용자 정보가 있는지 확인 후 생체 인증 실행
                        // (실제 앱에서는 EncryptedSharedPreferences 등으로 안전하게 관리)
                        setupBiometricPrompt(email, password)
                    }
                )
            }
        }

        lifecycleScope.launch {
            viewModel.loginState.collect { state ->
                when (state) {
                    is LoginState.Success -> {
                        Toast.makeText(this@LoginActivity, "login success!", Toast.LENGTH_SHORT).show()
                        // 메인 화면으로 이동
                        val intent = Intent(this@LoginActivity, ListActivity::class.java)
                        listLauncher.launch(intent)
                        finish()
                    }

                    is LoginState.Error -> {
                        Toast.makeText(this@LoginActivity, "login error!", Toast.LENGTH_SHORT).show()
                    }
                    // ... Loading, Idle 상태 처리
                    LoginState.Idle -> {
                        Toast.makeText(this@LoginActivity, "login idle!", Toast.LENGTH_SHORT).show()
                    }

                    LoginState.Loading -> {

                    }

                    else -> {}
                }
            }
        }

        lifecycleScope.launch {
            viewModel.signupState.collect { state ->
                when (state) {
                    is Success -> {
                        Toast.makeText(this@LoginActivity, "signup success!", Toast.LENGTH_SHORT)
                            .show()
                        // 메인 화면으로 이동
                        val intent = Intent(this@LoginActivity, ListActivity::class.java)
                        listLauncher.launch(intent)
                        finish()

                    }

                    is SignUpState.Error -> {
                        Toast.makeText(this@LoginActivity, "signup error!", Toast.LENGTH_SHORT)
                            .show()
                    }
                    // ... Loading, Idle 상태 처리
                    SignUpState.Idle -> {

                    }

                    SignUpState.Loading -> {

                    }

                    else -> {}

                }
            }
        }


//        val resultIntent = Intent()
//        resultIntent.putExtra("result_key", "이것이 결과값입니다!")
//        setResult(Activity.RESULT_OK, resultIntent)
//        finish()
    }

    fun setupBiometricPrompt(email: String, password: String) {
        val executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    // ✅ 인증 성공!
                    Toast.makeText(applicationContext, "인증 성공: $result", Toast.LENGTH_SHORT).show()

                    // 여기서 EncryptedSharedPreferences에 저장된
                    // 이메일/비밀번호나 토큰으로 Firebase 로그인을 시도
                    viewModel.loginWithFirebase("jinny7389@naver.com", "jinny4019")
                }

                // ❌ 에러 발생 시 호출됨 (예: 센서 오류, 시도 횟수 초과)
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(applicationContext, "인증 에러: $errString", Toast.LENGTH_SHORT).show()
                }

                // 👎 인증에 실패했을 때 호출됨 (예: 지문이 일치하지 않음)
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext, "인증에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("생체 인증 로그인")
            .setSubtitle("지문 또는 얼굴로 인증해주세요.")
            .setNegativeButtonText("취소")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }



    override fun observeData() = viewModel.loginLiveData.observe(this@LoginActivity) {

    }
}
