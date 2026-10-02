package com.example.prueba.data.remote

/**
 * Envoltorio del resultado de una operación con Firebase.
 *
 * Es una 'sealed class' de Kotlin: permite representar de forma segura los tres
 * estados posibles de una operación asíncrona (cargando, éxito o error) y
 * manejarlos con 'when' de manera exhaustiva en la interfaz.
 */
sealed class Resultado<out T> {
    /** Operación en curso. */
    data object Cargando : Resultado<Nothing>()

    /** Operación exitosa con su dato asociado. */
    data class Exito<T>(val dato: T) : Resultado<T>()

    /** Operación fallida con un mensaje descriptivo. */
    data class Error(val mensaje: String) : Resultado<Nothing>()
}
