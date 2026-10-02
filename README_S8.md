# VozApp — Guía de la Sumativa 3 (Semana 8)

Preparación de la aplicación móvil Android/Kotlin para su distribución.
Esta guía resume lo implementado y **los pasos que debes completar tú**
(crear el proyecto Firebase, firmar el APK y publicarlo), ya que requieren
tus propias cuentas y contraseñas.

---

## 1. Qué trae el proyecto

- **Front end (Kotlin + Jetpack Compose)**: views Login, Registro, Recuperar
  contraseña, **HomeMenú**, **Escribir**, **Hablar** y **BuscarDispositivo**.
- **Back end (Firebase)**: `AuthService` (autenticación), `MensajeRepository`,
  `DispositivoRepository` y `UsuarioRemoteRepository` (CRUD en Firestore).
- **Extensiones KTX**: `lifecycle-viewmodel-compose`, `activity-ktx`,
  `fragment-ktx`, corrutinas (`viewModelScope`, `await`), `core-ktx`.
- **Funciones nativas**: TextToSpeech (Escribir) y SpeechRecognizer (Hablar)
  y geolocalización con Fused Location Provider (BuscarDispositivo).
- **Pruebas unitarias (JUnit + Mockito)** en `app/src/test/`.

> La app funciona **sin Firebase** para que puedas probar la interfaz: el
> login usa un respaldo local (arreglo de 5 usuarios) si Firebase no está
> configurado. Al conectar Firebase, la autenticación y el CRUD pasan a la nube
> automáticamente.
>
> Credenciales locales de prueba: `ana@correo.cl` / `Ana12345`.

---

## 2. Conectar TU proyecto Firebase (Criterio 5)

1. Entra a <https://console.firebase.google.com> e inicia sesión.
2. **Agregar proyecto** → nómbralo (p. ej. `vozapp`) → crea el proyecto.
3. Dentro del proyecto: **Compilación → Authentication → Comenzar** y activa
   el proveedor **Correo electrónico/contraseña**.
4. **Compilación → Firestore Database → Crear base de datos** (modo de prueba
   para empezar). Colecciones que usa la app: `usuarios`, `mensajes`,
   `dispositivos` (se crean solas al guardar el primer dato).
5. En **Descripción general del proyecto** → icono Android, registra la app con
   el **nombre de paquete exacto**: `com.example.prueba`.
6. Descarga el archivo **`google-services.json`** y **reemplaza** el que está
   en `app/google-services.json` (el incluido es solo una plantilla).
7. En Android Studio: **Sync Project with Gradle Files** y ejecuta la app.

Reglas de Firestore sugeridas para desarrollo (Firestore → Reglas):

```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read, write: if request.auth != null;
    }
  }
}
```

---

## 3. Ejecutar las pruebas unitarias (Criterio 6)

En Android Studio, clic derecho sobre `app/src/test/java/com/example/prueba`
→ **Run 'Tests in prueba'**. O por terminal:

```
./gradlew testDebugUnitTest
```

Pruebas incluidas: `UsuarioModelTest`, `UsuarioRepositoryTest` (JUnit) y
`MensajesViewModelTest` (Mockito + corrutinas).

---

## 4. Generar el APK (Criterio 6)

En Android Studio: **Build → Build Bundle(s) / APK(s) → Build APK(s)**.
El APK de depuración queda en `app/build/outputs/apk/debug/app-debug.apk`.

---

## 5. Firmar el APK (Criterio 7)

1. **Build → Generate Signed Bundle / APK… → APK → Next**.
2. **Create new…** para crear tu *keystore*:
   - Elige ruta y archivo (p. ej. `vozapp-keystore.jks`).
   - Define **contraseña del keystore** y **del alias** (guárdalas; sin ellas
     no podrás volver a firmar).
   - Completa alias (p. ej. `vozapp`) y tus datos.
3. Selecciona **release**, marca V1 y V2, **Finish**.
4. El APK firmado queda en `app/release/app-release.apk`.

> No subas el archivo `.jks` ni las contraseñas al repositorio.

---

## 6. Publicar el APK (Criterio 8)

Opción recomendada (gratuita, simula una tienda): **Firebase App Distribution**.

1. En Firebase: **Lanzamiento y supervisión → App Distribution → Comenzar**.
2. Sube el `app-release.apk`.
3. Agrega tu correo como probador o crea un **grupo** y comparte el enlace de
   descarga que genera Firebase.

Alternativas válidas: subir el APK a un repositorio de *Releases* en GitHub, o
a un servicio de descarga gratuito, y compartir el enlace. Incluye ese enlace
en el informe (documento PDF).

---

## 7. Subir a Git (Criterio 2)

```
git add -A
git commit -m "Sumativa 3 S8 - Firebase, KTX, voz/geolocalización y APK"
git push
```

> Recuerda: revisa y comprende el código antes de entregar. Úsalo como base
> y adáptalo con tu criterio.
