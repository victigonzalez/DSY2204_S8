package com.example.prueba.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prueba.data.PerfilBasico
import com.example.prueba.data.UsuarioRepository
import com.example.prueba.data.model.PerfilUsuario
import com.example.prueba.data.remote.AuthService
import com.example.prueba.data.remote.Resultado
import com.example.prueba.data.remote.UsuarioRemoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** Estado observable de las pantallas de autenticación. */
data class AuthUiState(
    val cargando: Boolean = false,
    val error: String? = null,
    val info: String? = null
)

/**
 * ViewModel de autenticación. Integra Firebase Authentication como back end
 * principal y mantiene un respaldo con el repositorio local en memoria, de modo
 * que la aplicación es demostrable antes de conectar un proyecto Firebase real
 * y usa Firebase automáticamente una vez configurado google-services.json.
 *
 * Usa corrutinas (viewModelScope) y las extensiones KTX de lifecycle.
 */
class AuthViewModel(
    private val auth: AuthService,
    private val perfiles: UsuarioRemoteRepository
) : ViewModel() {

    /** Constructor sin argumentos requerido por el factory por defecto de viewModel(). */
    constructor() : this(AuthService(), UsuarioRemoteRepository())

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    /**
     * Inicia sesión. Intenta Firebase; si el back end no está disponible,
     * recurre a la validación local contra el arreglo de usuarios.
     */
    fun login(correo: String, contrasena: String, onExito: (String) -> Unit) {
        if (correo.isBlank() || contrasena.isBlank()) {
            _uiState.value = AuthUiState(error = "Debe completar correo y contraseña.")
            return
        }
        viewModelScope.launch {
            _uiState.value = AuthUiState(cargando = true)
            when (val r = auth.iniciarSesion(correo, contrasena)) {
                is Resultado.Exito -> {
                    _uiState.value = AuthUiState()
                    onExito(correo.substringBefore("@"))
                }
                is Resultado.Error -> {
                    // Respaldo local para permitir la demostración sin Firebase.
                    if (UsuarioRepository.validarCredenciales(correo, contrasena)) {
                        val nombre = UsuarioRepository.buscarPorCorreo(correo)?.nombre ?: correo
                        _uiState.value = AuthUiState()
                        onExito(nombre)
                    } else {
                        _uiState.value = AuthUiState(error = "Credenciales incorrectas.")
                    }
                }
                Resultado.Cargando -> {}
            }
        }
    }

    /**
     * Registra un usuario en Firebase Auth y guarda su perfil en Firestore.
     * Si Firebase no está disponible, registra en el repositorio local.
     */
    fun registrar(perfil: PerfilBasico, onExito: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(cargando = true)
            when (val r = auth.registrar(perfil.correo, perfil.contrasena)) {
                is Resultado.Exito -> {
                    val uid = r.dato.uid
                    perfiles.guardar(
                        PerfilUsuario(
                            uid = uid,
                            nombre = perfil.nombre,
                            correo = perfil.correo,
                            tipoUsuario = perfil.tipoUsuario,
                            medioContacto = perfil.medioContacto
                        )
                    )
                    // Mantiene también el arreglo local sincronizado.
                    UsuarioRepository.registrar(perfil.aUsuario())
                    _uiState.value = AuthUiState(info = "Usuario registrado en Firebase.")
                    onExito()
                }
                is Resultado.Error -> {
                    // Respaldo local.
                    if (UsuarioRepository.existeCorreo(perfil.correo)) {
                        _uiState.value = AuthUiState(error = "El correo ya se encuentra registrado.")
                    } else {
                        UsuarioRepository.registrar(perfil.aUsuario())
                        _uiState.value = AuthUiState(info = "Usuario registrado localmente.")
                        onExito()
                    }
                }
                Resultado.Cargando -> {}
            }
        }
    }

    /** Envía el correo de recuperación por Firebase; respaldo: verifica local. */
    fun recuperar(correo: String, onResultado: (String) -> Unit) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(cargando = true)
            when (val r = auth.recuperarContrasena(correo)) {
                is Resultado.Exito -> {
                    _uiState.value = AuthUiState()
                    onResultado("Se envió un correo de recuperación a $correo.")
                }
                is Resultado.Error -> {
                    val msg = if (UsuarioRepository.existeCorreo(correo))
                        "Se enviarán instrucciones de recuperación a $correo."
                    else "El correo no está registrado."
                    _uiState.value = AuthUiState()
                    onResultado(msg)
                }
                Resultado.Cargando -> {}
            }
        }
    }

    fun limpiarMensajes() {
        _uiState.value = _uiState.value.copy(error = null, info = null)
    }
}
