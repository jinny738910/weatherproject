package com.jinny.plancast.data.api

import com.jinny.plancast.BuildConfig
import com.jinny.plancast.data.model.GptMessage
import com.jinny.plancast.data.model.GptRequest
import com.jinny.plancast.data.model.GptResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class GptApiService {

    // API 키는 BuildConfig를 통해 안전하게 가져옵니다.
    private val apiKey = BuildConfig.GPT_API_KEY
    private val client = OkHttpClient()
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()
    private val apiUrl = "https://api.openai.com/v1/chat/completions"

    /**
     * 특정 할 일(Task)에 대한 사용자 질문에 답변을 생성합니다.
     * @param taskContext 현재 할 일의 맥락 (예: "2박 3일 강원도 캠핑 준비")
     * @param userQuestion 사용자 채팅 입력 (예: "필요한 준비물 체크리스트 만들어줘")
     * @return GPT가 생성한 응답 텍스트
     */
    suspend fun getAssistantResponse(taskContext: String, userQuestion: String): String = withContext(Dispatchers.IO) {

        // 1. System Role 프롬프트: LLM에게 역할과 맥락을 정의
        val systemInstruction = "당신은 사용자 맞춤형 할 일 관리 비서입니다. " +
                "주어진 할 일 맥락($taskContext)을 기반으로 사용자의 질문에 가장 적합한 답변, 조언, 체크리스트, 혹은 계획을 상세하고 친절하게 제공하세요."

        // 2. 메시지 구성: System 메시지와 User 메시지를 배열로 전달
        val messages = listOf(
            GptMessage(role = "system", content = systemInstruction),
            GptMessage(role = "user", content = userQuestion)
        )

        // 3. 요청 본문(Request Body) 생성
        val requestBody = GptRequest(
            model = "gpt-4o-mini", // 사용할 모델 지정 (저렴하고 빠른 모델)
            messages = messages
        )

        val adapter = moshi.adapter(GptRequest::class.java)
        val jsonBody = adapter.toJson(requestBody)

        // 4. HTTP 요청 빌드
        val request = Request.Builder()
            .url(apiUrl)
            // 인증을 위한 API Key를 Authorization 헤더에 Bearer 토큰으로 포함
            .header("Authorization", "Bearer $apiKey")
            .post(jsonBody.toRequestBody(jsonMediaType))
            .build()

        // 5. API 호출 및 응답 처리
        return@withContext try {
            client.newCall(request).execute().use { response ->
                val responseBody = response.body?.string()

                if (!response.isSuccessful || responseBody == null) {
                    return@use "오류: API 호출 실패 (코드 ${response.code}). 응답: $responseBody"
                }

                // JSON 응답 파싱
                val responseAdapter = moshi.adapter(GptResponse::class.java)
                val gptResponse = responseAdapter.fromJson(responseBody)

                // 첫 번째 선택지의 메시지 내용 반환
                gptResponse?.choices?.firstOrNull()?.message?.content ?: "GPT 응답을 파싱할 수 없습니다."
            }
        } catch (e: Exception) {
            "네트워크 오류 발생: ${e.localizedMessage}"
        }
    }
}
