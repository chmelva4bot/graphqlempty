plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "cz.applifting.graphqlempty.firebasechat"
    compileSdk = 32

    defaultConfig {
        minSdk = 26
        targetSdk = 32

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {
    implementation(project(":common-android"))
    implementation(project(":nav-common"))

    implementation(libs.androidxKtx)
    implementation(libs.androidxLifecycle)
    implementation(libs.androidxLifecycleViewModelCompose)
    implementation(libs.bundles.composeUi)
    implementation(libs.coilCompose)
    implementation(libs.koinCompose)

    implementation(platform(libs.firebaseBOM))
    implementation(libs.bundles.firebase)

    testImplementation(libs.bundles.test)
    androidTestImplementation(libs.bundles.androidTest)
    debugImplementation(libs.bundles.debug)
}