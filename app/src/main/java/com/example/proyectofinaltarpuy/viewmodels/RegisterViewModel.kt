package com.example.proyectofinaltarpuy.viewmodels

import android.util.Patterns
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // LiveData para mostrar los mensajes de error
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    // Estado para saber si el registro fue exitoso
    val isRegistrationSuccessful = mutableStateOf(false)

    // Función de registro
    fun registerUser(fullName: String, username: String, email: String, password: String, confirmPassword: String) {
        // Validaciones
        if (fullName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            _errorMessage.value = "Todos los campos son obligatorios"
            return
        }

        // Validar nombre completo
        if (!fullName.contains(" ")) {
            _errorMessage.value = "Ingresa tu nombre completo (al menos dos palabras)"
            return
        }

        // Validar correo electrónico
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _errorMessage.value = "Ingresa un email válido"
            return
        }

        // Validar contraseña
        if (password.length < 8 || !password.matches(Regex(".*[A-Z].*")) ||
            !password.matches(Regex(".*[a-z].*")) || !password.matches(Regex(".*\\d.*")) ||
            !password.matches(Regex(".*[!@#\$%^&*(),.?\":{}|<>].*"))
        ) {
            _errorMessage.value = "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial"
            return
        }

        // Validar que las contraseñas coincidan
        if (password != confirmPassword) {
            _errorMessage.value = "Las contraseñas no coinciden"
            return
        }

        // Validar si el nombre de usuario ya está registrado
        firestore.collection("users").whereEqualTo("username", username).get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    _errorMessage.value = "El nombre de usuario ya está en uso"
                } else {
                    // Crear usuario en Firebase Authentication
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val userId = auth.currentUser?.uid
                                val user = hashMapOf("username" to username, "fullName" to fullName, "email" to email)

                                userId?.let {
                                    // Guardar los datos del usuario en Firestore
                                    firestore.collection("users").document(userId).set(user)
                                        .addOnSuccessListener {
                                            _errorMessage.value = "Registro exitoso"
                                            isRegistrationSuccessful.value = true // Cambiar estado de éxito
                                        }
                                        .addOnFailureListener {
                                            _errorMessage.value = "Error al guardar el usuario"
                                        }
                                }
                            } else {
                                _errorMessage.value = "Error en el registro: ${task.exception?.message}"
                            }
                        }
                }
            }
            .addOnFailureListener {
                _errorMessage.value = "Error al validar el nombre de usuario"
            }
    }
}
