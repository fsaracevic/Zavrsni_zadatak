package dao

import kotlinx.serialization.Serializable

@Serializable
data class ForecastDay(
    val hour: List<Hour>,
    val day: Day
)
