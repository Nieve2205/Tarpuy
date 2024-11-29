package com.example.proyectofinaltarpuy.ui.theme.screens

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.proyectofinaltarpuy.viewmodels.MapViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import java.util.Locale
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(viewModel: MapViewModel) {
    val cityName by viewModel.cityName.collectAsState()
    val weatherInfo by viewModel.weatherInfo.collectAsState()

    var query by remember { mutableStateOf(TextFieldValue("")) }
    val context = LocalContext.current

    val mapState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-12.0464, -77.0428), 10f)
    }

    // Recolectar el valor de markerPosition como un LatLng
    val markerPosition by viewModel.markerPosition.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Seleccionar Ubicación") }) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("Buscar ubicación") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                    val geocoder = Geocoder(context, Locale.getDefault())
                    val addresses = geocoder.getFromLocationName(query.text, 1)
                    if (addresses != null && addresses.isNotEmpty()) {
                        val address = addresses[0]
                        val latLng = LatLng(address.latitude, address.longitude)
                        viewModel.updateCityName(address.locality ?: "Ciudad desconocida")
                        viewModel.fetchWeather(address.locality ?: "")

                        // Mueve la cámara utilizando un CameraUpdate
                        mapState.move(CameraUpdateFactory.newCameraPosition(CameraPosition(latLng, 15f, 0f, 0f)))
                    }
                }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8BC34A)),
                    ) {
                    Text("Buscar", color = Color.Black)
                }

                Spacer(modifier = Modifier.height(16.dp))

                GoogleMap(
                    modifier = Modifier.weight(1f),
                    cameraPositionState = mapState,
                    properties = MapProperties(isMyLocationEnabled = true),
                    uiSettings = MapUiSettings(zoomControlsEnabled = false),
                    onMapClick = { latLng ->
                        val city = viewModel.getCityNameFromLocation(context, latLng)
                        viewModel.updateCityName(city)
                        viewModel.updateMarkerPosition(latLng)
                    }
                ) {
                    // Verifica que markerPosition no sea nulo antes de usarlo
                    markerPosition?.let { latLng ->
                        Marker(
                            state = MarkerState(position = latLng),
                            title = "Ciudad seleccionada"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text("Ciudad seleccionada: $cityName")

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { "MainScreen" },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8BC34A)),
                    ) {
                    Text("Aceptar", color = Color.Black)
                }
            }
        }
    )
}

private fun enableMyLocation(context: android.content.Context, googleMap: GoogleMap?) {
    if (googleMap != null) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.isMyLocationEnabled = true
        }
    } else {
        // Muestra un mensaje de error si googleMap es null
        Log.e("MapScreen", "GoogleMap instance is null")
    }
}

