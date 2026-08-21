package org.mindtec.uikit.ui.components.text_field

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.mindtec.uikit.R
import org.mindtec.uikit.ui.components.country_code_picker.Country
import org.mindtec.uikit.ui.components.country_code_picker.CountryPickerDialog
import org.mindtec.uikit.ui.components.country_code_picker.PickerCustomization
import org.mindtec.uikit.ui.theme.*
import org.mindtec.uikit.utils.Fonts

@Composable
fun TextFieldCountry(
    modifier: Modifier = Modifier,
    title: String,
    value: String = "",
    lines: Int = 1,
    titleFontSize: TextUnit = 16.sp,
    showError: Boolean = false,
    onValueChange: (Country) -> Unit,
) {
    var textState by remember { mutableStateOf(value) }
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = Fonts.textRegular(titleFontSize)
            )
        }

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .background(white)
                .clickable {
                    showDialog = true
                },
            value = textState,
            colors = TextFieldDefaults.colors(
                disabledContainerColor = white,
                disabledIndicatorColor = grayIndicator,
                disabledTextColor = black
            ),
            onValueChange = {},
            minLines = lines,
            maxLines = lines,
            enabled = false,
            readOnly = true,
        )

        if (showError) {
            Text(
                text = stringResource(R.string.uikit_field_required_error),
                style = Fonts.textRegular(12.sp, red200)
            )
        }
    }

    if (showDialog) {
        CountryPickerDialog(
            modifier = Modifier.clip(shape = RoundedCornerShape(10.dp)),
            onDismissRequest = { showDialog = false },
            onItemClicked = { country ->
                onValueChange.invoke(country)
                textState = country.name
                showDialog = false
            },
            listOfCountry = Country.getAllCountries(),
            pickerCustomization = PickerCustomization(),
            itemPadding = 10,
            backgroundColor = white
        )
    }
}

@Preview
@Composable
private fun TextFieldCountryPreview() {
    TextFieldCountry(
        title = "Título",
        value = "Valor",
        titleFontSize = 16.sp,
        showError = false,
        onValueChange = {}
    )
}
