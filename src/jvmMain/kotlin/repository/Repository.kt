package repository

import dao.WeatherResponse
import db.DatabaseConnection
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import model.User
import model.WeatherResults
import util.Lce
import util.WeatherTransformer

class Repository(
    private val db: DatabaseConnection,
    private val client: HttpClient = defaultHttpClient
) {

    private val apiKey = "10b98aeeebfb4094a41172245230205"

    private val transformer = WeatherTransformer()

    companion object {
        val defaultHttpClient = HttpClient(CIO) {
            install(HttpPlainText) // Install the HTTP plain text feature
        }
    }

    suspend fun weatherForCity(city: String): Lce<WeatherResults> {
        return try {
            val result = getWeatherForCity(city)
            val content = transformer.transform(result)
            Lce.Content(content)
        } catch (e: Exception) {
            e.printStackTrace()
            Lce.Error(e)
        }
    }

    private suspend fun getWeatherForCity(city: String): WeatherResponse {
        val json = Json { ignoreUnknownKeys = true }
        val response: HttpResponse = client.get {
            url {
                protocol = URLProtocol.HTTPS
                host = "api.weatherapi.com"
                encodedPath = "/v1/forecast.json"
                parameters.append("key", apiKey)
                parameters.append("q", city)
                parameters.append("days", "5")
                parameters.append("aqi", "no")
                parameters.append("alerts", "no")
            }
        }

        val jsonContent = response.body<String>()
        return json.decodeFromString(jsonContent)
    }

    fun addUser(username: String, password: String, location: String): Lce<String> {
        return if (db.createNewUser(username, password, location)) {
            Lce.Content("Successfully registered!")
        } else {
            Lce.Error(Throwable("User already exists!"))
        }
    }

    fun getUserById(id: Int): User? {
        return db.getUserById(id)
    }
}
