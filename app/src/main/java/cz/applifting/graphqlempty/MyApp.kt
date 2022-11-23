package cz.applifting.graphqlempty

import android.app.Application
import cz.applifting.graphqlempty.firebasechat.di.firebaseModule
import cz.applifting.graphqlempty.graphql.di.gqlModule
import cz.applifting.graphqlempty.navigation.navigationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

//@HiltAndroidApp
class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            // Log Koin into Android logger
            androidLogger()
            // Reference Android context
            androidContext(this@MyApp)
            // Load modules
            modules(gqlModule, firebaseModule, navigationModule)
        }
    }
}