package com.example.prueba.data

/**
 * Modelo de datos de un usuario registrado en la aplicación.
 *
 * @param nombre        Nombre completo del usuario.
 * @param correo        Correo electrónico (se usa como identificador de acceso).
 * @param contrasena    Contraseña de acceso.
 * @param tipoUsuario   Tipo de usuario seleccionado (combo box): "Persona sorda",
 *                      "Familiar / cuidador" o "Intérprete".
 * @param medioContacto Medio de comunicación preferido (radio buttons): "Texto" o "Voz".
 * @param aceptaTerminos Indica si aceptó los términos y condiciones (check list).
 */
data class Usuario(
    val nombre: String,
    val correo: String,
    val contrasena: String,
    val tipoUsuario: String,
    val medioContacto: String,
    val aceptaTerminos: Boolean
)
