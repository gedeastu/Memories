package org.d3if3132.assesment03.memories.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.d3if3132.assesment03.memories.R
import org.d3if3132.assesment03.memories.model.Item
import org.d3if3132.assesment03.memories.model.User
import org.d3if3132.assesment03.memories.network.ItemApi
import org.d3if3132.assesment03.memories.ui.presentation.MainViewModel

@Composable
fun ListItem(item: Item, user: User, viewModel: MainViewModel) {
    var onShowDeleting by remember {
        mutableStateOf(false)
    }
    Column(modifier = Modifier.padding(2.dp)
        .border(3.dp, MaterialTheme.colorScheme.primary).clip(
            RectangleShape), Arrangement.Center, Alignment.CenterHorizontally) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(
                    ItemApi.getItemUrl(imageId = item.imageId)
                )
                .crossfade(true)
                .build(),
            contentDescription = stringResource(id = R.string.gambar, item.title),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.loading_img),
            error = painterResource(id = R.drawable.baseline_broken_image_24),
            modifier = Modifier
                .fillMaxWidth()
            )
        Row(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.primary).border(2.dp,Color.Transparent,shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
            Column(modifier = Modifier
                .padding(4.dp)
                .padding(4.dp)){
                Text(text = item.title, fontWeight = FontWeight.Bold, color = Color.White)
                Text(text = item.description, fontStyle = FontStyle.Italic, fontSize = 14.sp, color = Color.White)
            }
            IconButton(onClick = {
                    onShowDeleting = true
            }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = stringResource(id = R.string.hapus), tint = MaterialTheme.colorScheme.surface)
            }
            if (onShowDeleting){
                DeleteDialog(onDismissRequest = { onShowDeleting = false }, onConfirmation = {
                    onShowDeleting = false
                    viewModel.deletingData(userId = user.email, id = item.id.toString())
                }, id = item.id.toString(), item = item)
            }
        }
    }
}