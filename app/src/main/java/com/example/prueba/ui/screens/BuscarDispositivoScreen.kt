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
import androidx.compose.material.icons.filled.LocationOn
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prueba.ui.viewmodel.DispositivosViewModel
import com.example.prueba.util.LocationManager
import kotlinx.coroutines.launch

/**
 * View BuscarDispositivo: obtiene la ubicación actual mediante geolocalización
 * (Fused Location Provider), permite nombrar el dispositivo y guardar su
 * posición en Firebase (CRUD). Solicita el permiso de ubicación en runtime.
 */
@Composable
fun BuscarDispositivoScreen(
    modifier: Modifier = Modifier,
    onVolver: () -> Unit,
    viewModel: DispositivosViewModel = viewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val locationManager = remember { LocationManager(context) }

    var nombre by remember { mutableStateOf("") }
    var latitud by remember { mutableStateOf<Double?>(null) }
    var longitud by remember { mutableStateOf<Double?>(null) }
    var aviso by remember { mutableStateOf<String?>(null) }

    val estado by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) { viewModel.cargar() }

    // Obtiene la ubicación actual de forma asíncrona.
    fun obtenerUbicacion() {
        scope.launch {
            aviso = "Obteniendo ubicación…"
            val coords = locationManager.ubicacionActual()
            if (coords != null) {
                latitud = coords.first
                longitud = coords.second
                aviso = null
            } else {
                aviso = "No se pudo obtener la ubicación. Verifica el GPS."
            }
        }
    }

    val permisoLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) obtenerUbicacion()
        else aviso = "Se necesita el permiso de ubicación para esta función."
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text("Buscar dispositivo", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        Text(
            "Obtén la ubicación actual y regístrala con un nombre.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                val concedido = ContextCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                if (concedido) obtenerUbicacion()
                else permisoLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Filled.LocationOn, contentDescription = null)
            Text("  Obtener mi ubicación")
        }

        Spacer(Modifier.height(12.dp))
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Latitud: ${latitud?.let { "%.6f".format(it) } ?: "—"}")
                Text("Longitud: ${longitud?.let { "%.6f".format(it) } ?: "—"}")
            }
        }

        if (aviso != null) {
            Spacer(Modifier.height(8.dp))
            Text(aviso!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre del dispositivo") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedButton(
            onClick = {
                val la = latitud
                val lo = longitud
                if (la != null && lo != null) {
                    viewModel.guardar(nombre, la, lo)
                    nombre = ""
                } else {
                    aviso = "Primero obtén la ubicación."
                }
            },
            enabled = nombre.isNotBlank() && latitud != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Filled.Save, contentDescription = null)
            Text("  Guardar dispositivo")
        }

        Spacer(Modifier.height(20.dp))
        Text("Dispositivos registrados", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(estado.dispositivos, key = { it.id }) { d ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(d.nombre, fontWeight = FontWeight.Bold)
                            Text(
                                "%.5f, %.5f".format(d.latitud, d.longitud),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        IconButton(onClick = { viewModel.eliminar(d.id) }) {
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
