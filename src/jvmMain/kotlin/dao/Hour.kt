package dao

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Hour(
    @SerialName("feelslike_c") val feelsLikeC: Double,
    @SerialName("chance_of_rain") val chanceOfRain: Int,
)
