package aop.fastcampus.part5.chapter01.presentation.weather

//import WeatherScreen
import WeatherScreen
import android.os.Bundle

import androidx.activity.compose.setContent
import aop.fastcampus.part5.chapter01.databinding.ActivityDetailBinding
import aop.fastcampus.part5.chapter01.presentation.BaseActivity
import aop.fastcampus.part5.chapter01.presentation.detail.DetailActivity.Companion.DETAIL_MODE_KEY
import aop.fastcampus.part5.chapter01.presentation.detail.DetailActivity.Companion.TODO_ID_KEY
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

internal class WeatherActivity : BaseActivity<WeatherViewModel>() {

    override val viewModel: WeatherViewModel by viewModel{
        parametersOf(
            intent?.getSerializableExtra(DETAIL_MODE_KEY),
            intent.getLongExtra(TODO_ID_KEY, -1)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
           WeatherScreen()
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
