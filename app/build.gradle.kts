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
    defaultConfig {
        applicationId = "com.openclassrooms.vitesse"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
    //Configuration du plugin gradle de room
    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.hilt.android)
    implementation(libs.espresso)
    ksp(libs.room.compiler)

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.activity.ktx)
    implementation(libs.fragment.ktx)

    coreLibraryDesugaring(libs.desugar)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso)

    //ajout des dépendances room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    //ajout de la dépendance Turbine
    androidTestImplementation(libs.turbine)

    //ajout de hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(libs.arch.core.common)
    implementation(libs.arch.core.runtime)
    testImplementation(libs.arch.core.testing)

    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.turbine)
    testImplementation(libs.kotlin.test)
}