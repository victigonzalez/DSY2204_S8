package com.example.prueba.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prueba.ui.viewmodel.MensajesViewModel
import com.example.prueba.util.TextToSpeechManager

/**
 * View Escribir: el usuario escribe un texto y la aplicación lo reproduce en
 * voz alta mediante Text-To-Speech. El mensaje puede guardarse en Firebase
 * (CRUD) y consultarse en el historial.
 */
@Composable
fun EscribirScreen(
    modifier: Modifier = Modifier,
    onVolver: () -> Unit,
    viewModel: MensajesViewModel = viewModel()
) {
    val context = LocalContext.current
    var texto by remember { mutableStateOf("") }
    var ttsListo by remember { mutableStateOf(false) }
    var aviso by remember { mutableStateOf<String?>(null) }

    // Crea el motor TTS y lo libera al salir de la pantalla.
    val tts = remember { TextToSpeechManager(context) { listo -> ttsListo = listo } }
    DisposableEffect(Unit) {
        onDispose { tts.liberar() }
    }

    val estado by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) { viewModel.cargar() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text("Escribir", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        Text(
            "Escribe un mensaje y la app lo dirá en voz alta.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = texto,
            onValueChange = { texto = it },
            label = { Text("Mensaje") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        )
        Spacer(Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = {
                    val ok = tts.hablar(texto)
                    aviso = if (ok) null
                            else if (!ttsListo) "El motor de voz aún no está listo."
                            else "Escribe un mensaje primero."
                },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = null)
                Spacer(Modifier.height(0.dp))
                Text("  Reproducir")
            }
            OutlinedButton(
                onClick = {
                    viewModel.guardar(texto, tipo = "escrito")
                    texto = ""
                },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Filled.Save, contentDescription = null)
                Text("  Guardar")
            }
        }

        if (aviso != null) {
            Spacer(Modifier.height(8.dp))
            Text(aviso!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
        }
        if (estado.error != null) {
            Spacer(Modifier.height(8.dp))
            Text(
                "Firebase: ${estado.error}",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(Modifier.height(20.dp))
        Text("Historial", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(estado.mensajes, key = { it.id }) { mensaje ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(mensaje.texto, modifier = Modifier.weight(1f))
                        IconButton(onClick = { viewModel.eliminar(mensaje.id) }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Eliminar")
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))
        OutlinedButton(onClick = onVolver, modifier = Modifier.fillMaxWidth()) {
            Text("Volver al menú")
        }
    }
}
