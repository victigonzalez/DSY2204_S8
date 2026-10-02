package com.example.prueba.data.remote

import com.example.prueba.data.model.Mensaje
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

/**
 * Repositorio CRUD de mensajes sobre Firebase Firestore.
 *
 * Implementa las cuatro operaciones (crear, leer, actualizar y eliminar) para
 * los mensajes generados en las views Escribir y Hablar, cumpliendo el
 * requerimiento de "CRUD para las funciones de las views".
 *
 * Colección en Firestore: "mensajes".
 */
class MensajeRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val coleccion get() = db.collection("mensajes")

    /** CREATE: guarda un mensaje y devuelve su id generado. */
    suspend fun crear(mensaje: Mensaje): Resultado<String> {
        return try {
            val doc = coleccion.document()
            val conId = mensaje.copy(id = doc.id, timestamp = System.currentTimeMillis())
            doc.set(conId).await()
            Resultado.Exito(doc.id)
        } catch (e: Exception) {
            Resultado.Error(e.localizedMessage ?: "No se pudo guardar el mensaje.")
        }
    }

    /** READ: lista los mensajes de un usuario ordenados por fecha descendente. */
    suspend fun listarPorUsuario(uid: String): Resultado<List<Mensaje>> {
        return try {
            val snapshot = coleccion
                .whereEqualTo("uid", uid)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()
            val mensajes = snapshot.toObjects(Mensaje::class.java)
            Resultado.Exito(mensajes)
        } catch (e: Exception) {
            Resultado.Error(e.localizedMessage ?: "No se pudieron cargar los mensajes.")
        }
    }

    /** UPDATE: actualiza el texto de un mensaje existente. */
    suspend fun actualizar(mensaje: Mensaje): Resultado<Unit> {
        return try {
            coleccion.document(mensaje.id).set(mensaje).await()
            Resultado.Exito(Unit)
        } catch (e: Exception) {
            Resultado.Error(e.localizedMessage ?: "No se pudo actualizar el mensaje.")
        }
    }

    /** DELETE: elimina un mensaje por su id. */
    suspend fun eliminar(id: String): Resultado<Unit> {
        return try {
            coleccion.document(id).delete().await()
            Resultado.Exito(Unit)
        } catch (e: Exception) {
            Resultado.Error(e.localizedMessage ?: "No se pudo eliminar el mensaje.")
        }
    }
}
