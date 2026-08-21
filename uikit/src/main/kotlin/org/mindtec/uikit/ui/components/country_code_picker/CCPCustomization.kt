package org.mindtec.uikit.ui.components.country_code_picker

import androidx.compose.ui.graphics.Color

data class PickerCustomization(
    var itemPadding: Int = 10,
    var dividerColor: Color = Color.LightGray,
    var showSearchClearIcon: Boolean = true,
    var showCountryCode: Boolean = true,
    var showFlag: Boolean = true,
    var showCountryIso: Boolean = false,
)

data class ViewCustomization(
    var showFlag: Boolean = true,
    var showCountryIso: Boolean = false,
    var showCountryName: Boolean = false,
    var showCountryCode: Boolean = true,
    var showArrow: Boolean = true,
    var clipToFull: Boolean = false,
)
