plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id ("androidx.navigation.safeargs")
    id("com.google.devtools.ksp") version "1.9.0-1.0.13" // Plugin KSP
}

android {
    namespace = "com.capstone.skinsense"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.capstone.skinsense"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    // Tambahkan KSP untuk menghasilkan file Room
    kotlin {
        ksp {
            arg("room.incremental", "true")
        }
    }

    buildTypes {
        getByName("debug") {
            // Menambahkan BASE_URL ke BuildConfig untuk debug build
            buildConfigField("String", "BASE_URL", "\"${project.findProperty("baseUrl") ?: "https://default-url.com"}\"")
            // Menambahkan IS_DEBUG ke BuildConfig untuk debug build
            buildConfigField("Boolean", "IS_DEBUG", "true")
        }
        getByName("release") {
            // Menambahkan BASE_URL ke BuildConfig untuk release build
            buildConfigField("String", "BASE_URL", "\"${project.findProperty("baseUrl") ?: "https://default-url.com"}\"")
            // Menambahkan IS_DEBUG ke BuildConfig untuk release build
            buildConfigField("Boolean", "IS_DEBUG", "false")
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation (libs.androidx.room.runtime)
    implementation (libs.androidx.room.ktx)

    ksp ("androidx.room:room-compiler:2.5.0")

    implementation(libs.androidx.datastore.preferences)

    implementation(libs.glide)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

// ViewModel & LiveData
    implementation (libs.androidx.lifecycle.viewmodel.ktx)
    implementation (libs.androidx.lifecycle.livedata.ktx)

// RecyclerView
    implementation (libs.androidx.recyclerview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.camera.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}