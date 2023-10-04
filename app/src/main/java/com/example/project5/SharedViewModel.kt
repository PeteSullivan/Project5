package com.example.project5

import android.widget.TextView
import androidx.lifecycle.ViewModel
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions

class SharedViewModel : ViewModel() {

    /*
    view model stores all data for the project.
     */
    private var input = ""
    private var sourceLanguage = "en"
    private var newLanguage = "en"
    private var translation = ""
    var auto = false
    private var options = TranslatorOptions.Builder()
        .setSourceLanguage(TranslateLanguage.ENGLISH)
        .setTargetLanguage(TranslateLanguage.ENGLISH)
        .build()
    private var translator = Translation.getClient(options)

    var textChangeListener: ((String) -> Unit)? = null

    /*
    updates the mainActivity
     */
    fun updateText() {
        val newTranslation = getTranslation()
        textChangeListener?.invoke(newTranslation)
    }

    fun setInput(text: String) {
        input = text
    }

    /*
    if a language source button is pressed, it builds a translator, updates the current source
    language, and turns on automatic translations if it is pressed.
     */
    fun setSource(language: String) {
        if (language == "auto") {
            auto = true
            return
        } else {
            auto = false
        }
        sourceLanguage = language

        val source = when (sourceLanguage) {
            "en" -> TranslateLanguage.ENGLISH
            "es" -> TranslateLanguage.SPANISH
            else -> TranslateLanguage.GERMAN
        }
        val target = when (newLanguage) {
            "en" -> TranslateLanguage.ENGLISH
            "es" -> TranslateLanguage.SPANISH
            else -> TranslateLanguage.GERMAN
        }

        options = TranslatorOptions.Builder()
            .setSourceLanguage(source)
            .setTargetLanguage(target)
            .build()
        translator = Translation.getClient(options)
        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()
        translator.downloadModelIfNeeded(conditions)


    }
    /*
    same as setSource
     */
    fun setNew(language: String) {
        newLanguage = language
        val source = when (sourceLanguage) {
            "en" -> TranslateLanguage.ENGLISH
            "es" -> TranslateLanguage.SPANISH
            else -> TranslateLanguage.GERMAN
        }
        val target = when (newLanguage) {
            "en" -> TranslateLanguage.ENGLISH
            "es" -> TranslateLanguage.SPANISH
            else -> TranslateLanguage.GERMAN
        }

        options = TranslatorOptions.Builder()
            .setSourceLanguage(source)
            .setTargetLanguage(target)
            .build()
        translator = Translation.getClient(options)
        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()
        translator.downloadModelIfNeeded(conditions)
    }
    /*
    uses the current translator and input to return the correct translation.
     */
    fun getTranslation(): String {

        translator.translate(input)
            .addOnSuccessListener { translatedText ->
                translation = translatedText
            }
            .addOnFailureListener { exception ->
                translation = "translation failed"
            }

        return translation
    }
    /*
    if auto is on, this is called every time the input is updated. If there is a new most likely
    language, it updates the source language and builds a new translator.
     */
    fun updateLanguage() {
        val languageIdentifier = LanguageIdentification.getClient()
        languageIdentifier.identifyLanguage(input)
            .addOnSuccessListener { languageCode ->
                if (languageCode != "und") {
                    if (languageCode != sourceLanguage) {
                        val source = when (languageCode) {
                            "en" -> TranslateLanguage.ENGLISH
                            "es" -> TranslateLanguage.SPANISH
                            else -> TranslateLanguage.GERMAN
                        }
                        val target = when (newLanguage) {
                            "en" -> TranslateLanguage.ENGLISH
                            "es" -> TranslateLanguage.SPANISH
                            else -> TranslateLanguage.GERMAN
                        }

                        options = TranslatorOptions.Builder()
                            .setSourceLanguage(source)
                            .setTargetLanguage(target)
                            .build()
                        translator = Translation.getClient(options)
                        val conditions = DownloadConditions.Builder()
                            .requireWifi()
                            .build()
                        translator.downloadModelIfNeeded(conditions)
                    }
                    sourceLanguage = languageCode

                }
            }
            .addOnFailureListener {
                sourceLanguage = "oops"
            }

    }

}

