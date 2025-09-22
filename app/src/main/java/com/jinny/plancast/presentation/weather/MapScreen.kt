
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState


@Composable
fun MapScreen(placeId: String?) {
    // ✅ 여기서 maps_local.show_on_map(places = listOf("place_id://$placeId")) API를 호출하여
    //    실제 지도를 표시할 수 있습니다.
    // 아래는 전달받은 ID를 기반으로 위치를 표시하는 UI 코드 예시입니다.

    // TODO: placeId를 사용하여 ViewModel에서 실제 위치 정보(LatLng)를 가져옵니다.
    val location = LatLng(48.8584, 2.2945) // 더미 데이터 (에펠탑)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location, 15f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = MarkerState(position = location),
            title = "선택된 위치",
//            snippet = "Place ID: $placeId"
            snippet = "Place ID: ChIJ0ay8ZjShfDURq_YqSP_HC7c"
        )
    }
}