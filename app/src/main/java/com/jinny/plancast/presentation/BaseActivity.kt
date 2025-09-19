package com.jinny.plancast.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Job

abstract class BaseActivity<VM: BaseViewModel>: AppCompatActivity() {

    abstract val viewModel: VM

    private lateinit var fetchJob: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fetchJob = viewModel.fetchData()
        observeData()
    }

    abstract fun observeData()

    override fun onDestroy() {
        if (::fetchJob.isInitialized && fetchJob.isActive) {
            fetchJob.cancel()
        }
        super.onDestroy()
    }

}
