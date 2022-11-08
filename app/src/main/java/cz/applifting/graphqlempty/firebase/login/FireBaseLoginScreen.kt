package cz.applifting.graphqlempty.firebase.login

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import cz.applifting.graphqlempty.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun FirebaseLoginScreen(navController: NavController) {

    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    
    val launcher = rememberLauncherForActivityResult(contract = FirebaseAuthUIActivityResultContract()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Log.d("FAuth", "Sign in successful!")
        } else {
            Toast.makeText(
                ctx,
                "There was an error signing in",
                Toast.LENGTH_LONG).show()

            val response = result.idpResponse
            if (response == null) {
                Log.w("FAuth", "Sign in canceled")
            } else {
                Log.w("FAuth", "Sign in error", response.error)
            }
        }
        scope.launch {
//            delay(2000)
            navController.popBackStack()
        }
    }

    LaunchedEffect(true) {
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setTheme(R.style.MyTheme)
            .setLogo(R.mipmap.ic_launcher)
            .setAvailableProviders(listOf(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build(),
            ))
            .build()
        launcher.launch(signInIntent)
    }

    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
}