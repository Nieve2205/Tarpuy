package com.example.proyectofinaltarpuy.ui.theme.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectofinaltarpuy.R
import com.example.proyectofinaltarpuy.viewmodels.LoginState
import com.example.proyectofinaltarpuy.viewmodels.LoginViewModel
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: (String) -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val loginState = viewModel.loginState.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.background_image),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(50.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Bienvenido a Tarpuy",
                fontSize = 50.sp,
                color = Color(0xFF142C2B),
                modifier = Modifier.padding(bottom = 32.dp),
                textAlign = TextAlign.Center
            )

            // Email Input
            BasicTextField(
                value = email.value,
                onValueChange = { email.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 20.sp, color = Color.Black),
                decorationBox = { innerTextField ->
                    if (email.value.isEmpty()) Text("Correo electrónico", color = Color.Black)
                    innerTextField()
                }
            )
            Divider(color = Color.Black, thickness = 1.dp) // Línea negra debajo

            Spacer(modifier = Modifier.height(16.dp))

            // Password Input
            BasicTextField(
                value = password.value,
                onValueChange = { password.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 20.sp, color = Color.Black),
                visualTransformation = PasswordVisualTransformation(),
                decorationBox = { innerTextField ->
                    if (password.value.isEmpty()) Text("Contraseña", color = Color.Black)
                    innerTextField()
                }
            )
            Divider(color = Color.Black, thickness = 1.dp) // Línea negra debajo

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.login(email.value, password.value) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8BC34A)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("INICIO SESIÓN", color = Color.Black)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "¿Olvidaste la contraseña?",
                color = Color.Black,
                modifier = Modifier.clickable { onForgotPasswordClick() }
            )
        }
    }

    when (val state = loginState.value) {
        is LoginState.Success -> onLoginSuccess(state.username)
        is LoginState.Error -> Toast.makeText(LocalContext.current, state.message, Toast.LENGTH_SHORT).show()
        is LoginState.Loading -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        LoginState.Idle -> Unit
    }
}
