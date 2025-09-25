package com.jinny.plancast.domain.model


sealed class WarningResult {

    data class Success(
        val isRaining: Boolean,
        val isFarAway: Boolean
    ): WarningResult()

    object Error: WarningResult()
}