// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    //Ajout des plugins gradle
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.room) apply false
    alias(libs.plugins.hilt) apply false
    kotlin("jvm") version "1.9.0"
}
val defaultTargetSdkVersion by extra(libs.versions.targetSdk.get().toInt())
val defaultMinSdkVersion by extra(libs.versions.minSdk.get().toInt())

extra["defaultMinSdkVersion"] = libs.versions.minSdk.get().toInt()
extra["defaultTargetSdkVersion"] = libs.versions.targetSdk.get().toInt()
