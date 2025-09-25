package com.jinny.plancast.presentation.transfer

import TransferScreen
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope

import com.jinny.plancast.presentation.BaseActivity
import com.jinny.plancast.presentation.login.LoginState
import com.jinny.plancast.presentation.password.PasswordActivity
import com.jinny.plancast.presentation.todo.list.ListActivity
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class TransferActivity : BaseActivity<TransferViewModel>() {

    override val viewModel: TransferViewModel by viewModel()

    private val passwordLauncher: ActivityResultLauncher<Intent> =
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

                TransferScreen(viewModel)
            }
        }

        lifecycleScope.launch {
            viewModel.passwordCheck.collect { state ->
                when (state) {
                    true -> {
                        Toast.makeText(this@TransferActivity, "go to password!", Toast.LENGTH_SHORT)
                            .show()
                        // 메인 화면으로 이동
                        val intent = Intent(this@TransferActivity, PasswordActivity::class.java)
                        passwordLauncher.launch(intent)
                    }

                    false -> {
                        // 초기 상태 또는 다른 상태 처리
                    }
                }
            }
        }
    }

    override fun observeData() = viewModel.transferLiveData.observe(this@TransferActivity) {
    }
}

