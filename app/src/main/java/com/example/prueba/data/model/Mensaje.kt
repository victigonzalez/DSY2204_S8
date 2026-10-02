package com.example.prueba.data.model

/**
 * Mensaje generado en las views Escribir (texto que se convierte a voz) y
 * Hablar (voz que se transcribe a texto). Se persiste en Firebase Firestore.
 *
 * Firestore requiere un constructor vacío y propiedades var para poder
 * deserializar los documentos automáticamente (toObject()).
 *
 * @param id        Identificador del documento en Firestore.
 * @param uid       Identificador del usuario dueño del mensaje.
 * @param texto     Contenido del mensaje.
 * @param tipo      Origen del mensaje: "escrito" o "hablado".
 * @param timestamp Marca de tiempo de creación (epoch millis).
 */
data class Mensaje(
    var id: String = "",
    var uid: String = "",
    var texto: String = "",
    var tipo: String = "escrito",
    var timestamp: Long = 0L
)
