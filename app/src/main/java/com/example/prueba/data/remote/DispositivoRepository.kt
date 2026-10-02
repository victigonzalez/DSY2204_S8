package com.example.prueba.data.remote

import com.example.prueba.data.model.Dispositivo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

/**
 * Repositorio CRUD de dispositivos localizados sobre Firebase Firestore.
 *
 * Guarda y recupera la última ubicación conocida de los dispositivos
 * registrados por el usuario en la view BuscarDispositivo.
 *
 * Colección en Firestore: "dispositivos".
 */
class DispositivoRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val coleccion get() = db.collection("dispositivos")

    /** CREATE: registra un dispositivo con su ubicación. */
    suspend fun crear(dispositivo: Dispositivo): Resultado<String> {
        return try {
            val doc = coleccion.document()
            val conId = dispositivo.copy(id = doc.id, timestamp = System.currentTimeMillis())
            doc.set(conId).await()
            Resultado.Exito(doc.id)
        } catch (e: Exception) {
            Resultado.Error(e.localizedMessage ?: "No se pudo guardar el dispositivo.")
        }
    }

    /** READ: lista los dispositivos de un usuario. */
    suspend fun listarPorUsuario(uid: String): Resultado<List<Dispositivo>> {
        return try {
            val snapshot = coleccion
                .whereEqualTo("uid", uid)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()
            Resultado.Exito(snapshot.toObjects(Dispositivo::class.java))
        } catch (e: Exception) {
            Resultado.Error(e.localizedMessage ?: "No se pudieron cargar los dispositivos.")
        }
    }

    /** UPDATE: actualiza la ubicación de un dispositivo. */
    suspend fun actualizar(dispositivo: Dispositivo): Resultado<Unit> {
        return try {
            coleccion.document(dispositivo.id).set(dispositivo).await()
            Resultado.Exito(Unit)
        } catch (e: Exception) {
            Resultado.Error(e.localizedMessage ?: "No se pudo actualizar el dispositivo.")
        }
    }

    /** DELETE: elimina un dispositivo por su id. */
    suspend fun eliminar(id: String): Resultado<Unit> {
        return try {
            coleccion.document(id).delete().await()
            Resultado.Exito(Unit)
        } catch (e: Exception) {
            Resultado.Error(e.localizedMessage ?: "No se pudo eliminar el dispositivo.")
        }
    }
}
