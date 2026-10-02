package com.example.prueba.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

/**
 * Servicio de autenticación de usuarios con Firebase Authentication.
 *
 * Encapsula el registro, el inicio de sesión, el envío de correo de
 * recuperación de contraseña y el cierre de sesión. Expone funciones
 * 'suspend' que usan corrutinas (await) para no bloquear la interfaz,
 * devolviendo un [Resultado] que la capa de UI maneja con 'when'.
 *
 * Configuración de parámetros de autenticación: la conexión con el proyecto
 * Firebase se define en app/google-services.json; aquí se usa la instancia
 * por defecto de FirebaseAuth.
 */
class AuthService(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {

    /** Usuario actualmente autenticado, o null si no hay sesión activa. */
    val usuarioActual: FirebaseUser?
        get() = auth.currentUser

    /** Indica si existe una sesión iniciada. */
    fun haySesion(): Boolean = auth.currentUser != null

    /** Registra un nuevo usuario con correo y contraseña. */
    suspend fun registrar(correo: String, contrasena: String): Resultado<FirebaseUser> {
        return try {
            val resultado = auth.createUserWithEmailAndPassword(correo, contrasena).await()
            val usuario = resultado.user
            if (usuario != null) Resultado.Exito(usuario)
            else Resultado.Error("No se pudo crear el usuario.")
        } catch (e: Exception) {
            Resultado.Error(e.localizedMessage ?: "Error al registrar el usuario.")
        }
    }

    /** Inicia sesión validando las credenciales contra Firebase. */
    suspend fun iniciarSesion(correo: String, contrasena: String): Resultado<FirebaseUser> {
        return try {
            val resultado = auth.signInWithEmailAndPassword(correo, contrasena).await()
            val usuario = resultado.user
            if (usuario != null) Resultado.Exito(usuario)
            else Resultado.Error("Credenciales incorrectas.")
        } catch (e: Exception) {
            Resultado.Error(e.localizedMessage ?: "Error al iniciar sesión.")
        }
    }

    /** Envía un correo para recuperar la contraseña. */
    suspend fun recuperarContrasena(correo: String): Resultado<Unit> {
        return try {
            auth.sendPasswordResetEmail(correo).await()
            Resultado.Exito(Unit)
        } catch (e: Exception) {
            Resultado.Error(e.localizedMessage ?: "No se pudo enviar el correo de recuperación.")
        }
    }

    /** Cierra la sesión actual. */
    fun cerrarSesion() {
        auth.signOut()
    }
}
