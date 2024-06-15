package org.d3if3132.assesment03.memories.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.d3if3132.assesment03.memories.R
import org.d3if3132.assesment03.memories.model.Item

@Composable
fun DeleteDialog(
    item: Item,
    onDismissRequest: () -> Unit, onConfirmation: (String) -> Unit, id: String
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = {
            Text(text = "Want to delete this ${item.title} item ?")
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirmation(id)
            }) {
                Text(text = stringResource(id = R.string.hapus))
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text(text = stringResource(id = R.string.batal))
            }
        }
    )
}