package org.mindtec.uikit.ui.components.text_field

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.mindtec.uikit.ui.components.country_code_picker.CCPUtils
import org.mindtec.uikit.ui.components.country_code_picker.Country
import org.mindtec.uikit.ui.components.country_code_picker.CountryCodePickerTextField
import org.mindtec.uikit.R
import org.mindtec.uikit.ui.theme.*
import org.mindtec.uikit.utils.Fonts

@Composable
fun TextFieldPhoneNumberByCountry(
    modifier: Modifier = Modifier,
    title: String,
    code: String? = null,
    value: String = "",
    haveVisualTransformation: Boolean = true,
    showError: Boolean = true,
    onValueChange: (Triple<String, String, Boolean>) -> Unit
) {
    var country by remember { mutableStateOf(Country.Peru) }

    LaunchedEffect(code) {
        Country.getAllCountries().find { it.countryCode == code }?.let {
            country = it
        }
    }

    if (!LocalInspectionMode.current) {
        if (code.isNullOrEmpty()) {
            CCPUtils.getCountryAutomatically(context = LocalContext.current).let {
                it?.let {
                    country = it
                }
            }
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = title,
            style = Fonts.textRegular(16.sp)
        )

        CountryCodePickerTextField(
            modifier = Modifier
                .fillMaxWidth()
                .background(white),
            textStyle = MaterialTheme.typography.bodyMedium,
            number = value,
            trailingIcon = {
                IconButton(onClick = { onValueChange.invoke(Triple("", "", false)) }) {
                    Icon(
                        imageVector = Icons.Default.Clear, contentDescription = "Clear"
                    )
                }
            },
            onValueChange = { code, newValue, isValid ->
                onValueChange.invoke(Triple(code, newValue, isValid))
            },
            shape = RoundedCornerShape(4.dp),
            haveVisualTransformation = haveVisualTransformation,
            showSheet = true,
            selectedCountry = country
        )
    }

    if (showError) {
        Text(
            text = stringResource(R.string.uikit_field_required_error),
            style = Fonts.textRegular(12.sp, red200)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TextFieldPhoneNumberByCountryPreview() {
    Column(modifier = Modifier.fillMaxSize()) {
        TextFieldPhoneNumberByCountry(
            title = "Número de teléfono",
            value = "987654321",
            onValueChange = {}
        )
    }
}
