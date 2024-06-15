package org.d3if3132.assesment03.memories.ui.screen

import android.content.Context
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.ClearCredentialException
import androidx.credentials.exceptions.GetCredentialException
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if3132.assesment03.memories.BuildConfig
import org.d3if3132.assesment03.memories.R
import org.d3if3132.assesment03.memories.model.User
import org.d3if3132.assesment03.memories.network.UserDataStore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(modifier: Modifier = Modifier, navController: NavHostController) {
    val context = LocalContext.current
    val dataStore = UserDataStore(context = context)
    val user by dataStore.userFlow.collectAsState(initial = User())
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Profile")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.surface
                ),
            )
        }
    ){paddingValues ->
        Column(modifier = modifier
            .padding(paddingValues = paddingValues)
            .padding(10.dp).fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
            if (user.email.isEmpty()){
                Button(onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        signIn(context = context, dataStore = dataStore)
                    }
                }) {
                    Text(text = "Login")
                }
            }else{
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    AsyncImage(model = ImageRequest.Builder(LocalContext.current).data(user.photoUrl).crossfade(true).build(), contentDescription = null, contentScale = ContentScale.Crop, placeholder = painterResource(
                        id = R.drawable.loading_img
                    ), error = painterResource(id = R.drawable.baseline_broken_image_24), modifier = Modifier.size(100.dp).clip(
                        CircleShape))
                    Text(text = user.nama, maxLines = 1, overflow = TextOverflow.Ellipsis, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 16.dp))
                    Text(text = user.email, maxLines = 1, overflow = TextOverflow.Ellipsis)

                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp), horizontalArrangement = Arrangement.Center){
                        OutlinedButton(onClick = { navController.popBackStack() }, modifier = Modifier.padding(8.dp)) {
                            Text(text = stringResource(id = R.string.tutup))
                        }
                        OutlinedButton(onClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                signOut(context = context, dataStore = dataStore)
                            }
                        }, modifier = Modifier.padding(8.dp), border = BorderStroke(1.dp,MaterialTheme.colorScheme.error)) {
                            Text(text = stringResource(id = R.string.logout), color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}
private suspend fun signIn(context: Context, dataStore: UserDataStore){
    val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(BuildConfig.API_KEY)
        .build()
    val request: GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()
    try {
        val credentialManager = CredentialManager.create(context = context)
        val result = credentialManager.getCredential(context = context, request = request)
        handleSignIn(result = result, dataStore = dataStore)
    }catch (e: GetCredentialException){
        Log.e("SIGN-IN", "Error: ${e.message}")
    }
}

private suspend fun handleSignIn(result: GetCredentialResponse, dataStore: UserDataStore){
    val credential = result.credential
    if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL){
        try {
            val googleId = GoogleIdTokenCredential.createFrom(credential.data)
            //Log.d("SIGN-IN", "User email : ${googleId.id}")
            val nama = googleId.displayName ?: ""
            val email = googleId.id
            val photoUrl = googleId.profilePictureUri.toString()
            dataStore.saveData(User(nama = nama, email = email, photoUrl = photoUrl ))
        }catch (e: GoogleIdTokenParsingException){
            Log.e("SIGN-IN", "Error : ${e.message}")
        }
    }else{
        Log.e("SIGN-IN","Error : unrecognized custom credential type")
    }
}

suspend fun signOut(context: Context,dataStore: UserDataStore){
    try {
        val credentialManager = CredentialManager.create(context = context)
        credentialManager.clearCredentialState(
            ClearCredentialStateRequest()
        )
        dataStore.saveData(User())
    }catch (e: ClearCredentialException){
        Log.e("SIGN-IN","Error: ${e.errorMessage}")
    }
}