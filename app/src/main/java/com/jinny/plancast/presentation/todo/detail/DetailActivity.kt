package com.jinny.plancast.presentation.todo.detail

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.isGone
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import com.jinny.plancast.R
import com.jinny.plancast.presentation.BaseActivity
import com.jinny.plancast.databinding.ActivityDetailBinding
import com.jinny.plancast.presentation.chat.ChatUserListActivity
import com.jinny.plancast.presentation.chat.ChatRoomActivity
import com.jinny.plancast.presentation.financial.payment.PaymentActivity
import com.jinny.plancast.presentation.financial.transfer.TransferActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class DetailActivity : BaseActivity<DetailViewModel>() {

    private lateinit var binding: ActivityDetailBinding
    private var selectedUri: Uri? = null
    private val auth by lazy {
        Firebase.auth
    }
    private val storage: FirebaseStorage by lazy {
        Firebase.storage
    }
    private val chatDB: DatabaseReference by lazy {
        Firebase.database.reference.child("chats")
    }

    override val viewModel: DetailViewModel by viewModel {
        parametersOf(
            intent.getSerializableExtra(DETAIL_MODE_KEY),
            intent.getLongExtra(TODO_ID_KEY, -1)
        )
    }

    private val paymentLauncher: ActivityResultLauncher<Intent> =
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

    private val transferLauncher: ActivityResultLauncher<Intent> =
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

    private val inviteLauncher: ActivityResultLauncher<Intent> =
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

    private val commentLauncher: ActivityResultLauncher<Intent> =
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
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        setResult(Activity.RESULT_OK)
    }

    override fun observeData() = viewModel.toDoDetailLiveData.observe(this@DetailActivity) {
        when (it) {
            is ToDoDetailState.UnInitialized -> {
                initViews(binding)
            }
            is ToDoDetailState.Loading -> {
                handleLoadingState()
            }
            is ToDoDetailState.Success -> {
                handleSuccessState(it)
            }
            is ToDoDetailState.Modify -> {
                handleModifyState()
            }
            is ToDoDetailState.Delete -> {
                Toast.makeText(this, "성공적으로 삭제되었습니다.", Toast.LENGTH_SHORT).show()

                setResult(Activity.RESULT_OK)
                finish()
            }
            is ToDoDetailState.Error -> {
                Toast.makeText(this, "오류: ${it.message}", Toast.LENGTH_LONG).show()
                Log.d("DetailActivity", "오류: ${it.message}")

                setResult(Activity.RESULT_OK)
                finish()
            }
            is ToDoDetailState.Write -> {
                handleWriteState()
            }
        }
    }

    private fun initViews(binding: ActivityDetailBinding) = with(binding) {
        titleInput.isEnabled = false
        descriptionInput.isEnabled = false

        deleteButton.isGone = true
        modifyButton.isGone = true
        updateButton.isGone = true

        weatherAlarmCheckbox.isEnabled = false
        locationAlarmCheckbox.isEnabled = false
        financialAlarmCheckbox.isEnabled = false
        repeatCheckbox.isEnabled = false
        lockCheckbox.isEnabled =false

        inviteButton.setOnClickListener {
            val intent = Intent(this@DetailActivity, ChatUserListActivity::class.java)
            inviteLauncher.launch(intent)
        }

        commentsButton.setOnClickListener {
            val intent = Intent(this@DetailActivity, ChatRoomActivity::class.java)
            commentLauncher.launch(intent)
        }

        deleteButton.setOnClickListener {
            viewModel.deleteToDo()
        }
        modifyButton.setOnClickListener {
            viewModel.setModifyMode()
        }
        updateButton.setOnClickListener {
            viewModel.writeToDo(
                title = titleInput.text.toString(),
                date = timeInput.text.toString(),
                destination = destinationInput.text.toString(),
                description = descriptionInput.text.toString(),
//                image = imageInput.text.toString(),
                image = selectedUri,
                isClimate = weatherAlarmCheckbox.isChecked,
                isLocation = locationAlarmCheckbox.isChecked,
                isFinancial = financialAlarmCheckbox.isChecked,
                isRepeat = repeatCheckbox.isChecked,
                isLock = lockCheckbox.isChecked,
                hasCompleted = false
            )
        }

        mediaButton.setOnClickListener {
            when {
                ContextCompat.checkSelfPermission(
                    this@DetailActivity,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == android.content.pm.PackageManager.PERMISSION_GRANTED -> {
                    // 권한이 이미 허용된 경우
                   startContentProvider()
                }
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                    // 권한 요청 이유 다이얼로그 표시
                    showPermissionDialog()
                }
                else -> {
                    // 권한 요청
                    requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 100)
                }

            }
        }

        financialButton.setOnClickListener {

            showTwoButtonDialog()
//            val destination = destinationInput.text.toString()
//            if (destination.isNotEmpty()) {
//                startActivity(Intent(this@DetailActivity, com.jinny.plancast.presentation.financial.FinancialActivity::class.java).apply {
//                    putExtra("destination", destination)
//                })
//            } else {
//                Toast.makeText(this@DetailActivity, "목적지를 입력해주세요.", Toast.LENGTH_SHORT).show()
//            }
        }

        completeButton.setOnClickListener {
            viewModel.writeToDo(
                title = titleInput.text.toString(),
                date = timeInput.text.toString(),
                destination = destinationInput.text.toString(),
                description = descriptionInput.text.toString(),
//                image = imageInput.text.toString(),
                image = selectedUri,
                hasCompleted = true,
                isClimate = weatherAlarmCheckbox.isChecked,
                isLocation = locationAlarmCheckbox.isChecked,
                isFinancial = financialAlarmCheckbox.isChecked,
                isRepeat = repeatCheckbox.isChecked,
                isLock = lockCheckbox.isChecked
            )
            completeButton.setText(R.string.complete)
            completeButton.setTextColor(Color.White.toArgb())
            completeButton.setBackgroundColor(Color.Blue.toArgb())
            completeButton.isEnabled = false
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            100 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 권한이 허용된 경우
                    startContentProvider()
                } else {
                    // 권한이 거부된 경우
                    Toast.makeText(this, "권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun startContentProvider() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, 2020)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode != Activity.RESULT_OK){
            return
        }

        when (resultCode) {
            2020 -> {
                val uri = data?.data ?: return
                Log.d("DetailActivity", "onActivityResult: $uri")
                binding.imageInput.setImageURI(uri)
                selectedUri = uri
            }
            else -> {
                Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showPermissionDialog() {
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다")
            .setMessage("사진을 불러오기 위해 권한이 필요합니다.")
            .setPositiveButton("확인") { dialog, which ->
                // 권한 요청
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 100)
            }
            .setNegativeButton("취소") { dialog, which ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun handleLoadingState() = with(binding) {
        progressBar.isGone = false
    }

    private fun handleModifyState() = with(binding) {
        titleInput.isEnabled = true
        descriptionInput.isEnabled = true

        deleteButton.isGone = true
        modifyButton.isGone = true
        completeButton.isGone = true
        updateButton.isGone = false
        financialButton.isGone = true

        weatherAlarmCheckbox.isEnabled = true
        locationAlarmCheckbox.isEnabled = true
        financialAlarmCheckbox.isEnabled = true

        repeatCheckbox.isEnabled = true
        lockCheckbox.isEnabled =true
    }

    private fun handleWriteState() = with(binding) {
        titleInput.isEnabled = true
        descriptionInput.isEnabled = true

        updateButton.isGone = false
        financialButton.isGone = true

        weatherAlarmCheckbox.isEnabled = true
        locationAlarmCheckbox.isEnabled = true
        financialAlarmCheckbox.isEnabled = true

        repeatCheckbox.isEnabled = true
        lockCheckbox.isEnabled =true
    }

    private fun handleSuccessState(state: ToDoDetailState.Success) = with(binding) {
        progressBar.isGone = true

        titleInput.isEnabled = false
        descriptionInput.isEnabled = false
        imageInput.isEnabled = false
        timeInput.isEnabled = false
        destinationInput.isEnabled = false


        deleteButton.isGone = false
        modifyButton.isGone = false
        completeButton.isGone = false
        updateButton.isGone = true
        financialButton.isGone = true

        val toDoItem = state.toDoItem
        titleInput.setText(toDoItem.title)
        descriptionInput.setText(toDoItem.description)
        timeInput.setText(toDoItem.date)
        destinationInput.setText(toDoItem.destination)
//        imageInput.setText(toDoItem.image)
        imageInput.setImageURI(toDoItem.image.toUri())
        weatherAlarmCheckbox.isChecked = toDoItem.isClimate
        locationAlarmCheckbox.isChecked = toDoItem.isLocation
        financialAlarmCheckbox.isChecked = toDoItem.isFinancial
        repeatCheckbox.isChecked = toDoItem.isRepeat
        lockCheckbox.isChecked = toDoItem.isLock

        if(financialAlarmCheckbox.isChecked){
            Log.d("DetailActivity", "isFinancial true")
            financialButton.isGone = false
        }

        if(toDoItem.hasCompleted) {
            completeButton.setText(R.string.complete)
            completeButton.setTextColor(Color.White.toArgb())
            completeButton.setBackgroundColor(Color.Blue.toArgb())
            completeButton.isEnabled = false
        }
    }

    private fun showTwoButtonDialog() {
        val builder = AlertDialog.Builder(this)

        // 팝업창의 제목과 메시지 설정
        builder.setTitle("금융 거래 선택")
        builder.setMessage("송금 혹은 결제 선택해주세요")

        // '예' 버튼 (Positive Button)
        builder.setPositiveButton("카드로 결제") { dialog, which ->
            // '예' 버튼을 눌렀을 때 실행될 코드
            Toast.makeText(this, "카드로 결제 진행하겠습니다.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@DetailActivity,PaymentActivity::class.java)
            paymentLauncher.launch(intent)
        }

        // '아니오' 버튼 (Negative Button)
        builder.setNegativeButton("송금하기") { dialog, which ->
            // '아니오' 버튼을 눌렀을 때 실행될 코드
            Toast.makeText(this, "송금 진행하겠습니다.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@DetailActivity, TransferActivity::class.java)
            transferLauncher.launch(intent)
        }

        // 팝업창 생성 및 표시
        val dialog = builder.create()
        dialog.show()
    }
}
