package com.example.prueba.data.model

/**
 * Perfil de usuario almacenado en Firebase Firestore.
 *
 * Complementa la autenticación (Firebase Auth) guardando los datos de perfil
 * que el formulario de Registro captura y que no forman parte de las
 * credenciales. Permite el CRUD para la gestión de usuarios.
 *
 * @param uid           Identificador del usuario en Firebase Auth.
 * @param nombre        Nombre completo.
 * @param correo        Correo electrónico.
 * @param tipoUsuario   Tipo de usuario (Persona sorda, Familiar, Intérprete).
 * @param medioContacto Medio de comunicación preferido (Texto o Voz).
 */
data class PerfilUsuario(
    var uid: String = "",
    var nombre: String = "",
    var correo: String = "",
    var tipoUsuario: String = "",
    var medioContacto: String = ""
)
