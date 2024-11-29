package com.example.proyectofinaltarpuy.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectofinaltarpuy.R
import androidx.compose.ui.layout.ContentScale

@Composable
fun InicioScreen(onNavigateToLogin: () -> Unit, onNavigateToRegister: () -> Unit) {
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
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(100.dp))
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = stringResource(id = R.string.logo_description),
                modifier = Modifier.size(150.dp)
            )
            Spacer(modifier = Modifier.height(60.dp))
            Text(
                text = "CULTIVO SANO Y SEGURO",
                fontSize = 50.sp,
                textAlign = TextAlign.Center,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(500.dp))
            Button(
                onClick = { onNavigateToLogin() },
                modifier = Modifier.size(width = 170.dp, height = 50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8BC34A)),
                shape = RoundedCornerShape(30.dp),
                contentPadding = PaddingValues()
            ) {
                Text("INICIA SESIÓN", color = Color.Black, fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.White)) {
                        append("¿Aun no tienes cuenta? ")
                    }
                    withStyle(style = SpanStyle(color = Color.Red)) {
                        append("Registrate")
                    }
                },
                fontSize = 20.sp,
                modifier = Modifier.clickable { onNavigateToRegister() }
            )
        }
    }

}
