package com.jinny.plancast.presentation.alarm

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import com.jinny.plancast.presentation.BaseActivity
import com.jinny.plancast.databinding.ActivityListBinding
import com.jinny.plancast.presentation.todo.view.ToDoAdapter
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.coroutines.CoroutineContext

class AlarmListActivity : BaseActivity<AlarmListViewModel>(), CoroutineScope{

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private lateinit var binding: ActivityListBinding

    private val adapter = ToDoAdapter()

    override val viewModel: AlarmListViewModel by viewModel()

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addToDoButton.isGone =true
        binding.weatherDetailsButton.isGone =true
    }

    private fun initViews(binding: ActivityListBinding) = with(binding) {
        recyclerView.layoutManager = LinearLayoutManager(this@AlarmListActivity, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter

        refreshLayout.setOnRefreshListener {
            viewModel.fetchData()
        }
    }

    override fun observeData() {
    }

}