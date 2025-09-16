package com.jinny.plancast.presentation.login

import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jinny.plancast.presentation.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jinny.plancast.presentation.todo.list.ToDoListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class LoginViewModel(
    var id: Long = -1,
) : BaseViewModel() {

    private lateinit var auth: FirebaseAuth

    // UI에 보여줄 로그인 상태 (예: StateFlow 사용)
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    // UI에 보여줄 로그인 상태 (예: StateFlow 사용)
    private val _signupState = MutableStateFlow<SignUpState>(SignUpState.Idle)
    val signupState: StateFlow<SignUpState> = _signupState


    private val _navigationEvent = MutableSharedFlow<Unit>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private var _loginLiveData = MutableLiveData<ToDoListState>(ToDoListState.UnInitialized)
    val loginLiveData: LiveData<ToDoListState> = _loginLiveData


    fun loginWithFirebase(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            return
        }

        auth = Firebase.auth

        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                // signInWithEmailAndPassword가 끝날 때까지 여기서 기다림 (suspend)
                val authResult = auth.signInWithEmailAndPassword(email, password).await()

                // await() 호출이 성공적으로 끝나면 이 코드가 실행됨
                _loginState.value = LoginState.Success(authResult.user)


            } catch (e: Exception) {
                // 로그인 실패 시 await()이 예외를 던짐
                _loginState.value = LoginState.Error(e.message ?: "로그인 실패")
            }
        }

    }

    fun loginWithToken(email: String, password: String) {
        auth = Firebase.auth

        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {

                // signInWithEmailAndPassword가 끝날 때까지 여기서 기다림 (suspend)
                val authResult = auth.signInWithEmailAndPassword(email, password).await()

                // await() 호출이 성공적으로 끝나면 이 코드가 실행됨
                _loginState.value = LoginState.Success(authResult.user)

            } catch (e: Exception) {
                // 로그인 실패 시 await()이 예외를 던짐
                _loginState.value = LoginState.Error(e.message ?: "로그인 실패")
            }
        }

    }



    fun signupWithFirebase(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            return
        }

        auth = Firebase.auth

        viewModelScope.launch {
            _signupState.value = SignUpState.Loading
            try {
                // signInWithEmailAndPassword가 끝날 때까지 여기서 기다림 (suspend)
                val authResult = auth.createUserWithEmailAndPassword(email, password).await()

                // await() 호출이 성공적으로 끝나면 이 코드가 실행됨
                _signupState.value = SignUpState.Success(authResult.user)

                // 회원가입 토큰 저장

            } catch (e: Exception) {
                // 로그인 실패 시 await()이 예외를 던짐
                _signupState.value = SignUpState.Error(e.message ?: "로그인 실패")
            }
        }

    }




    override fun fetchData()= viewModelScope.launch {
    }

}
