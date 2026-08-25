package com.example.prueba.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.prueba.data.UsuarioRepository

/**
 * View de Recuperar contraseña.
 *
 * Componentes UI utilizados: OutlinedTextField (input de correo), RadioButton
 * (método de recuperación), Button / OutlinedButton, Text (mensajes de estado).
 */
@Composable
fun RecuperarContrasenaScreen(
    modifier: Modifier = Modifier,
    onVolver: () -> Unit
) {
    var correo by remember { mutableStateOf("") }

    val metodos = listOf("Enviar enlace por correo", "Enviar código SMS")
    var metodoSeleccionado by remember { mutableStateOf(metodos.first()) }

    var mensaje by remember { mutableStateOf<String?>(null) }
    var mensajeEsError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Recuperar contraseña", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Text(
            "Ingrese su correo y elija cómo desea recuperar el acceso.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo electrónico") },
            leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Icono de correo") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))

        // Radio buttons: método de recuperación
        Text(
            "Método de recuperación",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.fillMaxWidth()
        )
        metodos.forEach { metodo ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (metodo == metodoSeleccionado),
                        onClick = { metodoSeleccionado = metodo }
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (metodo == metodoSeleccionado),
                    onClick = { metodoSeleccionado = metodo }
                )
                Text(metodo)
            }
        }

        if (mensaje != null) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = mensaje!!,
                color = if (mensajeEsError) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                when {
                    correo.isBlank() -> {
                        mensajeEsError = true
                        mensaje = "Debe ingresar un correo."
                    }
                    !UsuarioRepository.existeCorreo(correo) -> {
                        mensajeEsError = true
                        mensaje = "El correo no está registrado."
                    }
                    else -> {
                        mensajeEsError = false
                        mensaje = "Instrucciones enviadas mediante: $metodoSeleccionado."
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Recuperar acceso")
        }
        Spacer(Modifier.height(8.dp))
        OutlinedButton(onClick = onVolver, modifier = Modifier.fillMaxWidth()) {
            Text("Volver al inicio de sesión")
        }
    }
}
