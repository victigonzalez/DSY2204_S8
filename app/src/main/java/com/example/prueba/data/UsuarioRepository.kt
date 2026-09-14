package com.example.prueba.data

import androidx.compose.runtime.mutableStateListOf

/**
 * Repositorio en memoria que mantiene el arreglo (array) de usuarios de VozApp.
 *
 * Se declara como 'object' (instancia única / singleton) para compartir el
 * mismo arreglo entre todas las views. La lista es observable
 * (mutableStateListOf), de modo que la grilla de la pantalla de Registro se
 * actualiza automáticamente al agregar un usuario nuevo.
 *
 * Las funciones de consulta emplean colecciones y funciones de orden superior
 * de Kotlin (any, filter, map, find, count, sortedBy, firstOrNull), integrando
 * la sintaxis del lenguaje solicitada en la actividad.
 */
object UsuarioRepository {

    /**
     * Array inicial con los 5 usuarios precargados y sus contraseñas.
     * Se usa el tipo Array<Usuario> de Kotlin.
     */
    private val usuariosIniciales: Array<Usuario> = arrayOf(
        Usuario("Ana Torres",  "ana@correo.cl",   "Ana12345",  TipoUsuario.PERSONA_SORDA.etiqueta, "Texto", true),
        Usuario("Bruno Díaz",  "bruno@correo.cl", "Bruno2024", TipoUsuario.FAMILIAR.etiqueta,      "Voz",   true),
        Usuario("Carla Rojas", "carla@correo.cl", "Carla*789", TipoUsuario.INTERPRETE.etiqueta,    "Texto", true),
        Usuario("Diego Muñoz", "diego@correo.cl", "Diego#456", TipoUsuario.PERSONA_SORDA.etiqueta, "Texto", true),
        Usuario("Elena Pérez", "elena@correo.cl", "Elena!321", TipoUsuario.FAMILIAR.etiqueta,      "Voz",   true)
    )

    /** Lista observable que alimenta la interfaz; parte del array inicial. */
    val usuarios = mutableStateListOf(*usuariosIniciales)

    /** Cantidad de usuarios registrados (propiedad calculada). */
    val cantidad: Int
        get() = usuarios.size

    /** Agrega un nuevo usuario al arreglo desde la view de Registro. */
    fun registrar(usuario: Usuario) {
        usuarios.add(usuario)
    }

    /**
     * Valida las credenciales de acceso para el Login.
     * Usa la función de colección 'any' con una lambda.
     */
    fun validarCredenciales(correo: String, contrasena: String): Boolean =
        usuarios.any { it.tieneCorreo(correo) && it.coincideContrasena(contrasena) }

    /** Indica si un correo ya existe (para Registro y Recuperar contraseña). */
    fun existeCorreo(correo: String): Boolean =
        usuarios.any { it.tieneCorreo(correo) }

    /** Busca y devuelve un usuario por su correo, o null si no existe (find). */
    fun buscarPorCorreo(correo: String): Usuario? =
        usuarios.find { it.tieneCorreo(correo) }

    /** Devuelve los usuarios de un tipo dado (filter). */
    fun filtrarPorTipo(tipo: String): List<Usuario> =
        usuarios.filter { it.tipoUsuario == tipo }

    /** Devuelve la lista de correos registrados (map). */
    fun correos(): List<String> =
        usuarios.map { it.correo }

    /** Cuenta cuántos usuarios prefieren un medio de contacto (count). */
    fun contarPorMedio(medio: String): Int =
        usuarios.count { it.medioContacto == medio }

    /** Devuelve los usuarios ordenados alfabéticamente por nombre (sortedBy). */
    fun ordenadosPorNombre(): List<Usuario> =
        usuarios.sortedBy { it.nombre }
}
