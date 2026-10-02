package com.example.prueba.util

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.tasks.await

/**
 * Administrador de geolocalización basado en Google Play Services (Fused
 * Location Provider).
 *
 * Usado por la view BuscarDispositivo para obtener la ubicación actual del
 * dispositivo. La verificación del permiso ACCESS_FINE_LOCATION se realiza en
 * la capa de UI antes de invocar estas funciones.
 *
 * @param context Contexto de la aplicación.
 */
class LocationManager(context: Context) {

    private val cliente = LocationServices.getFusedLocationProviderClient(context)

    /**
     * Obtiene la ubicación actual como par (latitud, longitud), o null si no
     * se pudo determinar.
     *
     * Requiere que el permiso de ubicación ya haya sido concedido; por eso se
     * suprime la advertencia de permiso faltante.
     */
    @SuppressLint("MissingPermission")
    suspend fun ubicacionActual(): Pair<Double, Double>? {
        return try {
            val location = cliente
                .getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    CancellationTokenSource().token
                )
                .await()
            location?.let { Pair(it.latitude, it.longitude) }
        } catch (e: Exception) {
            null
        }
    }
}
