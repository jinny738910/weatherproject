package com.jinny.plancast.presentation.todo.list

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.jinny.plancast.R
import com.jinny.plancast.presentation.BaseActivity
import com.jinny.plancast.databinding.ActivityListBinding
import com.jinny.plancast.presentation.alarm.AlarmListActivity
import com.jinny.plancast.presentation.chat.chatview.ChatViewActivity
import com.jinny.plancast.presentation.financial.transaction.TransactionActivity
import com.jinny.plancast.presentation.login.LoginActivity
import com.jinny.plancast.presentation.setting.SettingActivity
import com.jinny.plancast.presentation.todo.detail.DetailActivity
import com.jinny.plancast.presentation.todo.detail.DetailMode
import com.jinny.plancast.presentation.todo.view.ToDoAdapter
import com.jinny.plancast.presentation.weather.weatherView.WeatherActivity
import kotlinx.coroutines.*
import kotlinx.coroutines.suspendCancellableCoroutine
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.Executor
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume

class ListActivity : BaseActivity<ListViewModel>(), CoroutineScope{

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private lateinit var binding: ActivityListBinding

    private val adapter = ToDoAdapter()

    override val viewModel: ListViewModel by viewModel()

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var userDB: DatabaseReference = Firebase.database.reference.child("users")

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

//    ActivityResultLauncher를 생성하고 결과를 처리할 콜백을 정의합니다.
    private val detailLauncher: ActivityResultLauncher<Intent> =
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

    private val weatherLauncher: ActivityResultLauncher<Intent> =
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

    private val alarmLauncher: ActivityResultLauncher<Intent> =
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

    private val settingLauncher: ActivityResultLauncher<Intent> =
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
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(auth.currentUser == null) {
            Log.d("ListActivity", "User is not signed in go to login: ${auth.currentUser?.email}")
            startActivity(Intent(this, LoginActivity::class.java))
        } else {
            // User is signed in
            Log.d("ListActivity", "User is signed in: ${auth.currentUser?.email}")
        }

        val currentUserDB = userDB.child(getCurrentUserID())
        Log.d("ListActivity", "currentUserId: ${getCurrentUserID()}")
        currentUserDB.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child("name").value == null) {
                    Log.d("ListActivity", "datasnapshot: $snapshot")
                    Log.d("ListActivity", "datasnapshot name: ${snapshot.child("name").value}")
                    Log.d("ListActivity", "datasnapshot userId: ${snapshot.child("userId").value}")
                    Log.d("ListActivity", "showNameInputPopup")
                    showNameInputPopup()
                    return
                }

            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }


    override fun onStart() {
        super.onStart()

    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchData()
    }


    private fun initViews(binding: ActivityListBinding) = with(binding) {
        recyclerView.layoutManager = LinearLayoutManager(this@ListActivity, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter

        refreshLayout.setOnRefreshListener {
            Log.d("ListActivity", "refresh ! ")
            viewModel.fetchData()
        }

        addToDoButton.setOnClickListener {
            val intent = Intent(this@ListActivity, DetailActivity::class.java)
            detailLauncher.launch(
                DetailActivity.getIntent(this@ListActivity, DetailMode.WRITE)
            )
        }

        weatherDetailsButton.setOnClickListener {
            val intent = Intent(this@ListActivity, WeatherActivity::class.java)
            weatherLauncher.launch(intent)
        }

    }

    override fun observeData() {
        viewModel.toDoListLiveData.observe(this) {
            when (it) {
                is ToDoListState.UnInitialized -> {
                    initViews(binding)
                }
                is ToDoListState.Loading -> {
                    handleLoadingState()
                }
                is ToDoListState.Suceess -> {
                    handleSuccessState(it)
                }
                is ToDoListState.Error -> {
                    handleErrorState(it)
                }
            }
        }
    }

    private fun handleLoadingState() = with(binding) {
//        refreshLayout.isRefreshing = true
    }

    private fun handleSuccessState(state: ToDoListState.Suceess) = with(binding) {
        refreshLayout.isEnabled = state.toDoList.isNotEmpty()
        refreshLayout.isRefreshing = false

        if (state.toDoList.isEmpty()) {
            emptyResultTextView.isGone = false
            recyclerView.isGone = true
        } else {
            emptyResultTextView.isGone = true
            recyclerView.isGone = false
            adapter.setToDoList(
                state.toDoList,
                toDoItemClickListener = {
                    if (it.isLock) {
                        launch {
                            val authOk = setupBiometricPrompt()
                            if (authOk) {
                                Log.e("ListActivity", "BiometricPrompt success!")
                                startActivityForResult(
                                    DetailActivity.getIntent(this@ListActivity, it.id, DetailMode.DETAIL),
                                    DetailActivity.FETCH_REQUEST_CODE
                                )
                            }
                        }
                    }
                    else {
                        startActivityForResult(
                            DetailActivity.getIntent(this@ListActivity, it.id, DetailMode.DETAIL),
                            DetailActivity.FETCH_REQUEST_CODE
                        )
//                        val intent = Intent(this@ListActivity, DetailActivity::class.java)
//                        detailLauncher.launch(
//                            DetailActivity.getIntent(this@ListActivity, it.id, DetailMode.WRITE)
//                        )
                    }
                }, toDoCheckListener = {
                    viewModel.updateEntity(it)
                }
            )
        }
    }

    private fun handleErrorState(state: ToDoListState.Error) = with(binding) {
        refreshLayout.isRefreshing = false
        Toast.makeText(this@ListActivity, "오류: ${state.message}", Toast.LENGTH_LONG).show()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == DetailActivity.FETCH_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Log.d("ListActivity", "onActivityResult !! fetch data! ")
            viewModel.fetchData()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete_all -> {
                viewModel.deleteAll()
                true
            }
            R.id.action_alarm_check -> {
                val intent = Intent(this@ListActivity, AlarmListActivity::class.java)
                alarmLauncher.launch(intent)
                true

            }
            R.id.action_setting -> {
                val intent = Intent(this@ListActivity, SettingActivity::class.java)
                settingLauncher.launch(intent)
                true

            }
            R.id.action_logout -> {
                auth.signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                true
            }
            R.id.action_chatlist -> {
                startActivity(Intent(this, ChatViewActivity::class.java))
                finish()
                true
            }
            R.id.action_money_check -> {
                startActivity(Intent(this,TransactionActivity::class.java))
                finish()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.list_menu, menu)
        return true
    }

    private suspend fun setupBiometricPrompt(): Boolean = suspendCancellableCoroutine { continuation ->
        val executor = ContextCompat.getMainExecutor(this)

        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(applicationContext, "인증 성공: $result", Toast.LENGTH_SHORT).show()
                    if (!continuation.isCompleted) continuation.resume(true)
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(applicationContext, "인증 에러: $errString", Toast.LENGTH_SHORT).show()
                    if (!continuation.isCompleted) continuation.resume(false)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext, "인증에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    // Keep waiting for another attempt
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("생체 인증 로그인")
            .setSubtitle("지문 또는 얼굴로 인증해주세요.")
            .setNegativeButtonText("취소")
            .build()

        biometricPrompt.authenticate(promptInfo)

        continuation.invokeOnCancellation {
            // No explicit cancel hook available for BiometricPrompt
        }
    }

    private fun showNameInputPopup() {
        val editText = EditText(this)
        AlertDialog.Builder(this)
            .setTitle("이름을 입력해 주세요")
            .setView(editText)
            .setPositiveButton("저장") { _, _ ->
                if (editText.text.isEmpty()) {
                    showNameInputPopup()
                } else {
                    viewModel.saveUserName(editText.text.toString())
                }
            }
            .setCancelable(true)
            .show()

    }

    private fun getCurrentUserID(): String {
        if (auth.currentUser == null) {
            return ""
        }

        return auth.currentUser!!.uid
    }


}