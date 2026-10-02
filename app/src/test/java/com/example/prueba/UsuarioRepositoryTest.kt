package com.example.prueba

import com.example.prueba.data.PerfilBasico
import com.example.prueba.data.UsuarioRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Pruebas unitarias del [UsuarioRepository] (JUnit).
 *
 * Verifican la validación de credenciales, la búsqueda, el filtrado y el
 * registro de usuarios sobre el arreglo, usando funciones y colecciones de
 * Kotlin. No dependen de Android ni de Firebase.
 */
class UsuarioRepositoryTest {

    @Test
    fun validarCredenciales_correctasDevuelveTrue() {
        assertTrue(UsuarioRepository.validarCredenciales("ana@correo.cl", "Ana12345"))
    }

    @Test
    fun validarCredenciales_incorrectasDevuelveFalse() {
        assertFalse(UsuarioRepository.validarCredenciales("ana@correo.cl", "malaClave"))
    }

    @Test
    fun existeCorreo_reconoceCorreoPrecargado() {
        assertTrue(UsuarioRepository.existeCorreo("bruno@correo.cl"))
        assertFalse(UsuarioRepository.existeCorreo("noexiste@correo.cl"))
    }

    @Test
    fun buscarPorCorreo_devuelveElUsuario() {
        val u = UsuarioRepository.buscarPorCorreo("carla@correo.cl")
        assertNotNull(u)
        assertEquals("Carla Rojas", u?.nombre)
    }

    @Test
    fun filtrarPorTipo_devuelveSoloDeEseTipo() {
        val sordas = UsuarioRepository.filtrarPorTipo("Persona sorda")
        assertTrue(sordas.all { it.tipoUsuario == "Persona sorda" })
    }

    @Test
    fun registrar_incrementaLaCantidad() {
        val antes = UsuarioRepository.cantidad
        UsuarioRepository.registrar(
            PerfilBasico(
                nombre = "Zoe Vega",
                correo = "zoe.test@correo.cl",
                contrasena = "Zoe12345",
                tipoUsuario = "Intérprete",
                medioContacto = "Texto",
                aceptaTerminos = true
            ).aUsuario()
        )
        assertEquals(antes + 1, UsuarioRepository.cantidad)
        assertTrue(UsuarioRepository.existeCorreo("zoe.test@correo.cl"))
    }
}
