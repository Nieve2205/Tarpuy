package com.example.proyectofinaltarpuy.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

    fun onSplashTimeout(onNavigateToInicio: () -> Unit) {
        viewModelScope.launch {
            delay(3000) // Espera de 3 segundos
            onNavigateToInicio()
        }
    }
}
