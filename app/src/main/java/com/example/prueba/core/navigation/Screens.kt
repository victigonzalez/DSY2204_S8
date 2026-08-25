package com.example.prueba.core.navigation

import kotlinx.serialization.Serializable

/** Rutas de navegación de la aplicación (type-safe navigation con Compose). */

@Serializable
object Login

@Serializable
object Registro

@Serializable
object RecuperarContrasena

/** Pantalla de bienvenida a la que se llega tras un login correcto. */
@Serializable
data class Inicio(val nombreUsuario: String)
