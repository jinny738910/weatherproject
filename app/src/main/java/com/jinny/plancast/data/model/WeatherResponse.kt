package com.jinny.plancast.data.model

import com.jinny.plancast.domain.model.WeatherCategory
import com.jinny.plancast.domain.model.WeatherInfo
import com.google.gson.annotations.SerializedName

/**
 * 기상청 API 응답의 가장 바깥쪽 JSON 객체에 해당합니다.
 * "response"라는 키 하나를 가지고 있습니다.
 */
data class WeatherResponse(
    // @SerializedName: JSON 데이터의 키 이름과 Kotlin 클래스의 변수 이름이 다를 경우,
    // 둘을 매핑시켜주는 역할을 합니다. 이름이 같아도 명시적으로 작성하면 더 안전합니다.
    @SerializedName("response")
    val response: Response<Any?>
)

/**
 * "response" 객체 내부에 있는 header와 body를 담는 클래스입니다.
 */
data class Response<T>(
    @SerializedName("header")
    val header: Header,
    @SerializedName("body")
    val body: Body
)

/**
 * API 요청의 성공 여부와 메시지를 담는 header 정보를 나타냅니다.
 * resultCode가 "00"이면 정상 응답입니다.
 */
data class Header(
    @SerializedName("resultCode")
    val resultCode: String,
    @SerializedName("resultMsg")
    val resultMsg: String
)

/**
 * 실제 날씨 예보 데이터의 본문(body)에 해당합니다.
 * 예보 아이템 목록(items)과 페이징 정보를 포함합니다.
 */
data class Body(
    @SerializedName("dataType")
    val dataType: String,
    @SerializedName("items")
    val items: Items,
    @SerializedName("pageNo")
    val pageNo: Int,
    @SerializedName("numOfRows")
    val numOfRows: Int,
    @SerializedName("totalCount")
    val totalCount: Int
)

/**
 * 날씨 예보 아이템들을 리스트(배열) 형태로 담고 있는 클래스입니다.
 * JSON에서 "items": { "item": [...] } 와 같은 구조를 표현합니다.
 */
data class Items(
    // API 응답에서 item은 배열(리스트) 형태이므로 List<Item>으로 선언합니다.
    @SerializedName("item")
    val item: List<Item>
)

/**
 * 가장 중요한 실제 날씨 예보 하나하나의 정보를 담는 클래스입니다.
 * 기온, 습도, 강수확률 등 개별 데이터가 여기에 포함됩니다.
 */
data class Item(
    @SerializedName("baseDate")
    val baseDate: String,       // 발표일자 (e.g., "20250903")
    @SerializedName("baseTime")
    val baseTime: String,       // 발표시각 (e.g., "1400")
    @SerializedName("category")
    val category: String,       // 자료구분코드 (e.g., "T1H", "RN1", "SKY")
    @SerializedName("fcstDate")
    val fcstDate: String,       // 예보일자 (e.g., "20250903")
    @SerializedName("fcstTime")
    val fcstTime: String,       // 예보시각 (e.g., "1500")
    @SerializedName("fcstValue")
    val fcstValue: String,      // 예보값 (문자열: 숫자, "강수없음", "1.0mm" 등)
    @SerializedName("nx")
    val nx: Int,                // X좌표
    @SerializedName("ny")
    val ny: Int                 // Y좌표
)

/**
 * ⭐️ 클린 아키텍처의 핵심: 데이터 변환(매핑) 함수 ⭐️
 *
 * 데이터 계층의 모델(Item)을 도메인 계층의 모델(WeatherInfo)로 변환하는 확장 함수입니다.
 * 이 과정을 통해 앱의 핵심 로직(도메인 계층)은 API의 복잡한 구조에 대해 전혀 알 필요가 없어집니다.
 * 만약 나중에 기상청 API의 응답 형식이 바뀌더라도, 이 변환 함수와 데이터 모델만 수정하면 되므로
 * 앱의 다른 부분에 미치는 영향을 최소화할 수 있습니다.
 */
fun Item.toDomain(): WeatherInfo {
    val numericValue: Double = parseForecastValueSafely(category = category, raw = fcstValue)
    return WeatherInfo(
        // "T1H"와 같은 문자열 코드를 우리가 정의한 WeatherCategory Enum 타입으로 변환합니다.
        // 이를 통해 코드의 안정성과 가독성이 크게 향상됩니다.
        category = WeatherCategory.fromCode(this.category),
        forecastDate = this.fcstDate,
        forecastTime = this.fcstTime,
        forecastValue = numericValue
    )
}

/**
 * 기상청 "fcstValue" 문자열을 안전하게 Double로 변환합니다.
 * - "강수없음" 같은 비수치 값을 0.0으로 해석
 * - "1mm", "0.5mm" 등 단위를 포함한 값에서 숫자만 추출
 * - "-" 등 비어있거나 알 수 없는 값은 0.0
 */
private fun parseForecastValueSafely(category: String, raw: String): Double {
    val trimmed = raw.trim()
    if (trimmed.isEmpty() || trimmed == "-" || trimmed == "강수없음") return 0.0

    // mm, cm, %, m/s 등 단위 제거 및 숫자, 부호, 소수점만 남김
    val numericPart = trimmed
        .replace("mm", "", ignoreCase = true)
        .replace("cm", "", ignoreCase = true)
        .replace("%", "", ignoreCase = true)
        .replace("m/s", "", ignoreCase = true)
        .replace("m", "", ignoreCase = true)
        .replace("deg", "", ignoreCase = true)
        .trim()

    return numericPart.toDoubleOrNull() ?: 0.0
}