package com.example.prueba.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prueba.data.model.Mensaje
import com.example.prueba.data.remote.AuthService
import com.example.prueba.data.remote.MensajeRepository
import com.example.prueba.data.remote.Resultado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Estado observable de la pantalla de mensajes.
 */
data class MensajesUiState(
    val mensajes: List<Mensaje> = emptyList(),
    val cargando: Boolean = false,
    val error: String? = null
)

/**
 * ViewModel que gestiona los mensajes de las views Escribir y Hablar.
 *
 * Usa las extensiones KTX de lifecycle (viewModelScope) para lanzar corrutinas
 * y expone el estado mediante StateFlow. Se apoya en [MensajeRepository] para
 * el CRUD sobre Firebase Firestore.
 */
class MensajesViewModel(
    private val repo: MensajeRepository,
    private val auth: AuthService
) : ViewModel() {

    /** Constructor sin argumentos requerido por el factory por defecto de viewModel(). */
    constructor() : this(MensajeRepository(), AuthService())

    private val _uiState = MutableStateFlow(MensajesUiState())
    val uiState: StateFlow<MensajesUiState> = _uiState.asStateFlow()

    /** uid del usuario autenticado; usa un valor de respaldo para demo local. */
    private val uid: String
        get() = auth.usuarioActual?.uid ?: "demo-local"

    /** Guarda un mensaje nuevo (CREATE) y recarga la lista. */
    fun guardar(texto: String, tipo: String) {
        if (texto.isBlank()) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(cargando = true, error = null)
            val mensaje = Mensaje(uid = uid, texto = texto, tipo = tipo)
            when (val r = repo.crear(mensaje)) {
                is Resultado.Exito -> cargar()
                is Resultado.Error -> _uiState.value =
                    _uiState.value.copy(cargando = false, error = r.mensaje)
                Resultado.Cargando -> {}
            }
        }
    }

    /** Carga la lista de mensajes del usuario (READ). */
    fun cargar() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(cargando = true, error = null)
            when (val r = repo.listarPorUsuario(uid)) {
                is Resultado.Exito -> _uiState.value =
                    MensajesUiState(mensajes = r.dato, cargando = false)
                is Resultado.Error -> _uiState.value =
                    _uiState.value.copy(cargando = false, error = r.mensaje)
                Resultado.Cargando -> {}
            }
        }
    }

    /** Elimina un mensaje (DELETE) y recarga la lista. */
    fun eliminar(id: String) {
        viewModelScope.launch {
            when (val r = repo.eliminar(id)) {
                is Resultado.Exito -> cargar()
                is Resultado.Error -> _uiState.value =
                    _uiState.value.copy(error = r.mensaje)
                Resultado.Cargando -> {}
            }
        }
    }
}
