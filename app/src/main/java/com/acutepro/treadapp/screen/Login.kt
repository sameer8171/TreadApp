package com.acutepro.treadapp.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.acutepro.common.showToast
import com.acutepro.treadapp.navigation.Routes
import com.acutepro.treadapp.viewModels.AuthViewModel

@Composable
fun Login(navHostController: NavHostController) {

    val authViewModel: AuthViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState(null)

    val error by authViewModel.error.observeAsState()

    val context = LocalContext.current

    error?.let {
        context.showToast(it)
    }

    LaunchedEffect(firebaseUser) {
        if (firebaseUser != null) {
            navHostController.navigate(Routes.BottomNav.routes) {
                popUpTo(navHostController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Login",
            style = TextStyle(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp
            )
        )
        Box(modifier = Modifier.height(50.dp))

        OutlinedTextField(value = email, onValueChange = { email = it }, label = {
            Text(text = "Email")
        }, keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email
        ), singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Box(modifier = Modifier.height(20.dp))

        OutlinedTextField(value = password, onValueChange = { password = it }, label = {
            Text(text = "Password")
        }, keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email
        ), singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Box(modifier = Modifier.height(30.dp))

        ElevatedButton(onClick = {

            if (email.isEmpty() || password.isEmpty()) {
                context.showToast("Please fill all details")
            } else {
                authViewModel.login(email, password, context)
            }

        }, Modifier.fillMaxWidth()) {


            Text(
                text = "Login",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ), modifier = Modifier.padding(vertical = 6.dp)
            )
        }

        TextButton(onClick = {
            navHostController.navigate(Routes.Register.routes) {
                popUpTo(navHostController.graph.startDestinationId)
                launchSingleTop = true
            }
        }, Modifier.fillMaxWidth()) {

            Text(
                text = "New User? Create Account",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            )
        }

    }


}


@Preview(showBackground = true)
@Composable
fun LoginView() {
//    Login()
}