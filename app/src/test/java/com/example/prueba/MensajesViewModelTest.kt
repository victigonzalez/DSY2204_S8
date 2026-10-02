package com.example.prueba

import com.example.prueba.data.model.Mensaje
import com.example.prueba.data.remote.AuthService
import com.example.prueba.data.remote.MensajeRepository
import com.example.prueba.data.remote.Resultado
import com.example.prueba.ui.viewmodel.MensajesViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.wheneverBlocking

/**
 * Prueba unitaria del [MensajesViewModel] usando Mockito.
 *
 * Simula (mock) el repositorio CRUD y el servicio de autenticación para
 * verificar, de forma aislada y sin Firebase real, que al guardar un mensaje
 * el ViewModel recarga la lista y actualiza su estado.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MensajesViewModelTest {

    @get:Rule
    val mainRule = MainDispatcherRule()

    private val repo: MensajeRepository = mock()
    private val auth: AuthService = mock()

    @Test
    fun guardar_creaMensajeYRecargaLista() = runTest {
        val lista = listOf(
            Mensaje(id = "1", uid = "demo-local", texto = "hola", tipo = "escrito", timestamp = 1L)
        )
        wheneverBlocking { repo.crear(any()) }.thenReturn(Resultado.Exito("1"))
        wheneverBlocking { repo.listarPorUsuario(any()) }.thenReturn(Resultado.Exito(lista))

        val viewModel = MensajesViewModel(repo, auth)
        viewModel.guardar("hola", "escrito")
        advanceUntilIdle()

        val estado = viewModel.uiState.value
        assertEquals(1, estado.mensajes.size)
        assertEquals("hola", estado.mensajes.first().texto)
        verify(repo).crear(any())
    }

    @Test
    fun eliminar_borraYRecargaLista() = runTest {
        wheneverBlocking { repo.eliminar(any()) }.thenReturn(Resultado.Exito(Unit))
        wheneverBlocking { repo.listarPorUsuario(any()) }.thenReturn(Resultado.Exito(emptyList()))

        val viewModel = MensajesViewModel(repo, auth)
        viewModel.eliminar("1")
        advanceUntilIdle()

        assertEquals(0, viewModel.uiState.value.mensajes.size)
        verify(repo).eliminar("1")
    }
}
