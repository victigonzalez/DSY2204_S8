package com.example.prueba.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prueba.ui.viewmodel.MensajesViewModel
import com.example.prueba.util.SpeechRecognizerManager

/**
 * View Hablar: captura la voz del usuario mediante reconocimiento de voz
 * (Speech-To-Text) y la transcribe a texto, que puede guardarse en Firebase.
 *
 * Solicita el permiso RECORD_AUDIO en tiempo de ejecución antes de escuchar.
 */
@Composable
fun HablarScreen(
    modifier: Modifier = Modifier,
    onVolver: () -> Unit,
    viewModel: MensajesViewModel = viewModel()
) {
    val context = LocalContext.current
    var transcripcion by remember { mutableStateOf("") }
    var escuchando by remember { mutableStateOf(false) }
    var aviso by remember { mutableStateOf<String?>(null) }

    val speech = remember { SpeechRecognizerManager(context) }
    DisposableEffect(Unit) { onDispose { speech.detener() } }

    val estado by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) { viewModel.cargar() }

    // Función que inicia la escucha del micrófono.
    fun iniciarEscucha() {
        escuchando = true
        aviso = null
        speech.escuchar(
            onResultado = { texto ->
                transcripcion = texto
                escuchando = false
            },
            onError = { msg ->
                aviso = msg
                escuchando = false
            },
            onListo = { aviso = "Escuchando… habla ahora." }
        )
    }

    // Lanzador del permiso de micrófono.
    val permisoLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) iniciarEscucha()
        else aviso = "Se necesita el permiso de micrófono para usar esta función."
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text("Hablar", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        Text(
            "Presiona el botón y habla; la app transcribirá lo que se dijo.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                val yaConcedido = ContextCompat.checkSelfPermission(
                    context, Manifest.permission.RECORD_AUDIO
                ) == PackageManager.PERMISSION_GRANTED
                if (yaConcedido) iniciarEscucha()
                else permisoLauncher.launch(Manifest.permission.RECORD_AUDIO)
            },
            enabled = !escuchando,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Filled.Mic, contentDescription = null)
            Text(if (escuchando) "  Escuchando…" else "  Hablar")
        }

        Spacer(Modifier.height(16.dp))
        Card(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = transcripcion.ifBlank { "Aquí aparecerá el texto reconocido." },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        if (aviso != null) {
            Spacer(Modifier.height(8.dp))
            Text(aviso!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(Modifier.height(12.dp))
        OutlinedButton(
            onClick = {
                viewModel.guardar(transcripcion, tipo = "hablado")
                transcripcion = ""
            },
            enabled = transcripcion.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Filled.Save, contentDescription = null)
            Text("  Guardar transcripción")
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
