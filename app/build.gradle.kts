plugins {
    alias(libs.plugins.androidApplicationPlugin)
    alias(libs.plugins.kotlinAndroidPlugin)
//    alias(libs.plugins.daggerHiltAndroidPlugin)
    alias(libs.plugins.googleServicesPlugin)
}

android {
    namespace = "cz.applifting.graphqlempty"
    compileSdk = 32

    defaultConfig {
        applicationId = "cz.applifting.graphqlempty"
        minSdk = 26
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeUi.get()
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(project(":nav-common"))
    implementation(project(":common-android"))
    implementation(project(":graphql"))
    implementation(project(":firebase-chat"))

    implementation(libs.androidxKtx)
    implementation(libs.composeActivity)
    implementation(libs.bundles.composeUi)
    implementation(libs.composeNav)

    implementation(libs.koinCompose)
}