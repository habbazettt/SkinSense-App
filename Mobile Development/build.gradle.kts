// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
    }
    dependencies {
        classpath (libs.androidx.navigation.safe.args.gradle.plugin)
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    id ("com.google.devtools.ksp") version "1.9.0-1.0.13" // Tambahkan KSP
//    id("androidx.navigation.safeargs.kotlin") version "2.8.4" apply false
}