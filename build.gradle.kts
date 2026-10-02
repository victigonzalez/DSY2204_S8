// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.jetbrainsKotlinSerialization) apply false
    //  plugin de Google Services para integrar Firebase
    alias(libs.plugins.google.services) apply false
}
