package com.jinny.plancast.domain.model


/**
 * 기상청 API에서 제공하는 예보 항목을 나타내는 Enum 클래스
 *
 * @property code API에서 사용하는 카테고리 코드
 * @property description 카테고리 설명
 */
enum class WeatherCategory(val code: String, val description: String) {
    // 초단기예보 & 단기예보 공통
    PTY("PTY", "강수형태"),      // 강수형태 (없음(0), 비(1), 비/눈(2), 눈(3), 소나기(4))
    RN1("RN1", "1시간 강수량"), // 1시간 강수량 (mm)
    SKY("SKY", "하늘상태"),      // 하늘상태 (맑음(1), 구름많음(3), 흐림(4))
    T1H("T1H", "기온"),          // 기온 (°C)
    REH("REH", "습도"),          // 습도 (%)
    UUU("UUU", "동서바람성분"),   // 풍속 (m/s)
    VVV("VVV", "남북바람성분"),   // 풍속 (m/s)
    VEC("VEC", "풍향"),          // 풍향 (deg)
    WSD("WSD", "풍속"),          // 풍속 (m/s)

    // 단기예보 전용
    POP("POP", "강수확률"),      // 강수확률 (%)
    WAV("WAV", "파고"),          // 파고 (M)
    PCP("PCP", "1시간 신강수량"), // 1시간 신강수량 (mm)
    SNO("SNO", "1시간 신적설"),   // 1시간 신적설 (cm)
    TMN("TMN", "일 최저기온"),   // 일 최저기온 (°C)
    TMX("TMX", "일 최고기온"),   // 일 최고기온 (°C)

    UNKNOWN("UNKNOWN", "알 수 없음");

    companion object {
        // 코드값(e.g., "T1H")으로 해당하는 Enum 객체를 찾는 함수
        fun fromCode(code: String): WeatherCategory {
            return entries.find { it.code == code } ?: UNKNOWN
        }
    }
}