package com.example.prueba.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.prueba.data.Usuario

/**
 * Grilla que muestra la lista de usuarios almacenados en el arreglo.
 *
 * Recibe la colección por parámetro, lo que la hace independiente del repositorio y fácil de previsualizar.
 *
 * @param usuarios Colección de usuarios a mostrar.
 * @param modifier Modificador opcional para el contenedor.
 */
@Composable
fun ListaUsuarios(
    usuarios: List<Usuario>,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier
            .fillMaxWidth()
            .height(260.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = usuarios,
            key = { usuario -> usuario.correo }
        ) { usuario ->
            ItemUsuario(usuario = usuario)
        }
    }
}

/**
 * Tarjeta que representa a un único usuario dentro de la grilla.
 *
 * @param usuario Usuario a representar.
 */
@Composable
fun ItemUsuario(usuario: Usuario) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(usuario.nombre, fontWeight = FontWeight.Bold)
            Text(usuario.correo, style = MaterialTheme.typography.bodySmall)
            Text(usuario.tipoUsuario, style = MaterialTheme.typography.bodySmall)
            Text("Medio: ${usuario.medioContacto}", style = MaterialTheme.typography.bodySmall)
        }
    }
}
