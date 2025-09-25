package com.jinny.plancast.presentation.password

import com.google.firebase.auth.FirebaseUser


// 로그인 상태를 나타내는 Sealed Class (UI 상태 관리)
sealed class PasswordState {
    object Idle : PasswordState()
    object Loading : PasswordState()
    data class Success(val user: FirebaseUser?) : PasswordState()
    data class Error(val message: String) : PasswordState()
}



