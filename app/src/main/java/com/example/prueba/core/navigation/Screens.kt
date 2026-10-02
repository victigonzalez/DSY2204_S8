package com.example.prueba.core.navigation

import kotlinx.serialization.Serializable

/** Rutas de navegación de la aplicación (type-safe navigation con Compose). */

@Serializable
object Login

@Serializable
object Registro

@Serializable
object RecuperarContrasena

/** Menú principal al que se llega tras un login correcto. */
@Serializable
data class HomeMenu(val nombreUsuario: String)

/** View Escribir (texto a voz). */
@Serializable
object Escribir

/** View Hablar (voz a texto). */
@Serializable
object Hablar

/** View BuscarDispositivo (geolocalización). */
@Serializable
object BuscarDispositivo
