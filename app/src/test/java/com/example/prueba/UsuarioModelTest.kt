package com.example.prueba

import com.example.prueba.data.TipoUsuario
import com.example.prueba.data.Usuario
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Pruebas unitarias del modelo [Usuario] y la enum [TipoUsuario] (JUnit).
 *
 * Verifican la lógica pura de Kotlin: propiedades calculadas, funciones
 * miembro y funciones de la enum, sin dependencias de Android.
 */
class UsuarioModelTest {

    private val usuario = Usuario(
        nombre = "Ana Torres",
        correo = "ana@correo.cl",
        contrasena = "Ana12345",
        tipoUsuario = "Persona sorda",
        medioContacto = "Texto",
        aceptaTerminos = true
    )

    @Test
    fun iniciales_seCalculanDesdeElNombre() {
        assertEquals("AT", usuario.iniciales)
    }

    @Test
    fun coincideContrasena_verdaderoSiCoincide() {
        assertTrue(usuario.coincideContrasena("Ana12345"))
        assertFalse(usuario.coincideContrasena("otra"))
    }

    @Test
    fun tieneCorreo_ignoraMayusculas() {
        assertTrue(usuario.tieneCorreo("ANA@correo.cl"))
        assertFalse(usuario.tieneCorreo("otro@correo.cl"))
    }

    @Test
    fun enum_etiquetasContieneLosTresTipos() {
        val etiquetas = TipoUsuario.etiquetas()
        assertEquals(3, etiquetas.size)
        assertTrue(etiquetas.contains("Persona sorda"))
        assertTrue(etiquetas.contains("Intérprete"))
    }

    @Test
    fun enum_desdeEtiquetaDevuelveElValorCorrecto() {
        assertEquals(TipoUsuario.INTERPRETE, TipoUsuario.desdeEtiqueta("Intérprete"))
        assertNull(TipoUsuario.desdeEtiqueta("Inexistente"))
    }
}
