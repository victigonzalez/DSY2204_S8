package com.example.prueba.data

/**
 * Modelo de datos de un usuario registrado en la aplicación (VozApp).
 *
 * Se implementa como 'data class' de Kotlin. Incluye además funciones miembro
 * y propiedades calculadas que demuestran la sintaxis del lenguaje.
 *
 * @param nombre         Nombre completo del usuario.
 * @param correo         Correo electrónico (identificador de acceso).
 * @param contrasena     Contraseña de acceso.
 * @param tipoUsuario    Tipo de usuario seleccionado en el combo box.
 * @param medioContacto  Medio de comunicación preferido (radio buttons): "Texto" o "Voz".
 * @param aceptaTerminos Indica si aceptó los términos y condiciones (check list).
 */
data class Usuario(
    val nombre: String,
    val correo: String,
    val contrasena: String,
    val tipoUsuario: String,
    val medioContacto: String,
    val aceptaTerminos: Boolean
) {
    /**
     * Propiedad calculada: devuelve las iniciales del usuario a partir del nombre.
     * Usa colecciones de Kotlin.
     */
    val iniciales: String
        get() = nombre
            .split(" ")
            .filter { it.isNotBlank() }
            .map { it.first().uppercaseChar() }
            .joinToString("")

    /** Función miembro con expresión: valida si la contraseña ingresada coincide. */
    fun coincideContrasena(clave: String): Boolean = contrasena == clave

    /** Función miembro: compara el correo sin distinguir mayúsculas. */
    fun tieneCorreo(otroCorreo: String): Boolean =
        correo.equals(otroCorreo, ignoreCase = true)
}

/**
 * Enumeración de los tipos de usuario admitidos (funciones y colecciones de Kotlin).
 * Cada valor lleva una etiqueta legible que se muestra en la interfaz.
 */
enum class TipoUsuario(val etiqueta: String) {
    PERSONA_SORDA("Persona sorda"),
    FAMILIAR("Familiar / cuidador"),
    INTERPRETE("Intérprete");

    companion object {
        /** Lista de etiquetas para poblar el combo box de la view de Registro. */
        fun etiquetas(): List<String> = entries.map { it.etiqueta }

        /** Busca el tipo a partir de su etiqueta; devuelve null si no existe. */
        fun desdeEtiqueta(etiqueta: String): TipoUsuario? =
            entries.firstOrNull { it.etiqueta == etiqueta }
    }
}
