package com.example.proyectofinaltarpuy.viewmodels

import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinaltarpuy.repository.SharedPreferencesManager
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor (

) : ViewModel() {
    private val _cityName = MutableStateFlow("")
    val cityName: StateFlow<String> = _cityName

    private val _weatherInfo = MutableStateFlow("")
    val weatherInfo: StateFlow<String> = _weatherInfo

    private val _markerPosition = MutableStateFlow<LatLng?>(null)
    val markerPosition: StateFlow<LatLng?> = _markerPosition

    fun updateMarkerPosition(position: LatLng) {
        _markerPosition.value = position
    }

    fun saveCity(context: Context, city: String) {
        SharedPreferencesManager.saveCity(context, city)
    }

    fun updateCityName(newCity: String) {
        _cityName.value = newCity
    }

    fun fetchWeather(city: String) {
        viewModelScope.launch {
            val apiKey = "AIzaSyAg6Q-ZyAYvmWvVUXE3EpbtgDqCMpaJSNo"
            val url = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$apiKey&units=metric"

            try {
                val client = OkHttpClient()
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    _weatherInfo.value = response.body?.string() ?: "Sin datos de clima"
                } else {
                    _weatherInfo.value = "Error al obtener el clima"
                }
            } catch (e: Exception) {
                _weatherInfo.value = "Error: ${e.message}"
            }
        }
    }

    fun getCityNameFromLocation(context: Context, latLng: LatLng): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        return try {
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            addresses?.firstOrNull()?.locality ?: "Ciudad desconocida"
        } catch (e: Exception) {
            "Ciudad desconocida"
        }
    }

    fun saveCityToFirebase(city: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "default_user"
        FirebaseDatabase.getInstance().getReference("users")
            .child(userId).child("city").setValue(city)
    }
}
