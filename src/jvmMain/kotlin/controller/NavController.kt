package controller

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

class NavController {

    private val _currentScreen = mutableStateOf(Screen.LOGIN)
    val currentScreen: State<Screen> = _currentScreen

    enum class Screen {
        LOGIN,
        REGISTER,
        HOME
    }

    fun navigateToHome(){
        _currentScreen.value = Screen.HOME
    }

    fun navigateToLogin(){
        _currentScreen.value = Screen.LOGIN
    }

    fun navigateToRegister(){
        _currentScreen.value = Screen.REGISTER
    }

}