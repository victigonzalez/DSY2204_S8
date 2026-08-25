package com.example.prueba.data

import androidx.compose.runtime.mutableStateListOf

/**
 * Repositorio en memoria que mantiene el arreglo (array) de usuarios.
 *
 * Cumple el requerimiento: "deberá contemplar un array que almacene los datos,
 * desde la view de Registro, de 5 usuarios con sus respectivas contraseñas de acceso."
 *
 * Se inicializa con 5 usuarios de ejemplo y admite el registro de nuevos usuarios
 * desde la pantalla de Registro. Al ser 'object' funciona como una única instancia
 * compartida entre todas las views.
 */
object UsuarioRepository {

    // Array/lista observable de usuarios. Precargado con 5 usuarios y contraseñas.
    val usuarios = mutableStateListOf(
        Usuario("Ana Torres",    "ana@correo.cl",    "Ana12345",    "Persona sorda",       "Texto", true),
        Usuario("Bruno Díaz",    "bruno@correo.cl",  "Bruno2024",   "Familiar / cuidador", "Voz",   true),
        Usuario("Carla Rojas",   "carla@correo.cl",  "Carla*789",   "Intérprete",          "Texto", true),
        Usuario("Diego Muñoz",   "diego@correo.cl",  "Diego#456",   "Persona sorda",       "Texto", true),
        Usuario("Elena Pérez",   "elena@correo.cl",  "Elena!321",   "Familiar / cuidador", "Voz",   true)
    )

    /** Agrega un nuevo usuario al array desde la view de Registro. */
    fun registrar(usuario: Usuario) {
        usuarios.add(usuario)
    }

    /** Valida credenciales para el Login. */
    fun validarCredenciales(correo: String, contrasena: String): Boolean {
        return usuarios.any { it.correo.equals(correo, ignoreCase = true) && it.contrasena == contrasena }
    }

    /** Indica si un correo ya existe (para Registro y Recuperar contraseña). */
    fun existeCorreo(correo: String): Boolean {
        return usuarios.any { it.correo.equals(correo, ignoreCase = true) }
    }
}
