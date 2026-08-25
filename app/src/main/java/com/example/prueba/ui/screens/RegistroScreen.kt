package com.example.prueba.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.prueba.data.Usuario
import com.example.prueba.data.UsuarioRepository

/**
 * View de Registro de usuario. Alimenta el array de usuarios del repositorio.
 *
 * Componentes UI utilizados: OutlinedTextField (inputs), ExposedDropdownMenuBox
 * (combo box de tipo de usuario), RadioButton (medio de contacto preferido),
 * Checkbox (check list de términos y notificaciones), Button/OutlinedButton,
 * y LazyVerticalGrid (grilla que muestra los usuarios registrados).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    modifier: Modifier = Modifier,
    onRegistroExitoso: () -> Unit,
    onVolver: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmar by remember { mutableStateOf("") }

    // Combo box
    val tipos = listOf("Persona sorda", "Familiar / cuidador", "Intérprete")
    var comboExpandido by remember { mutableStateOf(false) }
    var tipoSeleccionado by remember { mutableStateOf(tipos.first()) }

    // Radio buttons
    val medios = listOf("Texto", "Voz")
    var medioSeleccionado by remember { mutableStateOf(medios.first()) }

    // Check list
    var aceptaTerminos by remember { mutableStateOf(false) }
    var recibeNotificaciones by remember { mutableStateOf(false) }

    var mensaje by remember { mutableStateOf<String?>(null) }
    var mensajeEsError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text("Registro de usuario", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre completo") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo electrónico") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = confirmar,
            onValueChange = { confirmar = it },
            label = { Text("Confirmar contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))

        // Combo box: tipo de usuario
        Text("Tipo de usuario", style = MaterialTheme.typography.titleSmall)
        Spacer(Modifier.height(4.dp))
        ExposedDropdownMenuBox(
            expanded = comboExpandido,
            onExpandedChange = { comboExpandido = it }
        ) {
            OutlinedTextField(
                value = tipoSeleccionado,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    Icon(Icons.Filled.ArrowDropDown, contentDescription = "Desplegar opciones")
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = comboExpandido,
                onDismissRequest = { comboExpandido = false }
            ) {
                tipos.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion) },
                        onClick = {
                            tipoSeleccionado = opcion
                            comboExpandido = false
                        }
                    )
                }
            }
        }
        Spacer(Modifier.height(16.dp))

        // Radio buttons: medio de contacto preferido
        Text("Medio de comunicación preferido", style = MaterialTheme.typography.titleSmall)
        medios.forEach { medio ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (medio == medioSeleccionado),
                        onClick = { medioSeleccionado = medio }
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (medio == medioSeleccionado),
                    onClick = { medioSeleccionado = medio }
                )
                Text(medio)
            }
        }
        Spacer(Modifier.height(8.dp))

        // Check list
        FilaCheck(
            marcado = aceptaTerminos,
            onCambio = { aceptaTerminos = it },
            etiqueta = "Acepto los términos y condiciones"
        )
        FilaCheck(
            marcado = recibeNotificaciones,
            onCambio = { recibeNotificaciones = it },
            etiqueta = "Deseo recibir notificaciones de accesibilidad"
        )

        if (mensaje != null) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = mensaje!!,
                color = if (mensajeEsError) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(Modifier.height(20.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = onVolver, modifier = Modifier.weight(1f)) {
                Text("Volver")
            }
            Button(
                onClick = {
                    when {
                        nombre.isBlank() || correo.isBlank() || contrasena.isBlank() -> {
                            mensajeEsError = true
                            mensaje = "Complete todos los campos obligatorios."
                        }
                        contrasena != confirmar -> {
                            mensajeEsError = true
                            mensaje = "Las contraseñas no coinciden."
                        }
                        !aceptaTerminos -> {
                            mensajeEsError = true
                            mensaje = "Debe aceptar los términos y condiciones."
                        }
                        UsuarioRepository.existeCorreo(correo) -> {
                            mensajeEsError = true
                            mensaje = "El correo ya se encuentra registrado."
                        }
                        else -> {
                            UsuarioRepository.registrar(
                                Usuario(
                                    nombre = nombre,
                                    correo = correo,
                                    contrasena = contrasena,
                                    tipoUsuario = tipoSeleccionado,
                                    medioContacto = medioSeleccionado,
                                    aceptaTerminos = aceptaTerminos
                                )
                            )
                            mensajeEsError = false
                            mensaje = "Usuario registrado correctamente."
                            nombre = ""; correo = ""; contrasena = ""; confirmar = ""
                        }
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Registrar")
            }
        }

        Spacer(Modifier.height(24.dp))

        // Grilla de usuarios registrados (array de usuarios)
        Text(
            "Usuarios registrados (${UsuarioRepository.usuarios.size})",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(UsuarioRepository.usuarios) { usuario ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(usuario.nombre, fontWeight = FontWeight.Bold)
                        Text(usuario.correo, style = MaterialTheme.typography.bodySmall)
                        Text(usuario.tipoUsuario, style = MaterialTheme.typography.bodySmall)
                        Text("Medio: ${usuario.medioContacto}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
