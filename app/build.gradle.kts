import org.gradle.api.JavaVersion

plugins {
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.android.application)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    alias(libs.plugins.hilt)

}

android {
    namespace = "com.openclassrooms.vitesse"
    compileSdk = 35
    val defaultTargetSdkVersion: Int by rootProject.extra
    defaultConfig {
        applicationId = "com.openclassrooms.vitesse"
        minSdk = 26
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.hilt.android)
    implementation(libs.espresso)
    implementation(libs.glide)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.navigation.ui.ktx)

    ksp(libs.room.compiler)

    implementation(libs.core.ktx)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.activity.ktx)
    implementation(libs.fragment.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.core.splashscreen)


    coreLibraryDesugaring(libs.desugar)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso)

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    implementation(libs.retrofit)
    implementation(libs.converter.moshi)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    ksp(libs.moshi.kotlin.codegen)
    implementation(libs.play.services.maps)

    androidTestImplementation(libs.turbine)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(libs.arch.core.common)
    implementation(libs.arch.core.runtime)
    testImplementation(libs.arch.core.testing)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.turbine)
    testImplementation(libs.kotlin.test)
    implementation(kotlin("test"))

    testImplementation(libs.mockito.inline)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
}
