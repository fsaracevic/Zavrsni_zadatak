package dao

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Day(
    @SerialName("avgtemp_c") val averageTempC: Double,
    val condition: Condition
)
