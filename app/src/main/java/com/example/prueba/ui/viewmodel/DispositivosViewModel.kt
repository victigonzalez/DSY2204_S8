package com.example.prueba.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prueba.data.model.Dispositivo
import com.example.prueba.data.remote.AuthService
import com.example.prueba.data.remote.DispositivoRepository
import com.example.prueba.data.remote.Resultado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** Estado observable de la pantalla BuscarDispositivo. */
data class DispositivosUiState(
    val dispositivos: List<Dispositivo> = emptyList(),
    val cargando: Boolean = false,
    val error: String? = null
)

/**
 * ViewModel de la view BuscarDispositivo. Gestiona el CRUD de dispositivos
 * localizados sobre Firebase Firestore usando corrutinas (viewModelScope).
 */
class DispositivosViewModel(
    private val repo: DispositivoRepository,
    private val auth: AuthService
) : ViewModel() {

    /** Constructor sin argumentos requerido por el factory por defecto de viewModel(). */
    constructor() : this(DispositivoRepository(), AuthService())

    private val _uiState = MutableStateFlow(DispositivosUiState())
    val uiState: StateFlow<DispositivosUiState> = _uiState.asStateFlow()

    private val uid: String
        get() = auth.usuarioActual?.uid ?: "demo-local"

    /** Guarda un dispositivo con su ubicación (CREATE) y recarga la lista. */
    fun guardar(nombre: String, latitud: Double, longitud: Double) {
        if (nombre.isBlank()) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(cargando = true, error = null)
            val dispositivo = Dispositivo(
                uid = uid, nombre = nombre, latitud = latitud, longitud = longitud
            )
            when (val r = repo.crear(dispositivo)) {
                is Resultado.Exito -> cargar()
                is Resultado.Error -> _uiState.value =
                    _uiState.value.copy(cargando = false, error = r.mensaje)
                Resultado.Cargando -> {}
            }
        }
    }

    /** Carga la lista de dispositivos del usuario (READ). */
    fun cargar() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(cargando = true, error = null)
            when (val r = repo.listarPorUsuario(uid)) {
                is Resultado.Exito -> _uiState.value =
                    DispositivosUiState(dispositivos = r.dato, cargando = false)
                is Resultado.Error -> _uiState.value =
                    _uiState.value.copy(cargando = false, error = r.mensaje)
                Resultado.Cargando -> {}
            }
        }
    }

    /** Elimina un dispositivo (DELETE) y recarga la lista. */
    fun eliminar(id: String) {
        viewModelScope.launch {
            when (val r = repo.eliminar(id)) {
                is Resultado.Exito -> cargar()
                is Resultado.Error -> _uiState.value = _uiState.value.copy(error = r.mensaje)
                Resultado.Cargando -> {}
            }
        }
    }
}
