package ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import controller.LoginController
import controller.NavController
import util.Lce

@Composable
fun LoginScreen(navController: NavController, loginController: LoginController) {

    var textFieldUsernameState by remember { mutableStateOf("") }
    var textFieldPasswordState by remember { mutableStateOf("") }

    when(val state = loginController.uiState.value){
        is Lce.Content -> TODO()
        is Lce.Error -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize().padding(24.dp)
            ) {
                Text(text = state.error.message?: "Unknown error!")
                Button(
                    onClick ={
                        loginController.setState(null)
                    }
                ){
                    Text("Back")
                }
            }
        }
        Lce.Loading -> LoadingUI()
        null -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize().padding(24.dp)
            ) {
                TextField(
                    value = textFieldUsernameState,
                    onValueChange = {
                        textFieldUsernameState = it
                    },
                    label = {
                        Text("Enter your username")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.size(16.dp))
                TextField(
                    value = textFieldPasswordState,
                    onValueChange = {
                        textFieldPasswordState = it
                    },
                    label = {
                        Text("Enter your password")
                    },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.size(16.dp))
                Row {
                    Button(
                        onClick = {
                            loginController.onLogin(textFieldUsernameState, textFieldPasswordState)
                        }
                    ){
                        Text("Login")
                    }
                    ClickableText(
                        modifier = Modifier.align(Alignment.Bottom).padding(16.dp),
                        text = AnnotatedString("or register"),
                        onClick = {
                            navController.navigateToRegister()
                        }
                    )
                }
            }
        }
    }


}