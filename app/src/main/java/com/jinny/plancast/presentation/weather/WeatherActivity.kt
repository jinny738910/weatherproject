package com.jinny.plancast.presentation.weather

import MapScreen
import SearchScreen
import WeatherScreen
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle

import androidx.activity.compose.setContent
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jinny.plancast.presentation.BaseActivity
import com.jinny.plancast.presentation.todo.detail.DetailActivity
import com.jinny.plancast.presentation.todo.detail.DetailMode
import com.jinny.plancast.presentation.webView.WebViewActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class WeatherActivity : BaseActivity<WeatherViewModel>() {

    override val viewModel: WeatherViewModel by viewModel{
        parametersOf(
            intent?.getSerializableExtra(DETAIL_MODE_KEY),
            intent.getLongExtra(TODO_ID_KEY, -1)
        )
    }

    val SearchViewModel: SearchViewModel by viewModel{
        parametersOf(
            intent?.getSerializableExtra(DETAIL_MODE_KEY),
            intent.getLongExtra(TODO_ID_KEY, -1)
        )
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
            LaunchedEffect(Unit) {
                viewModel.navigationEvent.collect {
                    val intent = Intent(this@WeatherActivity, WebViewActivity::class.java)
                    startActivity(intent)
                }
            }

            WeatherAppNavigation()
        }

//        val resultIntent = Intent()
//        resultIntent.putExtra("result_key", "이것이 결과값입니다!")
//        setResult(Activity.RESULT_OK, resultIntent)
//        finish()
    }


    @Composable
    fun WeatherAppNavigation() {

        val searchResults by SearchViewModel.searchResults.collectAsState()
        val uistate by viewModel.uiState.collectAsState()

        // 1. 화면 이동을 제어하는 NavController를 생성합니다.
        val navController = rememberNavController()


        // 2. NavHost를 사용하여 내비게이션 경로와 화면을 정의합니다.
        NavHost(navController = navController, startDestination = "search") {
            // "weather" 경로 요청 시 WeatherScreen을 보여줍니다.
            composable("weather") {
                WeatherScreen(navController = navController, viewModel = viewModel)
            }
            // "search" 경로 요청 시 SearchScreen을 보여줍니다.
            composable("search") {
                SearchScreen(navController = navController, viewModel = SearchViewModel, onSerachIconClick = {
                    // 검색 아이콘 클릭 시 "weather" 화면으로 이동
                    navController.navigate("weather")
                })
            }
            composable(
                "map"
//                route = "map/{placeId}",
//                arguments = listOf(navArgument("placeId") { type = NavType.StringType })
            ) { backStackEntry ->
                MapScreen(placeId = backStackEntry.arguments?.getString("placeId"))
            }
        }
    }


    override fun observeData() = viewModel.weatherLiveData.observe(this@WeatherActivity) {
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
