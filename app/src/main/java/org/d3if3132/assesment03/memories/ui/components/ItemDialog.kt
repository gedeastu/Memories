package org.d3if3132.assesment03.memories.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.d3if3132.assesment03.memories.R

@Composable
fun ItemDialog(bitmap: Bitmap?, onDismissRequest: ()->Unit, onConfirmation:(String, String)->Unit) {
    var title by remember { mutableStateOf("") }
    var description by remember {
        mutableStateOf("")
    }
    Dialog(onDismissRequest = {
        onDismissRequest()
    }) {
        Card(modifier = Modifier.padding(16.dp), shape = RoundedCornerShape(16.dp)) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally){
                Image(bitmap = bitmap!!.asImageBitmap(), contentDescription = null, modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f))
                OutlinedTextField(value = title, onValueChange = {
                    title = it
                }, label = {
                    Text(text = stringResource(id = R.string.title))
                },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.padding(top = 8.dp)
                )
                OutlinedTextField(value = description, onValueChange = {
                    description = it
                }, label = {
                    Text(text = stringResource(id = R.string.desc))
                },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier.padding(top = 8.dp)
                )
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp), horizontalArrangement = Arrangement.Center){
                    OutlinedButton(onClick = { onDismissRequest() }, modifier = Modifier.padding(8.dp)) {
                        Text(text = stringResource(id = R.string.batal))
                    }
                    OutlinedButton(onClick = { onConfirmation(title,description) }, enabled = title.isNotEmpty() && description.isNotEmpty(), modifier = Modifier.padding(8.dp)) {
                        Text(text = stringResource(id = R.string.simpan))
                    }
                }
            }
        }
    }
}