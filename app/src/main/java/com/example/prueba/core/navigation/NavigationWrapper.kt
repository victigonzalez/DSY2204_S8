package com.example.prueba.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.prueba.ui.screens.InicioScreen
import com.example.prueba.ui.screens.LoginScreen
import com.example.prueba.ui.screens.RecuperarContrasenaScreen
import com.example.prueba.ui.screens.RegistroScreen

/**
 * Controlador de navegación de la app. Define el grafo de rutas y el
 * flujo entre las views: Login, Registro, Recuperar contraseña e Inicio.
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
                    navController.navigate(Inicio(nombreUsuario = nombre)) {
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

        // Pantalla de bienvenida tras login correcto
        composable<Inicio> {
            val datos = it.toRoute<Inicio>()
            InicioScreen(
                nombreUsuario = datos.nombreUsuario,
                modifier = modifier,
                onCerrarSesion = {
                    navController.navigate(Login) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
