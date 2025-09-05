package aop.fastcampus.part5.chapter01.presentation.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import aop.fastcampus.part5.chapter01.data.entity.ToDoEntity
import aop.fastcampus.part5.chapter01.domain.todoUseCase.DeleteToDoItemUseCase
import aop.fastcampus.part5.chapter01.domain.todoUseCase.GetToDoItemUseCase
import aop.fastcampus.part5.chapter01.domain.todoUseCase.InsertToDoUseCase
import aop.fastcampus.part5.chapter01.domain.todoUseCase.UpdateToDoUseCase
import aop.fastcampus.part5.chapter01.presentation.BaseViewModel
import kotlinx.coroutines.launch
import java.lang.Exception

internal class WeatherViewModel(
    var weatherMode: WeatherMode?,
    var id: Long = -1,
    private val getToDoItemUseCase: GetToDoItemUseCase,
    private val deleteToDoItemUseCase: DeleteToDoItemUseCase,
    private val updateToDoUseCase: UpdateToDoUseCase,
    private val insertToDoUseCase: InsertToDoUseCase
) : BaseViewModel() {

    private var _weatherLiveData = MutableLiveData<WeatherState>(WeatherState.UnInitialized)
    val weatherLiveData: LiveData<WeatherState> = _weatherLiveData

    override fun fetchData() = viewModelScope.launch {
//        when (weatherMode) {
//            weatherMode.WRITE -> {
//                _weatherLiveData.postValue(WeatherState.Write)
//            }
//            weatherMode.DETAIL -> {
//                _weatherLiveData.postValue(WeatherState.Loading)
//                try {
//                    getToDoItemUseCase(id)?.let {
//                        _weatherLiveData.postValue(WeatherState.Success(it))
//                    } ?: kotlin.run {
//                        _weatherLiveData.postValue(WeatherState.Error)
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                    _weatherLiveData.postValue(WeatherState.Error)
//                }
//            }
    }

}
