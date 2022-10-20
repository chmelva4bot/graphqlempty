package cz.applifting.graphqlempty

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cz.applifting.graphqlEmpty.LaunchListQuery
import cz.applifting.graphqlempty.ui.theme.GraphqlEmptyTheme
import cz.applifting.graphqlempty.ui.theme.apolloClient
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GraphqlEmptyTheme { // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {

                    val scope = rememberCoroutineScope()

                    LaunchedEffect(true) {
                        scope.launch {
                            val response = apolloClient.query(LaunchListQuery()).execute()
                            Log.d("LaunchList", "Success ${response.data}")
                        }
                    }


                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GraphqlEmptyTheme {
        Greeting("Android")
    }
}