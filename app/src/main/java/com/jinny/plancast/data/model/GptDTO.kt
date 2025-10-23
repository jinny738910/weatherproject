package com.jinny.plancast.data.model

import com.squareup.moshi.JsonClass

// 메시지 객체: 역할(role)과 내용(content)을 포함
@JsonClass(generateAdapter = true)
data class GptMessage(
    val role: String, // "system", "user", "assistant" 중 하나
    val content: String
)

// API 요청 본문 (Request Body)
@JsonClass(generateAdapter = true)
data class GptRequest(
    val model: String, // 사용할 모델명 (예: gpt-4o-mini)
    val messages: List<GptMessage>
)

// API 응답 객체 (Response)
@JsonClass(generateAdapter = true)
data class GptResponse(
    val choices: List<Choice>
)

// 응답의 선택지 (Choice)
@JsonClass(generateAdapter = true)
data class Choice(
    val message: GptMessage
)