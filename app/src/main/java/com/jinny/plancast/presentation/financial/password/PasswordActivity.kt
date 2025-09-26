package com.jinny.plancast.presentation.financial.password

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts


import com.chaos.view.PinView
import com.jinny.plancast.R
import com.jinny.plancast.presentation.BaseActivity
import com.jinny.plancast.presentation.todo.list.ListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class PasswordActivity : BaseActivity<ListViewModel>() {

    override val viewModel: ListViewModel by viewModel()

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
        setContentView(R.layout.activity_password_keypad_view)

        val pinView = findViewById<PinView>(R.id.pinView)


            pinView.addTextChangedListener(object : android.text.TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // Do nothing
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // 6자리가 모두 입력되었는지 확인
                    if (s?.length == 6) {
                        // 입력 완료 시 동작 정의
                        handlePinVerification(s.toString())
                    }
                }

                override fun afterTextChanged(s: android.text.Editable?) {
                    // Do nothing
                }
            })
    }

    /**
     * 입력된 PIN을 처리하는 함수
     * @param pin 사용자가 입력한 6자리 PIN 번호
     */
    private fun handlePinVerification(pin: String) {
        // 실제 앱에서는 여기서 서버로 PIN을 보내거나 내부 DB와 비교하여 인증 처리를 합니다.
        Toast.makeText(this, "입력된 비밀번호: $pin", Toast.LENGTH_SHORT).show()

        // TODO: 인증 성공 또는 실패 로직 구현
        if (pin == "123456") { // 예: 올바른 비밀번호
            // 성공 처리
        } else {
            // 실패 처리 (예: PinView 초기화, 오류 메시지 표시)
            val pinView = findViewById<PinView>(R.id.pinView)
            pinView.setText("") // 입력 칸 비우기
            Toast.makeText(this, "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show()
        }
    }


    override fun observeData() = viewModel.toDoListLiveData.observe(this@PasswordActivity) {
    }
}
