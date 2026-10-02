package com.example.prueba.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prueba.ui.viewmodel.AuthViewModel

/**
 * View de Login.
 *
 * Componentes UI utilizados: OutlinedTextField (input de correo y contraseña),
 * Checkbox (check list "Recordarme"), Button (ingresar), TextButton (vínculos a
 * Registro y Recuperar contraseña), Text (etiquetas y mensajes), iconos de Material.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLoginExitoso: (String) -> Unit,
    onIrARegistro: () -> Unit,
    onIrARecuperar: () -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var recordarme by remember { mutableStateOf(false) }
    val authState by authViewModel.uiState.collectAsState()
    val mensajeError = authState.error

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "VozApp",
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Comunicación accesible para personas con discapacidad auditiva",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(32.dp))

        // Input: correo
        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo electrónico") },
            leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "Icono de usuario") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "Campo para ingresar el correo electrónico" }
        )

        Spacer(Modifier.height(16.dp))

        // Input: contraseña
        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text("Contraseña") },
            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Icono de candado") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "Campo para ingresar la contraseña" }
        )

        Spacer(Modifier.height(8.dp))

        // Check list: recordarme
        Column(modifier = Modifier.fillMaxWidth()) {
            FilaCheck(
                marcado = recordarme,
                onCambio = { recordarme = it },
                etiqueta = "Recordar mi sesión"
            )
        }

        if (mensajeError != null) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = mensajeError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(Modifier.height(24.dp))

        // Botón principal (autenticación con Firebase + respaldo local)
        Button(
            onClick = { authViewModel.login(correo, contrasena, onLoginExitoso) },
            enabled = !authState.cargando,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (authState.cargando) "Ingresando…" else "Ingresar")
        }

        Spacer(Modifier.height(8.dp))

        // Vínculos (links)
        TextButton(onClick = onIrARecuperar) {
            Text("¿Olvidó su contraseña?")
        }
        TextButton(onClick = onIrARegistro) {
            Text("¿No tiene cuenta? Regístrese aquí")
        }
    }
}

/** Fila reutilizable de check (Checkbox + etiqueta) usada como check list. */
@Composable
fun FilaCheck(
    marcado: Boolean,
    onCambio: (Boolean) -> Unit,
    etiqueta: String
) {
    androidx.compose.foundation.layout.Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = marcado, onCheckedChange = onCambio)
        Text(etiqueta)
    }
}
