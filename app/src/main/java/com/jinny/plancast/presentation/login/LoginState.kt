package com.jinny.plancast.presentation.login

import com.google.firebase.auth.FirebaseUser


// 로그인 상태를 나타내는 Sealed Class (UI 상태 관리)
sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val user: FirebaseUser?) : LoginState()
    data class Error(val message: String) : LoginState()
}


// 로그인 상태를 나타내는 Sealed Class (UI 상태 관리)
sealed class SignUpState {
    object Idle : SignUpState()
    object Loading : SignUpState()
    data class Success(val user: FirebaseUser?) : SignUpState()
    data class Error(val message: String) : SignUpState()
}


