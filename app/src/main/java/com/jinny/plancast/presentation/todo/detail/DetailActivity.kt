package com.jinny.plancast.presentation.todo.detail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.jinny.plancast.presentation.BaseActivity
import com.jinny.plancast.databinding.ActivityDetailBinding
import com.jinny.plancast.presentation.payment.PaymentActivity
import com.jinny.plancast.presentation.todo.list.ListActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class DetailActivity : BaseActivity<DetailViewModel>() {

    private lateinit var binding: ActivityDetailBinding

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
                finish()
            }
            is ToDoDetailState.Error -> {
                Toast.makeText(this, "오류: ${it.message}", Toast.LENGTH_LONG).show()
                Log.d("DetailActivity", "오류: ${it.message}")
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
                image = imageInput.text.toString(),
                isClimate = weatherAlarmCheckbox.isChecked,
                isLocation = locationAlarmCheckbox.isChecked,
                isFinancial = financialAlarmCheckbox.isChecked,
                isRepeat = repeatCheckbox.isChecked,
                isLock = lockCheckbox.isChecked
            )
        }

        financialButton.setOnClickListener {
            val intent = Intent(this@DetailActivity, PaymentActivity::class.java)
            paymentLauncher.launch(intent)
//            val destination = destinationInput.text.toString()
//            if (destination.isNotEmpty()) {
//                startActivity(Intent(this@DetailActivity, com.jinny.plancast.presentation.financial.FinancialActivity::class.java).apply {
//                    putExtra("destination", destination)
//                })
//            } else {
//                Toast.makeText(this@DetailActivity, "목적지를 입력해주세요.", Toast.LENGTH_SHORT).show()
//            }
        }
    }

    private fun handleLoadingState() = with(binding) {
        progressBar.isGone = false
    }

    private fun handleModifyState() = with(binding) {
        titleInput.isEnabled = true
        descriptionInput.isEnabled = true

        deleteButton.isGone = true
        modifyButton.isGone = true
        updateButton.isGone = false
    }

    private fun handleWriteState() = with(binding) {
        titleInput.isEnabled = true
        descriptionInput.isEnabled = true

        updateButton.isGone = false
    }

    private fun handleSuccessState(state: ToDoDetailState.Success) = with(binding) {
        progressBar.isGone = true

        titleInput.isEnabled = false
        descriptionInput.isEnabled = false

        deleteButton.isGone = false
        modifyButton.isGone = false
        updateButton.isGone = true
        financialButton.isGone = true

        val toDoItem = state.toDoItem
        titleInput.setText(toDoItem.title)
        descriptionInput.setText(toDoItem.description)
        timeInput.setText(toDoItem.date)
        destinationInput.setText(toDoItem.destination)
        imageInput.setText(toDoItem.image)
        weatherAlarmCheckbox.isChecked = toDoItem.isClimate
        locationAlarmCheckbox.isChecked = toDoItem.isLocation
        financialAlarmCheckbox.isChecked = toDoItem.isFinancial
        repeatCheckbox.isChecked = toDoItem.isRepeat

        Log.d("DetailActivity", "is Financial : $(toDoItem.isFinancial)")

        if(financialAlarmCheckbox.isChecked){
            Log.d("DetailActivity", "isFinancial true")
            financialButton.isGone = false
        }
    }
}
