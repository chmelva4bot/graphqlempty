plugins {
    alias(libs.plugins.androidApplicationPlugin)
    alias(libs.plugins.kotlinAndroidPlugin)
    alias(libs.plugins.apollo3Plugin)
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

    implementation project(":common-android")

    implementation(libs.androidxKtx)
    implementation(libs.androidxLifecycle)
    implementation(libs.androidxLifecycleViewModelCompose)
    implementation(libs.composeActivity)
    implementation(libs.bundles.composeUi)


    implementation(libs.composeNav)
    implementation(libs.coilCompose)
    implementation(libs.androidxSecurity)

    implementation(libs.apollo3)

//    implementation 'com.google.dagger:hilt-android:2.44'
//    kapt 'com.google.dagger:hilt-compiler:2.44'
//    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
//    kapt("androidx.hilt:hilt-compiler:1.0.0")

    implementation(libs.koinCompose)

    implementation(libs.bundles.firebase)


    testImplementation(libs.bundles.test)

    androidTestImplementation(libs.bundles.androidTest)
    debugImplementation(libs.bundles.debug)
}

kapt {
    correctErrorTypes = true
}

apollo {
    packageName.set("cz.applifting.graphqlEmpty")
}