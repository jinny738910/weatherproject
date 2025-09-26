package com.jinny.plancast.presentation.financial.password


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


class PasswordViewModel(
    var id: Long = -1,
) : BaseViewModel() {

    private lateinit var auth: FirebaseAuth

    // UI에 보여줄 로그인 상태 (예: StateFlow 사용)
    private val _PasswordState = MutableStateFlow<PasswordState>(PasswordState.Idle)
    val loginState: StateFlow<PasswordState> = _PasswordState

    private val _navigationEvent = MutableSharedFlow<Unit>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private var _loginLiveData = MutableLiveData<ToDoListState>(ToDoListState.UnInitialized)
    val loginLiveData: LiveData<ToDoListState> = _loginLiveData


    fun AuthenticateWithPassword(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            return
        }

        auth = Firebase.auth

        viewModelScope.launch {
            _PasswordState.value = PasswordState.Loading
            try {
                // signInWithEmailAndPassword가 끝날 때까지 여기서 기다림 (suspend)
                val authResult = auth.signInWithEmailAndPassword(email, password).await()

                // await() 호출이 성공적으로 끝나면 이 코드가 실행됨
                _PasswordState.value = PasswordState.Success(authResult.user)


            } catch (e: Exception) {
                // 로그인 실패 시 await()이 예외를 던짐
                _PasswordState.value = PasswordState.Error(e.message ?: "로그인 실패")
            }
        }

    }


    override fun fetchData()= viewModelScope.launch {
    }

}
