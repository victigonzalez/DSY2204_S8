package com.example.prueba.data.remote

import com.example.prueba.data.model.PerfilUsuario
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * Repositorio CRUD de perfiles de usuario sobre Firebase Firestore.
 *
 * Gestiona los datos de perfil asociados a cada usuario autenticado,
 * cumpliendo el requerimiento de "CRUD para la gestión de usuarios".
 *
 * Colección en Firestore: "usuarios" (documento identificado por el uid).
 */
class UsuarioRemoteRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val coleccion get() = db.collection("usuarios")

    /** CREATE / UPDATE: crea o reemplaza el perfil de un usuario. */
    suspend fun guardar(perfil: PerfilUsuario): Resultado<Unit> {
        return try {
            coleccion.document(perfil.uid).set(perfil).await()
            Resultado.Exito(Unit)
        } catch (e: Exception) {
            Resultado.Error(e.localizedMessage ?: "No se pudo guardar el perfil.")
        }
    }

    /** READ: obtiene el perfil de un usuario por su uid. */
    suspend fun obtener(uid: String): Resultado<PerfilUsuario?> {
        return try {
            val doc = coleccion.document(uid).get().await()
            Resultado.Exito(doc.toObject(PerfilUsuario::class.java))
        } catch (e: Exception) {
            Resultado.Error(e.localizedMessage ?: "No se pudo cargar el perfil.")
        }
    }

    /** READ: lista todos los perfiles registrados. */
    suspend fun listar(): Resultado<List<PerfilUsuario>> {
        return try {
            val snapshot = coleccion.get().await()
            Resultado.Exito(snapshot.toObjects(PerfilUsuario::class.java))
        } catch (e: Exception) {
            Resultado.Error(e.localizedMessage ?: "No se pudieron cargar los perfiles.")
        }
    }

    /** DELETE: elimina el perfil de un usuario. */
    suspend fun eliminar(uid: String): Resultado<Unit> {
        return try {
            coleccion.document(uid).delete().await()
            Resultado.Exito(Unit)
        } catch (e: Exception) {
            Resultado.Error(e.localizedMessage ?: "No se pudo eliminar el perfil.")
        }
    }
}
