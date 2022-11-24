plugins {
    alias(libs.plugins.androidLibraryPlugin)
    alias(libs.plugins.kotlinAndroidPlugin)
    alias(libs.plugins.apollo3Plugin)
}

android {
    namespace = "cz.applifting.graphqlempty.graphql"
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
    implementation(libs.androidxSecurity)
    implementation(libs.apollo3)
    implementation(libs.koinCompose)
    debugImplementation(libs.bundles.debug)
}

apollo {
    packageName.set("cz.applifting.graphqlEmpty")
}