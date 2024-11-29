package com.example.proyectofinaltarpuy.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.proyectofinaltarpuy.ui.theme.ProyectoFinalTarpuyTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.proyectofinaltarpuy.R
import com.example.proyectofinaltarpuy.viewmodels.RegisterViewModel

@Composable
fun RegisterScreen(navController: NavHostController, viewModel: RegisterViewModel = hiltViewModel()) {
    var fullName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val myViewModel: RegisterViewModel = viewModel()
    val isRegistrationSuccessful = viewModel.isRegistrationSuccessful.value
    val errorMessage = viewModel.errorMessage.observeAsState().value

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.background_image),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Surface(
            modifier = Modifier
                .width(450.dp) // Ancho fijo para el formulario
                .padding(20.dp),
            shape = RoundedCornerShape(16.dp), // Esquinas redondeadas
            color = Color.White.copy(alpha = 0.5f) // Fondo negro medio transparente
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center // Centrar verticalmente
            ) {
                Text(
                    text = "Registro",
                    fontSize = 30.sp,
                    color = Color.Black, // Color del texto
                    modifier = Modifier.padding(top = 20.dp, bottom = 24.dp),
                )

                // Full Name Input
                BasicTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, color = Color.Black),
                    decorationBox = { innerTextField ->
                        if (fullName.isEmpty()) Text("Nombre completo", color = Color.Gray)
                        innerTextField()
                    }
                )
                HorizontalDivider(thickness = 1.dp, color = Color.Black) // Línea negra debajo

                Spacer(modifier = Modifier.height(12.dp))

                // Username Input
                BasicTextField(
                    value = username,
                    onValueChange = { username = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, color = Color.Black),
                    decorationBox = { innerTextField ->
                        if (username.isEmpty()) Text("Nombre de usuario", color = Color.Gray)
                        innerTextField()
                    }
                )
                HorizontalDivider(thickness = 1.dp, color = Color.Black) // Línea negra debajo

                Spacer(modifier = Modifier.height(12.dp))

                // Email Input
                BasicTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, color = Color.Black),
                    decorationBox = { innerTextField ->
                        if (email.isEmpty()) Text("Correo electrónico", color = Color.Gray)
                        innerTextField()
                    }
                )
                HorizontalDivider(thickness = 1.dp, color = Color.Black) // Línea negra debajo

                Spacer(modifier = Modifier.height(12.dp))

                // Password Input
                BasicTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, color = Color.Black),
                    visualTransformation = PasswordVisualTransformation(),
                    decorationBox = { innerTextField ->
                        if (password.isEmpty()) Text("Contraseña", color = Color.Gray)
                        innerTextField()
                    }
                )
                HorizontalDivider(thickness = 1.dp, color = Color.Black) // Línea negra debajo

                Spacer(modifier = Modifier.height(12.dp))

                // Confirm Password Input
                BasicTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, color = Color.Black),
                    visualTransformation = PasswordVisualTransformation(),
                    decorationBox = { innerTextField ->
                        if (confirmPassword.isEmpty()) Text("Confirmar contraseña", color = Color.Gray)
                        innerTextField()
                    }
                )
                HorizontalDivider(thickness = 1.dp, color = Color.Black) // Línea negra debajo

                Spacer(modifier = Modifier.height(12.dp))

                // Display error message if any
                errorMessage?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                // Register Button
                Button(
                    onClick = {
                        myViewModel.registerUser(fullName, username, email, password, confirmPassword)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8BC34A)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("REGISTRAR", color = Color.Black)
                }
            }
        }
        // Handle successful registration
        LaunchedEffect(isRegistrationSuccessful) {
            if (isRegistrationSuccessful) {
                // Navigate to another screen (e.g., Home or Login screen)
                navController.navigate("map") // Example navigation
            }
        }
    }
}
