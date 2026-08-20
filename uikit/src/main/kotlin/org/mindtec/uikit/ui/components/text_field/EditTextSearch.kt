package org.mindtec.uikit.ui.components.text_field

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.mindtec.uikit.R
import org.mindtec.uikit.ui.theme.*
import org.mindtec.uikit.utils.Fonts

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTextSearch(
    modifier: Modifier = Modifier,
    title: String,
    newText: String? = null,
    onSearch: (String) -> Unit = {},
    onClickClear: () -> Unit = {},
    onFocusChanged: (Boolean) -> Unit = {},
    keyboardNumeric: Boolean = false
) {
    var text by remember { mutableStateOf("") }
    var previousText by remember { mutableStateOf("") }

    LaunchedEffect(newText) {
        newText?.let {
            if (it != text) {
                text = it
            }
        }
    }

    LaunchedEffect(text) {
        try {
            if ((text.isNotEmpty() && text.length % 3 == 0) || (text.isEmpty() && previousText.isNotEmpty())) {
                onSearch(text)
            }
            previousText = text
        } catch (_: Exception) { }
    }

    OutlinedTextField(
        modifier = modifier
            .height(48.dp)
            .onFocusChanged { focusState ->
                try {
                    onFocusChanged(focusState.isFocused)
                } catch (_: Exception) { }
            },
        value = text,
        onValueChange = {
            try {
                text = if (keyboardNumeric) {
                    it.filter { char -> char.isDigit() }
                } else {
                    it
                }
            } catch (_: Exception) { }
        },
        singleLine = true,
        placeholder = {
            Text(
                text = title,
                style = Fonts.textLight(14.sp, grayPlaceholder)
            )
        },
        suffix = {
            Card(
                modifier = Modifier.clickable {
                    try {
                        text = ""
                        onClickClear.invoke()
                    } catch (_: Exception) { }
                },
                shape = CircleShape,
                colors = CardDefaults.cardColors(
                    containerColor = grayTextField
                )
            ) {
                Icon(
                    modifier = Modifier.size(12.dp),
                    imageVector = Icons.Default.Clear,
                    tint = white,
                    contentDescription = "Clear"
                )
            }
        },
        trailingIcon = {
            IconButton(onClick = {
                try {
                    onSearch(text)
                } catch (_: Exception) { }
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.uikit_ic_search),
                    contentDescription = null,
                    modifier = Modifier.size(15.dp),
                    tint = purple300
                )
            }
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = neutral400,
            unfocusedBorderColor = neutral400,
            containerColor = grayCard
        ),
        keyboardOptions = if (keyboardNumeric) {
            KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            )
        } else {
            KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            )
        },
        keyboardActions = KeyboardActions(
            onDone = {
                onSearch(text)
            }
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun EditTextPreview() {
    Column(modifier = Modifier.padding(16.dp)) {
        EditTextSearch(title = stringResource(R.string.uikit_search_placeholder))
    }
}
