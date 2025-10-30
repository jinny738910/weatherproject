package com.jinny.plancast.presentation.chat.chatroom

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ChatRoomScreen(
    chatTitle: String,
    initialMessages: List<MessageItem> = emptyList()
) {
    // 1. 상태 관리: 메시지 목록과 현재 입력 텍스트
    var messages by remember { mutableStateOf(initialMessages.toMutableList()) }
    var inputMessage by remember { mutableStateOf("") }

    // 메시지 전송 로직
    val onSendClick: () -> Unit = {
        if (inputMessage.isNotBlank()) {
            val newMessage = MessageItem(
                userId = "user_123",
                name = "나",
                message = inputMessage,
                isUser = true, // 사용자가 보낸 메시지
                timestamp = "방금"
            )
            messages = (messages + newMessage).toMutableList() // 메시지 목록 업데이트
            inputMessage = "" // 입력창 초기화
            // TODO: 실제 서버 전송 로직 구현
        }
    }

    Scaffold(
        topBar = {
            // 상단 앱 바 (채팅방 제목)
//            TopAppBar(title = { Text(chatTitle) })
        },
        bottomBar = {
            // 하단 입력 창
            MessageInput(
                inputMessage = inputMessage,
                onMessageChange = { inputMessage = it },
                onSendClick = onSendClick
            )
        }
    ) { paddingValues ->
        // 메시지 목록 (메인 콘텐츠 영역)
        MessageList(
            messages = messages,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        )
    }
}

@Composable
fun MessageList(messages: List<MessageItem>, modifier: Modifier = Modifier) {
    // LazyColumn을 사용하여 성능 최적화된 목록을 구현 (RecyclerView와 유사)
    LazyColumn(
        modifier = modifier,
        reverseLayout = true, // 최신 메시지가 아래에 표시되도록 설정 (스크롤 시작점)
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp, alignment = Alignment.Bottom)
    ) {
        // 메시지 리스트를 역순으로 표시 (가장 최근 메시지가 아래)
        items(messages.reversed(), key = { it.message }) { message ->
            MessageBubble(message = message)
        }
    }
}

@Composable
fun MessageBubble(message: MessageItem) {
    // 메시지 버블 정렬 (사용자 메시지는 오른쪽, 상대방 메시지는 왼쪽)
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        // 배경색 및 모양 설정
        val bubbleColor = if (message.isUser) Color(0xFFC8E6C9) else Color(0xFFE0E0E0) // Greenish vs Grayish
        val alignment = if (message.isUser) Alignment.BottomEnd else Alignment.BottomStart

        Column(
            horizontalAlignment = if (message.isUser) Alignment.End else Alignment.Start,
            modifier = Modifier.weight(1f, fill = false) // 내용에 맞춰 너비 조절
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = bubbleColor,
                modifier = Modifier.wrapContentWidth()
            ) {
                Text(
                    text = message.message,
                    modifier = Modifier.padding(10.dp)
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = message.timestamp,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun MessageInput(
    inputMessage: String,
    onMessageChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .heightIn(min = 48.dp), // 최소 높이 설정
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 텍스트 입력 필드
        OutlinedTextField(
            value = inputMessage,
            onValueChange = onMessageChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text("메시지를 입력하세요...") },
            shape = RoundedCornerShape(24.dp), // 둥근 모서리
            singleLine = false, // 여러 줄 입력 가능
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedContainerColor = Color(0xFFF0F0F0),
                unfocusedContainerColor = Color(0xFFF0F0F0)
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        // 전송 버튼
        Button(
            onClick = onSendClick,
            // 텍스트가 비어있으면 버튼 비활성화
            enabled = inputMessage.isNotBlank(),
            shape = RoundedCornerShape(50),
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Send,
                contentDescription = "메시지 전송",
                tint = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewChatScreen() {
    val dummyMessages = listOf(
        MessageItem("1", "나", true, "안녕하세요! Compose로 채팅 UI를 만들고 있어요.", timestamp = "10:00"),
        MessageItem("2", "상대", false, "오, 멋지네요! Parcelable 구현은 하셨나요?",timestamp = "10:00"),
        MessageItem("3", "나", true, "네, @Parcelize로 쉽게 했어요. 이제 데이터 전송만 남았어요.",timestamp = "10:00"),
        MessageItem("4", "상대", false, "화면 구성이 깔끔하네요. \uD83D\uDC4D",timestamp = "10:00"),
    )

    MaterialTheme {
        ChatRoomScreen(
            chatTitle = "Compose 채팅방",
            initialMessages = dummyMessages
        )
    }
}

