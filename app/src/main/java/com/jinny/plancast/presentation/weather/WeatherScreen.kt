import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.jinny.plancast.presentation.weather.WeatherMode
import com.jinny.plancast.presentation.weather.WeatherViewModel


// R.drawable.ic_weather_sun, ic_weather_cloudy 등이 필요합니다.
// 예시로 사용할 아이콘을 drawable 폴더에 추가해주세요.
// import com.example.weatherapp.R

@Composable
fun WeatherScreen(
    navController: NavController,
    viewModel: WeatherViewModel
) {
    Scaffold(
        topBar = { LocationBar() }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // 전체 화면 스크롤
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WebViewSection(navController = navController, viewModel = viewModel)
            Spacer(modifier = Modifier.height(16.dp))
            CurrentWeatherSection()
            Spacer(modifier = Modifier.height(24.dp))
            RainForecastSection()
            Spacer(modifier = Modifier.height(24.dp))
            HourlyForecastSection()
            Spacer(modifier = Modifier.height(24.dp))
            WeeklyForecastSection()
            Spacer(modifier = Modifier.height(24.dp))
            DetailedInfoSection()
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationBar() {
    TopAppBar(
        title = {
            Text("성남시, 분당구", fontWeight = FontWeight.Bold)
        },
        actions = {
            IconButton(onClick = { /* TODO: 검색 화면 이동 */ }) {
                Icon(Icons.Default.Search, contentDescription = "지역 검색")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

@Composable
fun WebViewSection(navController: NavController, viewModel:WeatherViewModel) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = { /* TODO: 현재 위치 가져오는 로직 호출 */
                viewModel.onOpenActivityClick()
            },
            modifier = Modifier.fillMaxWidth()
        ) {

            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
            Text("자세한 정보 보러가기")
        }
    }
}


@Composable
fun CurrentWeatherSection() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("23°", fontSize = 96.sp, fontWeight = FontWeight.Light)
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Image(painterResource(id = R.drawable.ic_weather_sun), contentDescription = "맑음 아이콘")
            Text("☀️", fontSize = 32.sp) // 아이콘 대신 임시 이모지 사용
            Spacer(modifier = Modifier.width(8.dp))
            Text("맑음", fontSize = 24.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text("최고:25° / 최저:15°", fontSize = 16.sp, color = Color.Gray)
    }
}

@Composable
fun RainForecastSection() {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("💧 초단기 강수 예보", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("앞으로 1시간 동안 비 소식이 없습니다.")
            // TODO: 시간대별 강수 확률 그래프나 아이콘 추가
        }
    }
}

@Composable
fun HourlyForecastSection() {
    val hourlyData = listOf(
        Triple("지금", "☀️", "23°"),
        Triple("17시", "☀️", "22°"),
        Triple("18시", "🌤️", "21°"),
        Triple("19시", "☁️", "20°"),
        Triple("20시", "☁️", "19°"),
        Triple("21시", "🌙", "18°"),
        Triple("22시", "🌙", "17°"),
    )
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("⏳ 시간대별 예보", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                items(hourlyData) { (time, icon, temp) ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(time, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(icon, fontSize = 28.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(temp, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}

@Composable
fun WeeklyForecastSection() {
    val weeklyData = listOf(
        Triple("오늘", "☀️", "25°/15°"),
        Triple("금", "🌤️", "24°/16°"),
        Triple("토", "☁️", "22°/15°"),
        Triple("일", "🌧️", "20°/14°"),
        Triple("월", "🌦️", "21°/13°"),
        Triple("화", "☀️", "23°/15°"),
    )
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("🗓️ 주간 예보", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            weeklyData.forEach { (day, icon, temp) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(day, modifier = Modifier.weight(1f))
                    Text(icon, fontSize = 24.sp)
                    Text(
                        temp,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}

@Composable
fun DetailedInfoSection() {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            InfoItem("💧", "습도", "65%")
            InfoItem("💨", "풍속", "3 m/s")
            InfoItem("😎", "자외선", "보통")
        }
    }
}

@Composable
fun InfoItem(icon: String, label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(icon, fontSize = 24.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, fontSize = 12.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, fontWeight = FontWeight.Bold)
    }
}


@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    // MaterialTheme으로 감싸주면 미리보기가 더 정확해집니다.

    // 2. Create an instance of the ViewModel using the fake UseCases
    val fakeViewModel = WeatherViewModel(
        weatherMode = WeatherMode.WRITE,
        id = -1L,
    )

    MaterialTheme {
        WeatherScreen(
            navController = rememberNavController(),
            viewModel = fakeViewModel
        )
    }
}