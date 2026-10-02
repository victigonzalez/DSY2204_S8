package com.example.prueba.util

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

/**
 * Administrador de síntesis de voz (Text-To-Speech) del sistema Android.
 *
 * Usado por la view Escribir para leer en voz alta el texto ingresado por el
 * usuario, facilitando la comunicación de una persona con discapacidad
 * auditiva hacia una persona oyente.
 *
 * @param context  Contexto de la aplicación.
 * @param onListo  Callback que informa si el motor TTS quedó disponible.
 */
class TextToSpeechManager(
    context: Context,
    private val onListo: (Boolean) -> Unit = {}
) {
    private var tts: TextToSpeech? = null
    private var disponible = false

    init {
        tts = TextToSpeech(context) { status ->
            disponible = status == TextToSpeech.SUCCESS
            if (disponible) {
                tts?.language = Locale("es", "ES")
            }
            onListo(disponible)
        }
    }

    /** Reproduce el texto en voz alta. Devuelve false si el motor no está listo. */
    fun hablar(texto: String): Boolean {
        if (!disponible || texto.isBlank()) return false
        tts?.speak(texto, TextToSpeech.QUEUE_FLUSH, null, "vozapp_utterance")
        return true
    }

    /** Detiene la reproducción en curso. */
    fun detener() {
        tts?.stop()
    }

    /** Libera los recursos del motor TTS. Llamar al salir de la pantalla. */
    fun liberar() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        disponible = false
    }
}
