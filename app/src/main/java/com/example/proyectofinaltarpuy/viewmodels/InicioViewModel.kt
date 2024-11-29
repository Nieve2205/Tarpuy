package com.example.proyectofinaltarpuy.viewmodels

import androidx.lifecycle.ViewModel

class InicioViewModel : ViewModel() {
    fun navigateToLogin(onNavigate: () -> Unit) {
        onNavigate()
    }

    fun navigateToRegister(onNavigate: () -> Unit) {
        onNavigate()
    }
}
