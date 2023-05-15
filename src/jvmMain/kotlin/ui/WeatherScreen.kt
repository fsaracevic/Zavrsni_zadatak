import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import controller.LoginController
import controller.NavController
import controller.WeatherController
import kotlinx.coroutines.launch
import ui.ContentUI
import ui.ErrorUI
import ui.LoadingUI
import util.AuthenticationService
import util.Lce

@Composable
@Preview
fun WeatherScreen(
    navController: NavController,
    authenticationService: AuthenticationService,
    weatherController: WeatherController,
    loginController: LoginController
) {
    val backgroundColor = Color.LightGray

    var queriedCity by remember {
        mutableStateOf(
            weatherController.getUserById(authenticationService.getAuthenticatedUserId())?.location ?: ""
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

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor
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
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(weatherController.getLimitedSearchedCities(5)) { cityHistory ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        scope.launch {
                                            val result = weatherController.weatherForCity(cityHistory.city)
                                            weatherController.setState(result)
                                        }
                                    },
                                elevation = 4.dp,
                                shape = MaterialTheme.shapes.medium
                            ) {
                                Box(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth()
                                ) {
                                    Text(
                                        text = cityHistory.city,
                                        style = MaterialTheme.typography.body1
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

