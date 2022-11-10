plugins {
    alias(libs.plugins.androidApplicationPlugin).apply(false)
    alias(libs.plugins.kotlinAndroidPlugin).apply(false)
    alias(libs.plugins.apollo3Plugin).apply(false)
//    alias(libs.plugins.daggerHiltAndroidPlugin).apply(false)
    alias(libs.plugins.googleServicesPlugin).apply(false)
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
            minifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = '1.8'
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

    implementation("com.apollographql.apollo3:apollo-runtime:3.6.2")

//    implementation 'com.google.dagger:hilt-android:2.44'
//    kapt 'com.google.dagger:hilt-compiler:2.44'
//    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
//    kapt("androidx.hilt:hilt-compiler:1.0.0")

    implementation "io.insert-koin:koin-androidx-compose:3.3.0"

    // Google Sign In SDK
    implementation 'com.google.android.gms:play-services-auth:20.2.0'

    // Firebase SDK
    implementation platform('com.google.firebase:firebase-bom:30.3.2')
    implementation 'com.google.firebase:firebase-database-ktx'
    implementation 'com.google.firebase:firebase-storage-ktx'
    implementation 'com.google.firebase:firebase-auth-ktx'
    implementation 'com.google.firebase:firebase-messaging-ktx'

    // Firebase UI Library
    implementation 'com.firebaseui:firebase-ui-auth:8.0.2'
    implementation 'com.firebaseui:firebase-ui-database:8.0.2'

    testImplementation 'junit:junit:4.13.2'
    testImplementation "org.jetbrains.kotlinx:kotlinx-coroutines-test"
    testImplementation("io.mockk:mockk:1.13.2")

    androidTestImplementation 'androidx.test.ext:junit:1.1.3'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.4.0'
    androidTestImplementation "androidx.compose.ui:ui-test-junit4:$compose_ui_version"
    debugImplementation "androidx.compose.ui:ui-tooling:$compose_ui_version"
    debugImplementation "androidx.compose.ui:ui-test-manifest:$compose_ui_version"
}

kapt {
    correctErrorTypes = true
}

apollo {
    packageName.set("cz.applifting.graphqlEmpty")
}