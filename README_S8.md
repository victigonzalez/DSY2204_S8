# VozApp — Guía de la Sumativa 3 (Semana 8)

Preparación de la aplicación móvil Android/Kotlin para su distribución.

---

## Qué trae el proyecto

- **Front end (Kotlin + Jetpack Compose)**: views Login, Registro, Recuperar
  contraseña, **HomeMenú**, **Escribir**, **Hablar** y **BuscarDispositivo**.
- **Back end (Firebase)**: `AuthService` (autenticación), `MensajeRepository`,
  `DispositivoRepository` y `UsuarioRemoteRepository` (CRUD en Firestore).
- **Extensiones KTX**: `lifecycle-viewmodel-compose`, `activity-ktx`,
  `fragment-ktx`, corrutinas (`viewModelScope`, `await`), `core-ktx`.
- **Funciones nativas**: TextToSpeech (Escribir) y SpeechRecognizer (Hablar)
  y geolocalización con Fused Location Provider (BuscarDispositivo).
- **Pruebas unitarias (JUnit + Mockito)** en `app/src/test/`.

> Credenciales locales de prueba: `ana@correo.cl` / `Ana12345`.

---


## Ejecutar las pruebas unitarias 

En Android Studio, clic derecho sobre `app/src/test/java/com/example/prueba`
→ **Run 'Tests in prueba'**. O por terminal:

```
./gradlew testDebugUnitTest
```

Pruebas incluidas: `UsuarioModelTest`, `UsuarioRepositoryTest` (JUnit) y
`MensajesViewModelTest` (Mockito + corrutinas).

---
