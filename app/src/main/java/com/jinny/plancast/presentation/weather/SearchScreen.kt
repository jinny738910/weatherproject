import android.annotation.SuppressLint
import android.os.Parcel
import android.text.SpannableString
import android.text.style.CharacterStyle
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.jinny.plancast.domain.repository.PlaceRepository
import com.jinny.plancast.presentation.weather.SearchViewModel
import com.jinny.plancast.presentation.weather.WeatherMode


@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel
) {

    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()

    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 2. 지역 이름 입력창 (TextField)
        OutlinedTextField(
            value = searchQuery,
            onValueChange = viewModel::onSearchQueryChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("지역 이름(동/읍/면) 입력...") },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "검색 아이콘"
                )
            },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 3. 현재 위치로 설정 버튼
        Button(
            onClick = { /* TODO: 현재 위치 가져오는 로직 호출 */
                navController.navigate("weather")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                Icons.Default.LocationOn,
                contentDescription = "현재 위치 아이콘",
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
            Text("현재 위치로 설정")
        }

        LazyColumn {
            items(searchResults) { result ->
                SearchResultItem(result = result, onClick = {
                    // 결과 클릭 시, placeId를 지도 화면으로 전달하며 이동
                    navController.navigate("map/${result.placeId}")
                })
            }
        }
    }
}

@Composable
fun SearchResultItem(result: AutocompletePrediction, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp)
    ) {
//        Text(result.primaryText, style = MaterialTheme.typography.bodyLarge)
//        Text(result.secondaryText, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}



//class FakeAutocompletePrediction(
//    private val placeId: String,
//    private val primaryText: String,
//    private val secondaryText: String
//) : AutocompletePrediction() {
//    override fun getPlaceId(): String = placeId
//
//    override fun getPrimaryText(style: CharacterStyle?): SpannableString {
//        return SpannableString(primaryText)
//    }
//
//    override fun getSecondaryText(style: CharacterStyle?): SpannableString {
//        return SpannableString(secondaryText)
//    }
//    // 사용하지 않을 다른 메서드들은 기본값이나 null을 반환하도록 구현
//    override fun getFullText(style: CharacterStyle?): SpannableString = SpannableString("$primaryText, $secondaryText")
//    override fun getPlaceTypes(): List<Place.Type> = emptyList()
//    override fun getTypes(): MutableList<String> = mutableListOf()
//
//    override fun getDistanceMeters(): Int? = null
//    override fun describeContents(): Int = 0
//    override fun writeToParcel(dest: Parcel, flags: Int) {}
//}

//data class PlacePrediction(
//    val placeId: String,
//    val primaryText: String,
//    val secondaryText: String
//)
//
//private class FakeItemRepository : PlaceRepository {
//    private val dummyPredictions = listOf(
//        PlacePrediction("place_id_1", "서울역", "대한민국 서울특별시"),
//        PlacePrediction("place_id_2", "서울 시청", "대한민국 서울특별시")
//    )
//
//    override suspend fun searchPlaces(query: String): Result<List<AutocompletePrediction>> {
//        return if (query == "error") {
//            Result.failure(Exception("네트워크 오류 발생"))
//        } else {
//            Result.success(dummyPredictions)
//        }
//    }
//}
//

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    // MaterialTheme으로 감싸주면 미리보기가 더 정확해집니다.

    // 2. Create an instance of the ViewModel using the fake UseCases
    val fakeViewModel = SearchViewModel(
        weatherMode = WeatherMode.WRITE,
        id = -1L,
//        placeRepository = FakeItemRepository()
    )

    MaterialTheme {
        SearchScreen(
            navController = rememberNavController(),
            viewModel = fakeViewModel
        )
    }
}