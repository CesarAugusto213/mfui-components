package org.mindtec.uikit.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import org.mindtec.uikit.ui.theme.black

object Fonts {
    fun textBold(
        fontSize: TextUnit,
        color: Color = black,
        textAlign: TextAlign = TextAlign.Start
    ) = textStyleBase(fontSize, FontWeight.Bold, color, textAlign)

    fun textSemiBold(
        fontSize: TextUnit,
        color: Color = black,
        textAlign: TextAlign = TextAlign.Start
    ) = textStyleBase(fontSize, FontWeight.SemiBold, color, textAlign)

    fun textRegular(
        fontSize: TextUnit,
        color: Color = black,
        textAlign: TextAlign = TextAlign.Start
    ) = textStyleBase(fontSize, FontWeight.Normal, color, textAlign)

    fun textLight(
        fontSize: TextUnit,
        color: Color = black,
        textAlign: TextAlign = TextAlign.Start
    ) = textStyleBase(fontSize, FontWeight.Light, color, textAlign)

    private fun textStyleBase(
        fontSize: TextUnit,
        fontWeight: FontWeight,
        color: Color = Color.Black,
        textAlign: TextAlign = TextAlign.Start
    ) = TextStyle(
        fontSize = fontSize,
        color = color,
        fontFamily = FontFamily.SansSerif,
        fontWeight = fontWeight,
        textAlign = textAlign
    )
}
