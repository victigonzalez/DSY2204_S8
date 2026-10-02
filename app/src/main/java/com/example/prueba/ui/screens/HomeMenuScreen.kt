package com.example.prueba.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * View HomeMenú: menú principal de la aplicación tras iniciar sesión.
 *
 * Ofrece la navegación hacia las funciones principales (Escribir, Hablar y
 * BuscarDispositivo) mediante tarjetas accesibles, favoreciendo una
 * experiencia de usuario intuitiva. Incluye la opción de cerrar sesión.
 */
@Composable
fun HomeMenuScreen(
    nombreUsuario: String,
    modifier: Modifier = Modifier,
    onIrAEscribir: () -> Unit,
    onIrAHablar: () -> Unit,
    onIrABuscarDispositivo: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(
            text = "Hola, $nombreUsuario",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "¿Qué deseas hacer hoy?",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(24.dp))

        TarjetaMenu(
            titulo = "Escribir",
            descripcion = "Escribe un mensaje y la app lo dirá en voz alta.",
            icono = Icons.Filled.Edit,
            onClick = onIrAEscribir
        )
        Spacer(Modifier.height(16.dp))

        TarjetaMenu(
            titulo = "Hablar",
            descripcion = "Habla y la app transcribirá lo que se dijo a texto.",
            icono = Icons.Filled.Mic,
            onClick = onIrAHablar
        )
        Spacer(Modifier.height(16.dp))

        TarjetaMenu(
            titulo = "Buscar dispositivo",
            descripcion = "Ubica y registra la posición de tu dispositivo.",
            icono = Icons.Filled.LocationOn,
            onClick = onIrABuscarDispositivo
        )

        Spacer(Modifier.height(32.dp))
        OutlinedButton(
            onClick = onCerrarSesion,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Filled.Logout, contentDescription = null)
            Spacer(Modifier.size(8.dp))
            Text("Cerrar sesión")
        }
    }
}

/** Tarjeta reutilizable del menú principal. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TarjetaMenu(
    titulo: String,
    descripcion: String,
    icono: ImageVector,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icono,
                contentDescription = titulo,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )
            Spacer(Modifier.size(16.dp))
            Column {
                Text(titulo, style = MaterialTheme.typography.titleLarge)
                Text(
                    descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
