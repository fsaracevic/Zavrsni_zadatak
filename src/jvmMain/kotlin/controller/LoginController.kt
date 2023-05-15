package controller

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import repository.Repository
import util.AuthenticationService
import util.Lce

class LoginController(
    private val authService: AuthenticationService,
    private val navController: NavController,
    private val repository: Repository
) {
    private val _uiState = mutableStateOf<Lce<String>?>(null)
    val uiState: State<Lce<String>?> get() = _uiState

    fun setState(newState: Lce<String>?){
        _uiState.value = newState
    }

    fun onLogin(username: String, password: String){
        if(authService.authenticate(username, password)) {
            navController.navigateToHome()
        }else{
            setState(Lce.Error(Throwable("Invalid credentials")))
        }
    }

    fun onRegister(username: String, password: String, location: String){
        if(username != "" && password != "") {
            val result = repository.addUser(username, password, location)
            setState(result)
        }else{
            setState(Lce.Error(Throwable("Username and password can't be empty!")))
        }
    }

    fun logout() {
        authService.logout()
    }
}