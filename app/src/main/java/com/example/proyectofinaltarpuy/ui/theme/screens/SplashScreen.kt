package com.example.proyectofinaltarpuy.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.proyectofinaltarpuy.R

@Composable
fun SplashScreen(onNavigateToInicio: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Green)
    ) {
        Image(
            painter = painterResource(id = R.drawable.pantalla),
            contentDescription = stringResource(id = R.string.logo_description),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }

    // Lógica para navegar después de 3 segundos
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(3000)
        onNavigateToInicio()
    }
}
