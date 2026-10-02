package com.example.prueba.data.model

/**
 * Dispositivo localizado en la view BuscarDispositivo mediante geolocalización.
 * Se persiste en Firebase Firestore.
 *
 * @param id        Identificador del documento en Firestore.
 * @param uid       Identificador del usuario dueño del registro.
 * @param nombre    Nombre asignado al dispositivo.
 * @param latitud   Latitud de la última ubicación conocida.
 * @param longitud  Longitud de la última ubicación conocida.
 * @param timestamp Marca de tiempo del registro (epoch millis).
 */
data class Dispositivo(
    var id: String = "",
    var uid: String = "",
    var nombre: String = "",
    var latitud: Double = 0.0,
    var longitud: Double = 0.0,
    var timestamp: Long = 0L
)
