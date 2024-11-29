package com.example.proyectofinaltarpuy.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

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
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        firestore.collection("users").document(userId).get()
                            .addOnSuccessListener { document ->
                                val username = document.getString("username") ?: "usuario"
                                _loginState.value = LoginState.Success(username)
                            }
                            .addOnFailureListener { e ->
                                _loginState.value = LoginState.Error("Error al recuperar el nombre de usuario: ${e.message}")
                            }
                    }
                } else {
                    _loginState.value = LoginState.Error("Email o contraseña incorrectos")
                }
            }
    }
}
