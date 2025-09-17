package com.jinny.plancast.presentation.payment

import LoginScreen
import PaymentScreen
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast

import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.biometric.BiometricPrompt
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.jinny.plancast.presentation.BaseActivity
import com.jinny.plancast.presentation.payment.PaymentViewModel
import com.jinny.plancast.presentation.todo.detail.DetailActivity
import com.jinny.plancast.presentation.todo.detail.DetailMode
import org.koin.androidx.viewmodel.ext.android.viewModel



class PaymentActivity : BaseActivity<PaymentViewModel>() {

    override val viewModel: PaymentViewModel by viewModel()


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


    companion object {
        const val TODO_ID_KEY = "ToDoId"
        const val DETAIL_MODE_KEY = "DetailMode"

        const val FETCH_REQUEST_CODE = 10

        fun getIntent(context: Context, detailMode: DetailMode) = Intent(context, DetailActivity::class.java).apply {
            putExtra(DETAIL_MODE_KEY, detailMode)
        }

        fun getIntent(context: Context, id: Long, detailMode: DetailMode) = Intent(context, DetailActivity::class.java).apply {
            putExtra(TODO_ID_KEY, id)
            putExtra(DETAIL_MODE_KEY, detailMode)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                val uiState by viewModel.uiState.collectAsState()
                val showDialog by viewModel.showDialog.collectAsState()

                PaymentScreen(
                    state = uiState,
                    onPayClick = { /*TODO*/ },
                    onMethodChangeClick = {  },
                    onDismissRequest = { },
                    onMethodSelected = {
                    },
                    showPaymentMethodDialog = showDialog
                )
            }
        }



//        val resultIntent = Intent()
//        resultIntent.putExtra("result_key", "이것이 결과값입니다!")
//        setResult(Activity.RESULT_OK, resultIntent)
//        finish()
    }



    override fun observeData() = viewModel.loginLiveData.observe(this@PaymentActivity) {
//            when (it) {
////            is ToDoDetailState.UnInitialized -> {
////                initViews(binding)
////            }
////            is ToDoDetailState.Loading -> {
////                handleLoadingState()
////            }
////            is ToDoDetailState.Success -> {
////                handleSuccessState(it)
////            }
////            is ToDoDetailState.Modify -> {
////                handleModifyState()
////            }
////            is ToDoDetailState.Delete -> {
////                Toast.makeText(this, "성공적으로 삭제되었습니다.", Toast.LENGTH_SHORT).show()
////                finish()
////            }
////            is ToDoDetailState.Error -> {
////                Toast.makeText(this, "에러가 발생했습니다.", Toast.LENGTH_SHORT).show()
////                finish()
////            }
////            is ToDoDetailState.Write -> {
////                handleWriteState()
////            }
    }
}
