import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import controller.LoginController
import controller.NavController
import controller.WeatherController
import db.DatabaseConnection
import kotlinx.coroutines.launch
import repository.Repository
import ui.*
import util.AuthenticationService
import util.Lce

val CustomShapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(12.dp)
)

private val DarkColorPalette = darkColors(
    primary = Color.White,
    primaryVariant = Color.White,
    secondary = Color.LightGray,
    background = Color.DarkGray,
    surface = Color.Black,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
)

private val LightColorPalette = lightColors(
    primary = Color.Black,
    primaryVariant = Color.Black,
    secondary = Color.DarkGray,
    background = Color.LightGray,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black,
)

fun main() = application {

    val isDarkTheme = remember { mutableStateOf(true) }
    val db = DatabaseConnection()
    val repository = Repository(db)
    val authService = AuthenticationService(db)
    val navController = NavController()
    val loginController = LoginController(authService, navController, repository)
    val weatherController = WeatherController(repository, authService)

    val authenticationService = AuthenticationService(db)

    Window(
        state = rememberWindowState(size = DpSize(800.dp, 1000.dp)),
        onCloseRequest = ::exitApplication,
        title = "Weather app"
    ) {
        val scaffoldState = rememberScaffoldState()

        MaterialTheme(
            colors = if (isDarkTheme.value) DarkColorPalette else LightColorPalette,
            typography = MaterialTheme.typography,
            shapes = CustomShapes
        ) {
            when (navController.currentScreen.value) {
                NavController.Screen.LOGIN -> {
                    Scaffold(
                        Modifier.fillMaxSize(),
                        scaffoldState = scaffoldState
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top,
                            modifier = Modifier.fillMaxSize().padding(24.dp)
                        ) {
                            Text("Welcome to Weather App!", style = MaterialTheme.typography.h4)
                        }
                        LoginScreen(navController, loginController)
                    }
                }

                NavController.Screen.REGISTER -> {
                    Scaffold(
                        Modifier.fillMaxSize(),
                        scaffoldState = scaffoldState
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top,
                            modifier = Modifier.fillMaxSize().padding(24.dp)
                        ) {
                            Text("Welcome to Weather App!", style = MaterialTheme.typography.h4)
                        }
                        RegisterScreen(navController, loginController)
                    }
                }

                NavController.Screen.HOME -> {
                    val backgroundColor = Color.LightGray

                    var queriedCity by remember {
                        mutableStateOf(
                            weatherController.getUserById(authenticationService.getAuthenticatedUserId())?.location
                                ?: ""
                        )
                    }
                    val scope = rememberCoroutineScope()

                    LaunchedEffect(authenticationService.getAuthenticatedUserId()) {
                        if (authenticationService.getAuthenticatedUserId() > 0) {
                            weatherController.setState(Lce.Loading)
                            val result = weatherController.weatherForCity(queriedCity)
                            weatherController.setState(result)
                        }
                    }

                    Scaffold(
                        Modifier.fillMaxSize(),
                        scaffoldState = scaffoldState
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(color = Color.Black)
                                    .padding(16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .background(color = Color.Red)
                                            .clickable {
                                                loginController.logout()
                                                navController.navigateToLogin()
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.ExitToApp,
                                            "Logout",
                                            tint = Color.White
                                        )
                                    }

                                    val cityTextColor = Color.White

                                    TextField(
                                        modifier = Modifier.padding(16.dp).weight(1f),
                                        value = queriedCity,
                                        singleLine = true,
                                        onValueChange = { queriedCity = it },
                                        placeholder = {
                                            Text("Any city, really...")
                                        },
                                        label = { Text("Search for city") },
                                        leadingIcon = { Icon(Icons.Filled.LocationOn, "Location") },
                                        colors = TextFieldDefaults.textFieldColors(
                                            textColor = cityTextColor,
                                            cursorColor = cityTextColor,
                                            focusedIndicatorColor = cityTextColor,
                                            unfocusedIndicatorColor = cityTextColor
                                        )
                                    )

                                    Button(
                                        onClick = {
                                            weatherController.setState(Lce.Loading)
                                            scope.launch {
                                                val result = weatherController.weatherForCity(queriedCity)
                                                weatherController.setState(result)
                                                if (result is Lce.Content) {
                                                    weatherController.setState(result)
                                                    weatherController.addSearchedCity(queriedCity)
                                                }
                                            }
                                        }
                                    ) {
                                        Icon(Icons.Outlined.Search, "Search")
                                    }
                                }
                            }

                            when (val state = weatherController.weatherState.value) {
                                is Lce.Content -> {
                                    ContentUI(state.data)
                                    Spacer(modifier = Modifier.size(16.dp))
                                    Text("Searched Cities:", style = MaterialTheme.typography.h6)
                                    Spacer(modifier = Modifier.size(8.dp))
                                    LazyRow(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                                    ) {
                                        items(weatherController.getLimitedSearchedCities(5)) { cityHistory ->
                                            Box(
                                                modifier = Modifier
                                                    .height(48.dp)
                                                    .background(Color.Black, shape = RoundedCornerShape(8.dp))
                                                    .padding(8.dp)
                                            ) {
                                                Button(
                                                    onClick = {
                                                        scope.launch {
                                                            val result =
                                                                weatherController.weatherForCity(cityHistory.city)
                                                            if (result is Lce.Content) {
                                                                weatherController.setState(result)
                                                            }
                                                        }
                                                    },
                                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
                                                    elevation = ButtonDefaults.elevation(
                                                        defaultElevation = 0.dp,
                                                        pressedElevation = 0.dp
                                                    ),
                                                    contentPadding = PaddingValues(8.dp)
                                                ) {
                                                    Text(
                                                        cityHistory.city,
                                                        style = MaterialTheme.typography.body1,
                                                        color = Color.White
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                is Lce.Error -> ErrorUI()
                                Lce.Loading -> LoadingUI()
                                else -> {}
                            }

                        }
                    }
                }
            }
        }
    }
}


