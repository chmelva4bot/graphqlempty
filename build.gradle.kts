plugins {
   alias(libs.plugins.androidApplicationPlugin).apply(false)
   alias(libs.plugins.androidLibraryPlugin).apply(false)
   alias(libs.plugins.kotlinAndroidPlugin).apply(false)
   alias(libs.plugins.apollo3Plugin).apply(false)
   alias(libs.plugins.daggerHiltAndroidPlugin).apply(false)
   alias(libs.plugins.googleServicesPlugin).apply(false)
}

tasks.register("clean", Delete::class){
    delete(rootProject.buildDir)
}