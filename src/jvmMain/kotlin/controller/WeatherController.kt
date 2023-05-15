package controller

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import model.CityHistory
import model.User
import model.WeatherResults
import repository.Repository
import util.AuthenticationService
import util.Lce

class WeatherController(
    private val repository: Repository,
    private val authService: AuthenticationService
) {

    private val _weatherState = mutableStateOf<Lce<WeatherResults>?>(null)
    val weatherState: State<Lce<WeatherResults>?> get() = _weatherState

    private val _searchedCities = mutableStateListOf<CityHistory>()
    val searchedCities: List<CityHistory> get() = _searchedCities

    fun setState(newState: Lce<WeatherResults>?) {
        _weatherState.value = newState
    }

    suspend fun weatherForCity(queriedCity: String): Lce<WeatherResults> {
        val result = repository.weatherForCity(queriedCity)
        if (result is Lce.Content) {
            addSearchedCity(queriedCity)
        }
        return result
    }

    fun getUserById(id: Int): User? {
        return repository.getUserById(id)
    }

    fun addSearchedCity(city: String) {
        if (_searchedCities.any { it.city == city }) {
            _searchedCities.removeAll { it.city == city }
        }
        _searchedCities.add(CityHistory(city))
    }

    fun getLimitedSearchedCities(limit: Int): List<CityHistory> {
        return _searchedCities.takeLast(limit)
    }
}
