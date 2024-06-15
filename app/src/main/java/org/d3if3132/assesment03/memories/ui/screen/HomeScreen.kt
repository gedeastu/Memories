package org.d3if3132.assesment03.memories.ui.screen

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import org.d3if3132.assesment03.memories.R
import org.d3if3132.assesment03.memories.model.User
import org.d3if3132.assesment03.memories.navigation.Route
import org.d3if3132.assesment03.memories.network.ApiStatus
import org.d3if3132.assesment03.memories.network.UserDataStore
import org.d3if3132.assesment03.memories.ui.components.ItemDialog
import org.d3if3132.assesment03.memories.ui.components.ListItem
import org.d3if3132.assesment03.memories.ui.presentation.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val context = LocalContext.current
    val dataStore = UserDataStore(context = context)
    val user by dataStore.userFlow.collectAsState(initial = User())
    var showItemDialog by remember {
        mutableStateOf(false)
    }
    var bitmap: Bitmap? by remember {
        mutableStateOf(null)
    }
    val launcher = rememberLauncherForActivityResult(contract = CropImageContract()) {
        bitmap = getCroppedImage(context.contentResolver, it)
        if (bitmap != null) showItemDialog = true
    }
    val viewModel : MainViewModel = viewModel()
    val errorMessage by viewModel.errorMessage

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Home")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.surface
                ),
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Route.PROFILE_SCREEN)
                    }) {
                        Icon(painter = painterResource(id = R.drawable.baseline_account_circle_24), contentDescription = stringResource(
                            id = R.string.profile
                        ), tint = MaterialTheme.colorScheme.surface)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (user.email.isNotEmpty()){
                    val options = CropImageContractOptions(
                        uri = null, CropImageOptions(
                            imageSourceIncludeCamera = true,
                            imageSourceIncludeGallery = false,
                            fixAspectRatio = true
                        )
                    )
                    launcher.launch(options)
                }else{
                    navController.navigate(Route.PROFILE_SCREEN)
                }
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = stringResource(id = R.string.tambah_hewan))
            }
        }
    ){paddingValues ->
        ScreenContent(
            modifier = Modifier.padding(paddingValues = paddingValues),
            viewModel = viewModel,
            userId = user.email,
            user = user
        )
        if (showItemDialog){
            ItemDialog(
                bitmap = bitmap,
                onDismissRequest = { showItemDialog = false },
                onConfirmation = { nama, namaLatin ->
                    //Log.d("TAMBAH", "$nama $namaLatin ditambahkan.")
                    showItemDialog = false
                    viewModel.saveData(user.email, nama, namaLatin, bitmap!!)
                })
        }

        if (errorMessage != null){
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            viewModel.clearMessage()
        }
    }
}

@Composable
fun ScreenContent(modifier: Modifier, viewModel: MainViewModel, userId: String, user: User) {
    val data by viewModel.data
    val status by viewModel.status.collectAsState()
    LaunchedEffect(key1 = userId) {
        viewModel.getData(userId = userId)
    }

    when(status){
        ApiStatus.LOADING -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                CircularProgressIndicator()
            }
        }
        ApiStatus.SUCCESS -> {
            if (data.isNotEmpty()){
                LazyVerticalGrid(
                    modifier = modifier
                        .fillMaxSize(),
                    //.padding(4.dp),
                    columns = GridCells.Fixed(2),
                ){
                    items(data){
                        ListItem(item = it, user = user, viewModel = viewModel)
                    }
                }
            }else{
                Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                    Icon(painter = painterResource(id = R.drawable.baseline_book_24), contentDescription = stringResource(
                        id = R.string.empty
                    ), tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(100.dp))
                    Text(text = stringResource(id = R.string.empty), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                }
            }
        }
        ApiStatus.FAILED -> {
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                Text(text = stringResource(id = R.string.error))
                Button(onClick = { viewModel.getData(userId = userId) }, modifier = Modifier.padding(16.dp), contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)) {
                    Text(text = stringResource(id = R.string.try_again))
                }
            }
        }
    }
}

private fun getCroppedImage(
    resolver: ContentResolver,
    result: CropImageView.CropResult
) : Bitmap?{
    if (!result.isSuccessful){
        Log.e("IMAGE","Error: ${result.error}")
        return null
    }
    val uri = result.uriContent ?: return null
    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P){
        MediaStore.Images.Media.getBitmap(resolver,uri)
    }else{
        val source = ImageDecoder.createSource(resolver,uri)
        ImageDecoder.decodeBitmap(source)
    }
}
