package com.example.prueba.data

/**
 * Datos capturados por el formulario de Registro, usados tanto para crear el
 * usuario en Firebase como para registrarlo en el arreglo local.
 */
data class PerfilBasico(
    val nombre: String,
    val correo: String,
    val contrasena: String,
    val tipoUsuario: String,
    val medioContacto: String,
    val aceptaTerminos: Boolean
) {
    /** Convierte estos datos en el modelo local [Usuario]. */
    fun aUsuario(): Usuario = Usuario(
        nombre = nombre,
        correo = correo,
        contrasena = contrasena,
        tipoUsuario = tipoUsuario,
        medioContacto = medioContacto,
        aceptaTerminos = aceptaTerminos
    )
}
