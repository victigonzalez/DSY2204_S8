package com.example.prueba.util

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import java.util.Locale

/**
 * Administrador de reconocimiento de voz (Speech-To-Text) del sistema Android.
 *
 * Usado por la view Hablar para transcribir a texto lo que dice una persona
 * oyente, de modo que la persona con discapacidad auditiva pueda leerlo.
 *
 * @param context Contexto de la aplicación.
 */
class SpeechRecognizerManager(private val context: Context) {

    private var recognizer: SpeechRecognizer? = null

    /** Indica si el dispositivo dispone de reconocimiento de voz. */
    fun disponible(): Boolean = SpeechRecognizer.isRecognitionAvailable(context)

    /**
     * Inicia la escucha del micrófono.
     *
     * @param onResultado Texto reconocido.
     * @param onError     Mensaje de error legible.
     * @param onListo     Se invoca cuando el reconocedor está listo para escuchar.
     */
    fun escuchar(
        onResultado: (String) -> Unit,
        onError: (String) -> Unit,
        onListo: () -> Unit = {}
    ) {
        if (!disponible()) {
            onError("El reconocimiento de voz no está disponible en este dispositivo.")
            return
        }

        recognizer?.destroy()
        recognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
            setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) = onListo()
                override fun onResults(results: Bundle?) {
                    val textos = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    val texto = textos?.firstOrNull().orEmpty()
                    if (texto.isBlank()) onError("No se reconoció ninguna palabra.")
                    else onResultado(texto)
                }
                override fun onError(error: Int) = onError(descripcionError(error))
                override fun onBeginningOfSpeech() {}
                override fun onEndOfSpeech() {}
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onPartialResults(partialResults: Bundle?) {}
                override fun onEvent(eventType: Int, params: Bundle?) {}
            })
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale("es", "ES"))
        }
        recognizer?.startListening(intent)
    }

    /** Detiene la escucha y libera el reconocedor. */
    fun detener() {
        recognizer?.stopListening()
        recognizer?.destroy()
        recognizer = null
    }

    /** Traduce los códigos de error de SpeechRecognizer a mensajes legibles. */
    private fun descripcionError(error: Int): String = when (error) {
        SpeechRecognizer.ERROR_AUDIO -> "Error de audio."
        SpeechRecognizer.ERROR_CLIENT -> "Error del cliente."
        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Faltan permisos de micrófono."
        SpeechRecognizer.ERROR_NETWORK -> "Error de red."
        SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Tiempo de red agotado."
        SpeechRecognizer.ERROR_NO_MATCH -> "No se reconoció ninguna palabra."
        SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "El reconocedor está ocupado."
        SpeechRecognizer.ERROR_SERVER -> "Error del servidor."
        SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No se detectó voz."
        else -> "Error desconocido de reconocimiento."
    }
}
