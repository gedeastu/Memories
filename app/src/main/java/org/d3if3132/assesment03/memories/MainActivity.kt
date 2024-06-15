package org.d3if3132.assesment03.memories

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import org.d3if3132.assesment03.memories.model.User
import org.d3if3132.assesment03.memories.navigation.SetupNavGraph
import org.d3if3132.assesment03.memories.network.UserDataStore
import org.d3if3132.assesment03.memories.ui.presentation.MainViewModel
import org.d3if3132.assesment03.memories.ui.screen.MainScreen
import org.d3if3132.assesment03.memories.ui.theme.MemoriesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MemoriesTheme {
                    val context = LocalContext.current
                    val dataStore = UserDataStore(context = context)
                    val user by dataStore.userFlow.collectAsState(initial = User())
                    val navHostController = rememberNavController()
                    val viewModel : MainViewModel = viewModel()
                    MainScreen(navHostController = navHostController, content = { modifier ->
                        Box(modifier = modifier.fillMaxSize()){
                            SetupNavGraph(
                                navController = navHostController,
                                user = user,
                                viewModel = viewModel
                            )
                        }
                    })
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MemoriesTheme {
        Greeting("Android")
    }
}