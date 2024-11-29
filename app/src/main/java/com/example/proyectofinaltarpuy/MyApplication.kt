package com.example.proyectofinaltarpuy

import android.app.Application
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Inicializar Firebase
        FirebaseApp.initializeApp(this)
    }
}
