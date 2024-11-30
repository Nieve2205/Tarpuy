package com.example.proyectofinaltarpuy.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.launch

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val username: String) : LoginState()
    data class Error(val message: String) : LoginState()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> get() = _loginState

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _loginState.value = LoginState.Error("Por favor, completa todos los campos")
            return
        }

        _loginState.value = LoginState.Loading

        viewModelScope.launch {
            try {
                // Iniciar sesión con Firebase Auth
                auth.signInWithEmailAndPassword(email, password).await()

                val userId = auth.currentUser?.uid
                if (userId != null) {
                    // Obtener el nombre de usuario desde Firestore
                    val document = firestore.collection("users").document(userId).get().await()
                    val username = document.getString("username") ?: "usuario"
                    _loginState.value = LoginState.Success(username)
                } else {
                    _loginState.value = LoginState.Error("Error al obtener el ID del usuario")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Error: ${e.message}")
            }
        }
    }
}
