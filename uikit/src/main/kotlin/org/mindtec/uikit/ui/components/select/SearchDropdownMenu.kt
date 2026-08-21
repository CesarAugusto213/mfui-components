package org.mindtec.uikit.ui.components.select

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.mindtec.uikit.ui.theme.*
import org.mindtec.uikit.utils.Fonts

@Composable
fun <T> SearchDropdownMenu(
    title: String,
    options: List<T>,
    filter: (T, String) -> Boolean,
    optionLabel: (T) -> String,
    optionSelected: (T?) -> Unit,
) {
    var text by remember { mutableStateOf("") }
    var isDropdownVisible by remember { mutableStateOf(false) }

    val filteredItems = options.filter {
        filter.invoke(it, text)
    }

    Column {
        Text(
            modifier = Modifier.padding(bottom = 6.dp),
            text = title,
            style = Fonts.textRegular(16.sp)
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = text,
            onValueChange = {
                text = it
                isDropdownVisible = it.isNotEmpty()
            },
            suffix = {
                Card(
                    modifier = Modifier.clickable {
                        text = ""
                        optionSelected.invoke(null)
                    },
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(
                        containerColor = grayInactive
                    )
                ) {
                    Icon(
                        modifier = Modifier.size(15.dp),
                        imageVector = Icons.Default.Clear,
                        tint = white,
                        contentDescription = "Clear"
                    )
                }
            },
            trailingIcon = {
                IconButton(onClick = { isDropdownVisible = !isDropdownVisible }) {
                    Icon(
                        imageVector = if (isDropdownVisible) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Toggle dropdown"
                    )
                }
            }
        )

        if (isDropdownVisible && filteredItems.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = white)
            ) {
                LazyColumn {
                    items(filteredItems) { item ->
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    text = optionLabel(item)
                                    optionSelected.invoke(item)
                                    isDropdownVisible = false
                                }
                                .padding(16.dp),
                            text = optionLabel(item),
                            style = Fonts.textLight(18.sp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchDropdownMenuPreview() {
    val options = listOf("Opción 1", "Opción 2", "Opción 3")
    var selectedOption by remember { mutableStateOf("") }

    SearchDropdownMenu(
        title = "Selecciona una opción",
        options = options,
        filter = { item, text -> item.contains(text, ignoreCase = true) },
        optionLabel = { it },
        optionSelected = { selectedOption = it ?: "" }
    )
}
