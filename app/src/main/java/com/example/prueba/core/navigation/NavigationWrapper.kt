package com.example.prueba.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.prueba.ui.screens.BuscarDispositivoScreen
import com.example.prueba.ui.screens.EscribirScreen
import com.example.prueba.ui.screens.HablarScreen
import com.example.prueba.ui.screens.HomeMenuScreen
import com.example.prueba.ui.screens.LoginScreen
import com.example.prueba.ui.screens.RecuperarContrasenaScreen
import com.example.prueba.ui.screens.RegistroScreen

/**
 * Controlador de navegación de la app. Define el grafo de rutas y el flujo
 * entre las views: Login, Registro, Recuperar contraseña, HomeMenú y las
 * funciones Escribir, Hablar y BuscarDispositivo.
 */
@Composable
fun NavigationWrapper(modifier: Modifier) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Login) {

        // View de Login
        composable<Login> {
            LoginScreen(
                modifier = modifier,
                onLoginExitoso = { nombre ->
                    navController.navigate(HomeMenu(nombreUsuario = nombre)) {
                        popUpTo(Login) { inclusive = true }
                    }
                },
                onIrARegistro = { navController.navigate(Registro) },
                onIrARecuperar = { navController.navigate(RecuperarContrasena) }
            )
        }

        // View de Registro de usuario
        composable<Registro> {
            RegistroScreen(
                modifier = modifier,
                onRegistroExitoso = { navController.popBackStack() },
                onVolver = { navController.popBackStack() }
            )
        }

        // View de Recuperar contraseña
        composable<RecuperarContrasena> {
            RecuperarContrasenaScreen(
                modifier = modifier,
                onVolver = { navController.popBackStack() }
            )
        }

        // Menú principal tras login correcto
        composable<HomeMenu> {
            val datos = it.toRoute<HomeMenu>()
            HomeMenuScreen(
                nombreUsuario = datos.nombreUsuario,
                modifier = modifier,
                onIrAEscribir = { navController.navigate(Escribir) },
                onIrAHablar = { navController.navigate(Hablar) },
                onIrABuscarDispositivo = { navController.navigate(BuscarDispositivo) },
                onCerrarSesion = {
                    navController.navigate(Login) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // View Escribir (texto a voz)
        composable<Escribir> {
            EscribirScreen(
                modifier = modifier,
                onVolver = { navController.popBackStack() }
            )
        }

        // View Hablar (voz a texto)
        composable<Hablar> {
            HablarScreen(
                modifier = modifier,
                onVolver = { navController.popBackStack() }
            )
        }

        // View BuscarDispositivo (geolocalización)
        composable<BuscarDispositivo> {
            BuscarDispositivoScreen(
                modifier = modifier,
                onVolver = { navController.popBackStack() }
            )
        }
    }
}
