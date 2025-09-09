import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SearchScreen(
    // 실제 구현 시에는 ViewModel 등에서 상태를 관리하고,
    // 람다 함수를 파라미터로 받아 이벤트를 처리하는 것이 좋습니다.
) {
    // 1. 텍스트 필드의 상태를 저장하고 기억하는 변수
    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 2. 지역 이름 입력창 (TextField)
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
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
            onClick = { /* TODO: 현재 위치 가져오는 로직 호출 */ },
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
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    MaterialTheme {
        SearchScreen()
    }
}