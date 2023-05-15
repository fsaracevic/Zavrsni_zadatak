package dao

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Current(
    @SerialName("temp_c") val tempC: Double,
    val condition: Condition,
    @SerialName("feelslike_c") val feelsLikeC: Double
)
